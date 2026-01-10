package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetAddRequest;
import org.raflab.studsluzba.controllers.request.DrziPredmetNewRequest;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DrziPredmetService {

    private final DrziPredmetRepository drziPredmetRepository;
    private final PredmetRepository predmetRepository;
    private final NastavnikRepository nastavnikRepository;
    private final Converters converters;

    @Transactional
    public void saveDrziPredmet(DrziPredmetRequest request) {

        List<DrziPredmetNewRequest> drziPredmetList = request.getDrziPredmet();
        List<DrziPredmetNewRequest> newDrziPredmetList = request.getNewDrziPredmet();

        // izvuci sve predmete iz baze po predmetId
        Map<Long, Predmet> predmetMap = predmetRepository.findByIdIn(
                drziPredmetList.stream().map(DrziPredmetNewRequest::getPredmetId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Predmet::getId, Function.identity()));

        // izvuci sve predmete iz baze po predmetNaziv za nove unose
        Map<String, Predmet> newPredmetMap = predmetRepository.findByNazivIn(
                newDrziPredmetList.stream().map(DrziPredmetNewRequest::getPredmetNaziv).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Predmet::getNaziv, Function.identity()));

        // izvuci sve nastavnike iz baze po email
        List<String> allEmails = Stream.concat(
                drziPredmetList.stream().map(DrziPredmetNewRequest::getEmailNastavnik),
                newDrziPredmetList.stream().map(DrziPredmetNewRequest::getEmailNastavnik)
        ).distinct().collect(Collectors.toList());

        Map<String, Nastavnik> nastavnikMap = nastavnikRepository.findByEmailIn(allEmails)
                .stream().collect(Collectors.toMap(Nastavnik::getEmail, Function.identity()));

        // kreiranje liste DrziPredmet objekata za cuvanje
        List<DrziPredmet> drziPredmetEntities = new ArrayList<>();

        for (DrziPredmetNewRequest drziPredmetRequest : drziPredmetList) {
            Predmet predmet = predmetMap.get(drziPredmetRequest.getPredmetId());
            Nastavnik nastavnik = nastavnikMap.get(drziPredmetRequest.getEmailNastavnik());

            if (predmet != null && nastavnik != null) {
                DrziPredmet drziPredmet = new DrziPredmet();
                drziPredmet.setPredmet(predmet);
                drziPredmet.setNastavnik(nastavnik);
                drziPredmetEntities.add(drziPredmet);
            }
        }

        for (DrziPredmetNewRequest newDrziPredmetRequest : newDrziPredmetList) {
            Predmet predmet = newPredmetMap.get(newDrziPredmetRequest.getPredmetNaziv());
            Nastavnik nastavnik = nastavnikMap.get(newDrziPredmetRequest.getEmailNastavnik());

            if (predmet != null && nastavnik != null) {
                DrziPredmet drziPredmet = new DrziPredmet();
                drziPredmet.setPredmet(predmet);
                drziPredmet.setNastavnik(nastavnik);
                drziPredmetEntities.add(drziPredmet);
            }
        }
        drziPredmetRepository.saveAll(drziPredmetEntities);
    }
    
    @Transactional(readOnly = true)
    public DrziPredmetResponse findById(Long id) {
        return drziPredmetRepository.findById(id)
                .map(converters::toDrziPredmetResponse)
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("DrziPredmet sa ID " + id + " ne postoji"));
    }

    @Transactional
    public Long create(DrziPredmetAddRequest request) {
        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Predmet sa ID " + request.getPredmetId() + " ne postoji"));
        Nastavnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Nastavnik sa ID " + request.getNastavnikId() + " ne postoji"));

        DrziPredmet drziPredmet = new DrziPredmet();
        drziPredmet.setPredmet(predmet);
        drziPredmet.setNastavnik(nastavnik);
        
        DrziPredmet saved = drziPredmetRepository.save(drziPredmet);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> getAll() {
        List<DrziPredmet> result = new ArrayList<>();
        drziPredmetRepository.findAll().forEach(result::add);
        return result.stream()
                .map(converters::toDrziPredmetResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DrziPredmetResponse update(Long id, DrziPredmetAddRequest request) {
        DrziPredmet existing = drziPredmetRepository.findById(id)
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("DrziPredmet sa ID " + id + " ne postoji"));

        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Predmet sa ID " + request.getPredmetId() + " ne postoji"));
        Nastavnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Nastavnik sa ID " + request.getNastavnikId() + " ne postoji"));

        existing.setPredmet(predmet);
        existing.setNastavnik(nastavnik);

        DrziPredmet updated = drziPredmetRepository.save(existing);
        return converters.toDrziPredmetResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!drziPredmetRepository.existsById(id)) {
            throw new javax.persistence.EntityNotFoundException("DrziPredmet sa ID " + id + " ne postoji");
        }
        drziPredmetRepository.deleteById(id);
    }
}