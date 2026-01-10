package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.response.ObnovaGodineResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObnovaGodineService {

    private final ObnovaGodineRepository obnovaGodineRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final UpisGodineRepository upisGodineRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final EntityMappers entityMappers;

    @Transactional(readOnly = true)
    public List<ObnovaGodineResponse> getObnovaGodineByStudentIndeks(Long studentIndeksId) {
        List<ObnovaGodine> obnovaGodines = obnovaGodineRepository.findByStudentIndeksId(studentIndeksId);
        
        return obnovaGodines.stream()
                .map(entityMappers::fromObnovaGodineToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createObnovaGodine(ObnovaGodineRequest request) {
        // da li entiteti postoje
        StudentIndeks studentIndeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new EntityNotFoundException("StudentIndeks sa ID " + request.getStudentIndeksId() + " ne postoji"));
        
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("SkolskaGodina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));
        
        UpisGodine prethodniUpis = null;
        if (request.getPrethodniUpisId() != null) {
            prethodniUpis = upisGodineRepository.findById(request.getPrethodniUpisId())
                    .orElseThrow(() -> new EntityNotFoundException("UpisGodine sa ID " + request.getPrethodniUpisId() + " ne postoji"));
        }
        
         // student ne obnavlja godinu koja je vec prosla
        validateYearRenewal(studentIndeks, request.getGodina(), request.getSkolskaGodinaId());
        
        //automatski racuna predmete koje student nije polozio
        List<SlusaPredmet> allEnrolledSubjects = slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(studentIndeks.getId());
        List<DrziPredmet> failedSubjects = new ArrayList<>();
        int failedESPB = 0;
        
        for (SlusaPredmet slusaPredmet : allEnrolledSubjects) {
            // da lie je student prosao premdet
            boolean passed = izlazakNaIspitRepository.findBySlusaPredmetId(slusaPredmet.getId()).stream()
                    .anyMatch(izlazak -> izlazak.getOcena() != null && izlazak.getOcena() >= 6 && !izlazak.isPonistio());
            
            if (!passed) {
                DrziPredmet drziPredmet = slusaPredmet.getDrziPredmet();
                failedSubjects.add(drziPredmet);
                
                if (drziPredmet.getPredmet() != null && drziPredmet.getPredmet().getEspb() != null) {
                    failedESPB += drziPredmet.getPredmet().getEspb();
                }
            }
        }
        
        if (failedSubjects.isEmpty()) {
            throw new IllegalArgumentException("Student nema nepoloženih predmeta. Obnova godine nije potrebna.");
        }
        
        // uzmianje predmeta iz sledece godine koje student zeli da polozi
        List<DrziPredmet> nextYearSubjects = new ArrayList<>();
        int nextYearESPB = 0;
        
        if (request.getNextYearDrziPredmetIds() != null && !request.getNextYearDrziPredmetIds().isEmpty()) {
            for (Long drziPredmetId : request.getNextYearDrziPredmetIds()) {
                DrziPredmet drziPredmet = drziPredmetRepository.findById(drziPredmetId)
                        .orElseThrow(() -> new EntityNotFoundException("DrziPredmet sa ID " + drziPredmetId + " ne postoji"));
                nextYearSubjects.add(drziPredmet);
                
                if (drziPredmet.getPredmet() != null && drziPredmet.getPredmet().getEspb() != null) {
                    nextYearESPB += drziPredmet.getPredmet().getEspb();
                }
            }
        }
        
        //racuna ESPB bodove da budu max 60
        int totalESPB = failedESPB + nextYearESPB;
        if (totalESPB > 60) {
            throw new IllegalArgumentException("Ukupan broj ESPB poena (" + totalESPB + ") premašuje maksimalni dozvoljeni limit od 60 ESPB. " +
                    "Nepoloženi predmeti: " + failedESPB + " ESPB, Predmeti sledeće godine: " + nextYearESPB + " ESPB");
        }
        
        // spajanje svih poloz. i nepoloz. predmeta
        List<DrziPredmet> allSubjects = new ArrayList<>();
        allSubjects.addAll(failedSubjects);
        allSubjects.addAll(nextYearSubjects);
        
        ObnovaGodine obnovaGodine = new ObnovaGodine();
        obnovaGodine.setStudentIndeks(studentIndeks);
        obnovaGodine.setSkolskaGodina(skolskaGodina);
        obnovaGodine.setGodina(request.getGodina());
        obnovaGodine.setDatumUpisa(LocalDate.now());
        obnovaGodine.setPrenetiESPB(request.getPrenetiESPB() != null ? request.getPrenetiESPB() : 0);
        obnovaGodine.setPrethodniUpis(prethodniUpis);
        obnovaGodine.setRazlogObnove(request.getRazlogObnove());
        
        ObnovaGodine savedObnovaGodine = obnovaGodineRepository.save(obnovaGodine);
        
        // Pravimo SlusaPredmet z svaki predmet koji ce biti nepolozen na pocetku
        List<SlusaPredmet> slusaPredmets = new ArrayList<>();
        for (DrziPredmet drziPredmet : allSubjects) {
            SlusaPredmet slusaPredmet = new SlusaPredmet();
            slusaPredmet.setStudentIndeks(studentIndeks);
            slusaPredmet.setDrziPredmet(drziPredmet);
            slusaPredmet.setUpisGodine(savedObnovaGodine);
            slusaPredmets.add(slusaPredmet);
        }
        
        slusaPredmetRepository.saveAll(slusaPredmets);
        
        return savedObnovaGodine.getId();
    }


    @Transactional(readOnly = true)
    public org.raflab.studsluzba.controllers.response.FailedSubjectsResponse getFailedSubjectsForStudent(Long studentIndeksId) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new EntityNotFoundException("StudentIndeks sa ID " + studentIndeksId + " ne postoji"));
        
        if (!studentIndeks.isAktivan()) {
            throw new IllegalArgumentException("StudentIndeks sa ID " + studentIndeksId + " nije aktivan");
        }
        
        List<SlusaPredmet> allEnrolledSubjects = slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(studentIndeksId);
        
        List<org.raflab.studsluzba.controllers.response.FailedSubjectsResponse.FailedSubjectInfo> failedSubjects = new ArrayList<>();
        int totalFailedESPB = 0;
        
        for (SlusaPredmet slusaPredmet : allEnrolledSubjects) {
            boolean passed = izlazakNaIspitRepository.findBySlusaPredmetId(slusaPredmet.getId()).stream()
                    .anyMatch(izlazak -> izlazak.getOcena() != null && izlazak.getOcena() >= 6 && !izlazak.isPonistio());
            
            if (!passed) {
                DrziPredmet drziPredmet = slusaPredmet.getDrziPredmet();
                Predmet predmet = drziPredmet.getPredmet();
                
                org.raflab.studsluzba.controllers.response.FailedSubjectsResponse.FailedSubjectInfo info =
                        new org.raflab.studsluzba.controllers.response.FailedSubjectsResponse.FailedSubjectInfo();
                info.setDrziPredmetId(drziPredmet.getId());
                info.setPredmetId(predmet.getId());
                info.setPredmetSifra(predmet.getSifra());
                info.setPredmetNaziv(predmet.getNaziv());
                info.setEspb(predmet.getEspb());
                info.setGodina((predmet.getSemestar() + 1) / 2); // Calculate year from semester
                info.setSemestar(predmet.getSemestar());
                
                failedSubjects.add(info);
                totalFailedESPB += predmet.getEspb() != null ? predmet.getEspb() : 0;
            }
        }
        
        org.raflab.studsluzba.controllers.response.FailedSubjectsResponse response =
                new org.raflab.studsluzba.controllers.response.FailedSubjectsResponse();
        response.setStudentIndeksId(studentIndeksId);
        response.setStudentName(studentIndeks.getStudent().getIme() + " " + studentIndeks.getStudent().getPrezime());
        response.setFailedSubjects(failedSubjects);
        response.setTotalFailedESPB(totalFailedESPB);
        response.setAvailableESPBForNextYear(60 - totalFailedESPB);
        
        return response;
    }


    private void validateYearRenewal(StudentIndeks studentIndeks, Integer renewalYear, Long skolskaGodinaId) {
        List<UpisGodine> allEnrollments = upisGodineRepository.findByStudentIndeksId(studentIndeks.getId());
        
        if (allEnrollments.isEmpty()) {
            throw new IllegalArgumentException("Student nema prethodnih upisa. Obnova godine nije moguća.");
        }
        
        Integer currentYear = 0;
        for (UpisGodine enrollment : allEnrollments) {
            if (!(enrollment instanceof ObnovaGodine) && enrollment.getGodina() != null) {
                currentYear = Math.max(currentYear, enrollment.getGodina());
            }
        }
        
        if (!renewalYear.equals(currentYear)) {
            throw new IllegalArgumentException(
                    "Student može obnoviti samo trenutnu godinu (" + currentYear + 
                    "). Pokušaj obnove godine " + renewalYear + " nije dozvoljen."
            );
        }

        List<ObnovaGodine> existingRenewals = obnovaGodineRepository.findByStudentIndeksId(studentIndeks.getId());
        boolean alreadyRenewedInSameSchoolYear = existingRenewals.stream()
                .anyMatch(renewal -> renewal.getGodina().equals(renewalYear) && 
                                    renewal.getSkolskaGodina().getId().equals(skolskaGodinaId));
        
        if (alreadyRenewedInSameSchoolYear) {
            throw new IllegalArgumentException(
                    "Student je već obnovio godinu " + renewalYear + 
                    " u ovoj školskoj godini. Ponovna obnova u istoj školskoj godini nije dozvoljena."
            );
        }
    }
}
