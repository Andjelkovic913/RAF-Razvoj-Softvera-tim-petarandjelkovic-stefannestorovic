package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.Uplata;
import org.raflab.studsluzba.model.dtos.ExchangeRateDTO;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.UplataRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UplataService {

    private final UplataRepository uplataRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final RestTemplate restTemplate;
    private final EntityMappers entityMappers;

    private static final String EXCHANGE_RATE_API_URL = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";

    /**
     * Kreira novu uplatu sa dohvatanjem trenutnog srednjeg kursa EUR-a
     */
    @Transactional
    public Long createUplata(UplataRequest request) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student sa ID-om " + request.getStudentIndeksId() + " nije pronađen"
                ));

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Školska godina sa ID-om " + request.getSkolskaGodinaId() + " nije pronađena"
                ));

        double srednjiKurs = fetchExchangeRate();

        Uplata uplata = new Uplata();
        uplata.setStudentIndeks(studentIndeks);
        uplata.setSkolskaGodina(skolskaGodina);
        uplata.setDatumUplate(LocalDateTime.now());
        uplata.setIznosRsd(request.getIznosRsd());
        uplata.setSrednjiKurs(srednjiKurs);
        uplata.setTip(request.getTip());
        uplata.setOpis(request.getOpis());

        Uplata saved = uplataRepository.save(uplata);
        log.info("Uplata kreirana sa ID-om: {}, iznos: {} RSD, kurs: {}", saved.getId(), saved.getIznosRsd(), saved.getSrednjiKurs());

        return saved.getId();
    }

    /**
     * Dohvata sve uplate
     */
    public List<UplataResponse> getAllUplate() {
        return uplataRepository.findAll().stream()
                .map(entityMappers::fromUplataToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Dohvata uplatu po ID-u
     */
    public UplataResponse getUplataById(Long id) {
        Uplata uplata = uplataRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Uplata sa ID-om " + id + " nije pronađena"
                ));
        return entityMappers.fromUplataToResponse(uplata);
    }

    /**
     * Dohvata sve uplate za određenog studenta
     */
    public List<UplataResponse> getUplateByStudentIndeksId(Long studentIndeksId) {
        if (!studentIndeksRepository.existsById(studentIndeksId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student sa ID-om " + studentIndeksId + " nije pronađen"
            );
        }
        return uplataRepository.findByStudentIndeksId(studentIndeksId).stream()
                .map(entityMappers::fromUplataToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Dohvata srednji kurs EUR-a iz eksternog API-ja
     */
    private double fetchExchangeRate() {
        try {
            log.debug("Dohvatanje kursa sa API-ja: {}", EXCHANGE_RATE_API_URL);
            ExchangeRateDTO exchangeRate = restTemplate.getForObject(EXCHANGE_RATE_API_URL, ExchangeRateDTO.class);

            if (exchangeRate == null || exchangeRate.getExchangeMiddle() == null) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Nije moguće dohvatiti srednji kurs EUR-a"
                );
            }

            log.info("Srednji kurs EUR-a: {}", exchangeRate.getExchangeMiddle());
            return exchangeRate.getExchangeMiddle();

        } catch (Exception e) {
            log.error("Greška pri dohvatanju kursa: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Nije moguće dohvatiti srednji kurs EUR-a: " + e.getMessage()
            );
        }
    }

}
