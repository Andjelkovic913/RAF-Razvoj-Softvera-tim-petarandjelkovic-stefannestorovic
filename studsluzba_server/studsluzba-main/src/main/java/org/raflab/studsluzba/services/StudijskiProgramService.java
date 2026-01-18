package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudijskiProgramService {

    private final StudijskiProgramRepository studijskiProgramRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final PredmetRepository predmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final IspitRepository ispitRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final UpisGodineRepository upisGodineRepository;
    private final UplataRepository uplataRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public Long createStudijskiProgram(StudijskiProgramRequest request) {
        StudijskiProgram studijskiProgram = converters.toStudijskiProgram(request);
        if (request.getSkolskaGodinaId() != null) {
            skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                    .ifPresent(studijskiProgram::setSkolskaGodina);
        }
        StudijskiProgram saved = studijskiProgramRepository.save(studijskiProgram);
        return saved.getId();
    }

    public List<StudijskiProgramResponse> getAllSortedDesc() {
        return studijskiProgramRepository.getAllSortedByGodinaDesc().stream()
                .map(entityMappers::fromStudijskiProgramToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<String> getAllOznaka() {
        return studijskiProgramRepository.findAllOznaka();
    }

    public StudijskiProgramResponse updateStudijskiProgram(Long id, StudijskiProgramRequest request) {
        StudijskiProgram program = studijskiProgramRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Ne postoji ovaj studijski program"));
        converters.updateStudijskiProgram(program, request);
        StudijskiProgram updated = studijskiProgramRepository.save(program);
        return entityMappers.fromStudijskiProgramToResponse(updated);
    }

    /**
     * Brise studijski program i sve zavisne entitete (kaskadno brisanje).
     * Redosled: StudentIndeks zavisnosti -> StudentIndeks -> Predmet zavisnosti -> Predmet -> StudijskiProgram
     */
    @Transactional
    public void deleteStudijskiProgram(Long id) {
        if (!studijskiProgramRepository.existsById(id)) {
            throw new EntityNotFoundException("Studijski program sa ID " + id + " ne postoji");
        }
        
        // 1. Dohvati sve StudentIndekse za ovaj studijski program
        List<StudentIndeks> indeksi = studentIndeksRepository.findByStudijskiProgramId(id);
        
        // 2. Za svaki StudentIndeks obrisi zavisne entitete
        for (StudentIndeks indeks : indeksi) {
            deleteStudentIndeksDependencies(indeks.getId());
        }
        
        // 3. Obrisi sve StudentIndekse
        studentIndeksRepository.deleteAll(indeksi);
        
        // 4. Dohvati sve Predmete za ovaj studijski program
        List<Predmet> predmeti = predmetRepository.findByStudProgramId(id);
        
        // 5. Za svaki Predmet obrisi zavisne entitete
        for (Predmet predmet : predmeti) {
            deletePredmetDependencies(predmet.getId());
        }
        
        // 6. Obrisi sve Predmete
        predmetRepository.deleteAll(predmeti);
        
        // 7. Na kraju obrisi sam studijski program
        studijskiProgramRepository.deleteById(id);
    }
    
    /**
     * Brise sve zavisne entitete za dati StudentIndeks.
     */
    private void deleteStudentIndeksDependencies(Long studentIndeksId) {
        // 1. Dohvati sve SlusaPredmet za ovaj indeks
        List<SlusaPredmet> slusaniPredmeti = slusaPredmetRepository.findByStudentIndeksId(studentIndeksId);
        
        // 2. Za svaki SlusaPredmet obrisi PredispitneObaveze i IzlazakNaIspit
        for (SlusaPredmet sp : slusaniPredmeti) {
            List<PredispitneObaveze> predispitne = predispitneObavezeRepository.findBySlusaPredmetId(sp.getId());
            predispitneObavezeRepository.deleteAll(predispitne);
            
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findBySlusaPredmetId(sp.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
        }
        
        // 3. Obrisi sve SlusaPredmet
        slusaPredmetRepository.deleteAll(slusaniPredmeti);
        
        // 4. Obrisi sve PrijavaIspita
        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByStudentIndeksId(studentIndeksId);
        prijavaIspitaRepository.deleteAll(prijave);
        
        // 5. Obrisi sve UpisGodine
        List<UpisGodine> upisi = upisGodineRepository.findByStudentIndeksId(studentIndeksId);
        upisGodineRepository.deleteAll(upisi);
        
        // 6. Obrisi sve Uplate
        List<Uplata> uplate = uplataRepository.findByStudentIndeksId(studentIndeksId);
        uplataRepository.deleteAll(uplate);
    }
    
    /**
     * Brise sve zavisne entitete za dati Predmet.
     */
    private void deletePredmetDependencies(Long predmetId) {
        // 1. Dohvati sve DrziPredmet za ovaj predmet
        List<DrziPredmet> drziPredmeti = drziPredmetRepository.findByPredmetId(predmetId);
        
        // 2. Za svaki DrziPredmet obrisi zavisne entitete
        for (DrziPredmet dp : drziPredmeti) {
            // Dohvati sve SlusaPredmet za ovaj DrziPredmet
            List<SlusaPredmet> slusaniPredmeti = slusaPredmetRepository.findByDrziPredmetId(dp.getId());
            
            for (SlusaPredmet sp : slusaniPredmeti) {
                List<PredispitneObaveze> predispitne = predispitneObavezeRepository.findBySlusaPredmetId(sp.getId());
                predispitneObavezeRepository.deleteAll(predispitne);
                
                List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findBySlusaPredmetId(sp.getId());
                izlazakNaIspitRepository.deleteAll(izlasci);
            }
            
            slusaPredmetRepository.deleteAll(slusaniPredmeti);
            
            // Dohvati sve Ispite za ovaj DrziPredmet
            List<Ispit> ispiti = ispitRepository.findByDrziPredmetId(dp.getId());
            for (Ispit ispit : ispiti) {
                List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findByIspitId(ispit.getId());
                izlazakNaIspitRepository.deleteAll(izlasci);
                
                List<PrijavaIspita> prijave = prijavaIspitaRepository.findAllByIspitId(ispit.getId());
                prijavaIspitaRepository.deleteAll(prijave);
            }
            ispitRepository.deleteAll(ispiti);
        }
        
        // 3. Obrisi sve DrziPredmet
        drziPredmetRepository.deleteAll(drziPredmeti);
        
        // 4. Obrisi sve Ispite direktno vezane za predmet
        List<Ispit> ispiti = ispitRepository.findByPredmetId(predmetId);
        for (Ispit ispit : ispiti) {
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findByIspitId(ispit.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
            
            List<PrijavaIspita> prijave = prijavaIspitaRepository.findAllByIspitId(ispit.getId());
            prijavaIspitaRepository.deleteAll(prijave);
        }
        ispitRepository.deleteAll(ispiti);
    }

    public List<StudijskiProgram> findByOznaka(String oznaka) {
        return studijskiProgramRepository.findByOznaka(oznaka);
    }
}

