package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.response.UpisGodineResponse;
import org.raflab.studsluzba.controllers.response.UpisGodineSimpleResponse;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpisGodineService {

    private final UpisGodineRepository upisGodineRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final PredmetRepository predmetRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    @Transactional
    public Long createUpisGodine(UpisGodineRequest request) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student indeks sa ID " + request.getStudentIndeksId() + " ne postoji"));

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Skolska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));

        List<UpisGodine> postojeciUpisi = upisGodineRepository.findByStudentIndeksAndSkolskaGodinaAndGodina(
                request.getStudentIndeksId(),
                request.getSkolskaGodinaId(),
                request.getGodina());

        if (!postojeciUpisi.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Student (ID: %d) već ima upisanu %d. godinu u školskoj godini '%s' (ID: %d)",
                            request.getStudentIndeksId(), request.getGodina(), skolskaGodina.getNaziv(), request.getSkolskaGodinaId()));
        }

        // Validacija: student ne može da upise nižu godinu ako je već u višoj godini
        List<UpisGodine> sviUpisiStudenta = upisGodineRepository.findByStudentIndeksId(request.getStudentIndeksId());
        int maksimalnaGodina = sviUpisiStudenta.stream()
                .mapToInt(UpisGodine::getGodina)
                .max()
                .orElse(0);

        if (request.getGodina() < maksimalnaGodina) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Student ne može da upise %d. godinu jer je već upisao %d. godinu",
                            request.getGodina(), maksimalnaGodina));
        }

        // Validacija: provera da li student ima dovoljno ESPB bodova za upis godine
        validateESPBRequirements(studentIndeks, request.getGodina());

        UpisGodine upisGodine = converters.toUpisGodine(request);
        UpisGodine saved = upisGodineRepository.save(upisGodine);

        int semestar1 = (request.getGodina() - 1) * 2 + 1;
        int semestar2 = (request.getGodina() - 1) * 2 + 2;

        StudijskiProgram studijskiProgram = studentIndeks.getStudijskiProgram();
        if (studijskiProgram == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Student indeks nema dodeljen studijski program");
        }
        
        List<Integer> semestri = List.of(semestar1, semestar2);
        List<Predmet> predmetiZaGodinu = predmetRepository.findByStudProgramAndSemestarIn(studijskiProgram, semestri);

        for (Predmet predmet : predmetiZaGodinu) {
            List<DrziPredmet> drziPredmeti = drziPredmetRepository.findByPredmetIdAndSkolskaGodinaId(
                    predmet.getId(), skolskaGodina.getId());

            if (!drziPredmeti.isEmpty()) {
                DrziPredmet drziPredmet = drziPredmeti.get(0);
                
                SlusaPredmet slusaPredmet = new SlusaPredmet();
                slusaPredmet.setStudentIndeks(studentIndeks);
                slusaPredmet.setDrziPredmet(drziPredmet);
                slusaPredmet.setUpisGodine(saved);
                
                slusaPredmetRepository.save(slusaPredmet);
            }
        }

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<UpisGodineSimpleResponse> getUpisGodineByBrojIndeksa(int brojIndeksa) {
        List<UpisGodine> upisi = upisGodineRepository.findByBrojIndeksa(brojIndeksa);
        
        if (upisi.isEmpty()) {
            throw new EntityNotFoundException("Nisu pronađeni upisi godine za broj indeksa: " + brojIndeksa);
        }
        
        return upisi.stream()
                .map(ug -> {
                    UpisGodineSimpleResponse response = new UpisGodineSimpleResponse();
                    response.setGodina(ug.getGodina());
                    response.setDatumUpisa(ug.getDatumUpisa());
                    response.setPrenetiESPB(ug.getPrenetiESPB());
                    if (ug.getSkolskaGodina() != null) {
                        response.setSkolskaGodinaNaziv(ug.getSkolskaGodina().getNaziv());
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UpisGodineResponse> getUpisGodineByStudentIndeksId(Long studentIndeksId) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student indeks sa ID " + studentIndeksId + " ne postoji"));

        List<UpisGodine> upisi = upisGodineRepository.findByStudentIndeksId(studentIndeksId);
        return entityMappers.fromUpisGodineListToResponse(upisi);
    }

    @Transactional(readOnly = true)
    public List<SlusaPredmetResponse> getPredmetiZaUpisGodine(Long upisGodineId) {
        UpisGodine upisGodine = upisGodineRepository.findById(upisGodineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Upis godine sa ID " + upisGodineId + " ne postoji"));

        List<SlusaPredmet> slusaniPredmeti = slusaPredmetRepository.findByUpisGodineId(upisGodineId);
        return slusaniPredmeti.stream()
                .map(sp -> {
                    SlusaPredmetResponse response = new SlusaPredmetResponse();
                    response.setId(sp.getId());
                    response.setStudentIndeksId(sp.getStudentIndeks() != null ? sp.getStudentIndeks().getId() : null);
                    response.setUpisGodineId(sp.getUpisGodine() != null ? sp.getUpisGodine().getId() : null);
                    if (sp.getDrziPredmet() != null) {
                        response.setDrziPredmet(entityMappers.fromDrziPredmetToDTO(sp.getDrziPredmet()));
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * Validates if student has enough ESPB points to enroll in the requested year.
     * Requirements:
     * - 30 ESPB for 2nd year
     * - 90 ESPB for 3rd year
     * - 150 ESPB for 4th year
     */
    private void validateESPBRequirements(StudentIndeks studentIndeks, Integer godinaZaUpis) {
        // First year has no requirements
        if (godinaZaUpis == 1) {
            return;
        }

        // Calculate total earned ESPB
        List<SlusaPredmet> allEnrolledSubjects = slusaPredmetRepository.findByStudentIndeksId(studentIndeks.getId());
        int totalEarnedESPB = 0;

        for (SlusaPredmet slusaPredmet : allEnrolledSubjects) {
            // Check if student passed the subject
            boolean passed = izlazakNaIspitRepository.findBySlusaPredmetId(slusaPredmet.getId()).stream()
                    .anyMatch(izlazak -> izlazak.getOcena() != null && izlazak.getOcena() >= 6 && !izlazak.isPonistio());

            if (passed && slusaPredmet.getDrziPredmet() != null 
                    && slusaPredmet.getDrziPredmet().getPredmet() != null 
                    && slusaPredmet.getDrziPredmet().getPredmet().getEspb() != null) {
                totalEarnedESPB += slusaPredmet.getDrziPredmet().getPredmet().getEspb();
            }
        }

        // Define minimum ESPB requirements per year
        int requiredESPB = 0;
        switch (godinaZaUpis) {
            case 2:
                requiredESPB = 30;
                break;
            case 3:
                requiredESPB = 90;
                break;
            case 4:
                requiredESPB = 150;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nevažeća godina studija: " + godinaZaUpis);
        }

        if (totalEarnedESPB < requiredESPB) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Student nema dovoljno ESPB bodova za upis %d. godine. " +
                            "Potrebno: %d ESPB, Ostvareno: %d ESPB",
                            godinaZaUpis, requiredESPB, totalEarnedESPB));
        }
    }
}

