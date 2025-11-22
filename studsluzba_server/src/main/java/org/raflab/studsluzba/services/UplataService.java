package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.controllers.request.DodajUplataRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.PreostaliIznosResponse;
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
    private static final double SKOLARINA = 3000.0; 

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
                        "Skolska godina sa ID-om " + request.getSkolskaGodinaId() + " nije pronađena"
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
     * Kreira novu uplatu sa samo studentIndeksId i iznosDinara
     * Automatski dohvata aktivnu školsku godinu i trenutni srednji kurs
     */
    @Transactional
    public Long dodajUplata(DodajUplataRequest request) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student sa ID-om " + request.getStudentIndeksId() + " nije pronađen"
                ));

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nije pronađena aktivna školska godina"
                ));

        double srednjiKurs = fetchExchangeRate();

        Uplata uplata = new Uplata();
        uplata.setStudentIndeks(studentIndeks);
        uplata.setSkolskaGodina(skolskaGodina);
        uplata.setDatumUplate(LocalDateTime.now());
        uplata.setIznosRsd(request.getIznosDinara());
        uplata.setSrednjiKurs(srednjiKurs);
        uplata.setTip(Uplata.TipUplate.SKOLARINA);
        uplata.setStatus(Uplata.StatusUplate.U_TOKU);

        Uplata saved = uplataRepository.save(uplata);
        log.info("Uplata kreirana sa ID-om: {}, iznos: {} RSD, kurs: {}", saved.getId(), saved.getIznosRsd(), saved.getSrednjiKurs());

        return saved.getId();
    }

    /**
     * Kalkulise preostali iznos za uplatu za određenog studenta
     * Školarina = 3000 EUR
     */
    public PreostaliIznosResponse getPreostaliIznos(Long studentIndeksId) {
        if (!studentIndeksRepository.existsById(studentIndeksId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student sa ID-om " + studentIndeksId + " nije pronađen"
            );
        }

        List<Uplata> uplate = uplataRepository.findByStudentIndeksId(studentIndeksId);


        double ukupnoUplacenoEUR = 0.0;
        double ukupnoUplacenoRSD = 0.0;

        for (Uplata uplata : uplate) {
            double iznosEUR = uplata.getIznosRsd() / uplata.getSrednjiKurs();
            ukupnoUplacenoEUR += iznosEUR;
            ukupnoUplacenoRSD += uplata.getIznosRsd();
        }

        // Kalkulisi preostali iznos
        double preostaloEUR = SKOLARINA - ukupnoUplacenoEUR;
        // Za preostaloRSD, koristimo trenutni kurs sa API-ja
        double trenutniKurs = fetchExchangeRate();
        double preostaloRSD = preostaloEUR * trenutniKurs;

        // Ako je preostalo negativno, znači da je više uplaceno nego što je potrebno
        if (preostaloEUR < 0) {
            preostaloEUR = 0.0;
            preostaloRSD = 0.0;
        }

        PreostaliIznosResponse response = new PreostaliIznosResponse();
        response.setPreostaloEUR(Math.round(preostaloEUR * 100.0) / 100.0); // Zaokruzeno na 2 decimale
        response.setPreostaloRSD(Math.round(preostaloRSD * 100.0) / 100.0);
        response.setUkupnoUplacenoEUR(Math.round(ukupnoUplacenoEUR * 100.0) / 100.0);
        response.setUkupnoUplacenoRSD(Math.round(ukupnoUplacenoRSD * 100.0) / 100.0);

        log.info("Preostali iznos za studenta {}: {} EUR ({} RSD)", studentIndeksId, response.getPreostaloEUR(), response.getPreostaloRSD());

        return response;
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
