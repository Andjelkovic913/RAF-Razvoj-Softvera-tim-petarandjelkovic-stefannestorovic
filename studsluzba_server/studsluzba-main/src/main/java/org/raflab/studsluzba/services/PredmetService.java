package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.dtos.PredmetRequest;
import org.raflab.studsluzba.controllers.response.AverageGradeResponse;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.dtos.PredmetReportDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.raflab.studsluzba.controllers.response.YearlyGradeStatsResponse;
import org.raflab.studsluzba.controllers.response.PredmetGradeStatsResponse;

@Service
@RequiredArgsConstructor
public class PredmetService {

    private final PredmetRepository predmetRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final IspitRepository ispitRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public PredmetResponse create(PredmetRequest req) {
        if (predmetRepository.existsBySifra(req.getSifra())) {
            throw new IllegalArgumentException("Sifra predmeta vec postoji");
        }
        StudijskiProgram sp = studijskiProgramRepository.findById(req.getStudProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        Predmet p = converters.toPredmet(req);
        p.setStudProgram(sp);
        Predmet saved = predmetRepository.save(p);
        return entityMappers.fromPredmetToResponse(saved);
    }

    public List<PredmetResponse> getByStudProgram(Long studProgramId, Boolean obavezan) {
        StudijskiProgram sp = studijskiProgramRepository.findById(studProgramId)
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        List<Predmet> predmets = obavezan == null ? predmetRepository.findByStudProgram(sp) : predmetRepository.getPredmetsByStudProgramAndObavezan(sp, obavezan);
        
        return predmets.stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PredmetResponse> getAll() {
        List<Predmet> predmets = new ArrayList<>();
        predmetRepository.findAll().forEach(predmets::add);
        return predmets.stream().map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }

    public PredmetResponse getById(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + id + " ne postoji"));
        return entityMappers.fromPredmetToResponse(predmet);
    }

    public PredmetResponse update(Long id, PredmetRequest req) {
        Predmet existing = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + id + " ne postoji"));

        if (!existing.getSifra().equals(req.getSifra()) && predmetRepository.existsBySifra(req.getSifra())) {
            throw new IllegalArgumentException("Sifra predmeta vec postoji");
        }

        StudijskiProgram sp = studijskiProgramRepository.findById(req.getStudProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        existing.setSifra(req.getSifra());
        existing.setNaziv(req.getNaziv());
        existing.setOpis(req.getOpis());
        existing.setEspb(req.getEspb());
        existing.setSemestar(req.getSemestar());
        existing.setFondCasovaVezbe(req.getFondCasovaVezbe());
        existing.setFondCasovaPredavanja(req.getFondCasovaPredavanja());
        existing.setObavezan(req.isObavezan());
        existing.setStudProgram(sp);

        Predmet updated = predmetRepository.save(existing);
        return entityMappers.fromPredmetToResponse(updated);
    }

    /**
     * Brise predmet i sve zavisne entitete (kaskadno brisanje).
     * Redosled: IzlazakNaIspit -> PrijavaIspita -> Ispit -> PredispitneObaveze -> SlusaPredmet -> DrziPredmet -> Predmet
     */
    @Transactional
    public void delete(Long id) {
        if (!predmetRepository.existsById(id)) {
            throw new EntityNotFoundException("Predmet sa ID " + id + " ne postoji");
        }
        
        // 1. Dohvati sve DrziPredmet za ovaj predmet
        List<DrziPredmet> drziPredmeti = drziPredmetRepository.findByPredmetId(id);
        
        // 2. Za svaki DrziPredmet obrisi zavisne entitete
        for (DrziPredmet dp : drziPredmeti) {
            deleteDrziPredmetDependencies(dp.getId());
        }
        
        // 3. Obrisi sve DrziPredmet
        drziPredmetRepository.deleteAll(drziPredmeti);
        
        // 4. Obrisi sve Ispite direktno vezane za predmet (ako postoje)
        List<Ispit> ispiti = ispitRepository.findByPredmetId(id);
        for (Ispit ispit : ispiti) {
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findByIspitId(ispit.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
            
            List<PrijavaIspita> prijave = prijavaIspitaRepository.findAllByIspitId(ispit.getId());
            prijavaIspitaRepository.deleteAll(prijave);
        }
        ispitRepository.deleteAll(ispiti);
        
        // 5. Na kraju obrisi sam predmet
        predmetRepository.deleteById(id);
    }
    
    /**
     * Brise sve zavisne entitete za dati DrziPredmet.
     */
    private void deleteDrziPredmetDependencies(Long drziPredmetId) {
        // 1. Dohvati sve SlusaPredmet za ovaj DrziPredmet
        List<SlusaPredmet> slusaniPredmeti = slusaPredmetRepository.findByDrziPredmetId(drziPredmetId);
        
        // 2. Za svaki SlusaPredmet obrisi PredispitneObaveze i IzlazakNaIspit
        for (SlusaPredmet sp : slusaniPredmeti) {
            List<PredispitneObaveze> predispitne = predispitneObavezeRepository.findBySlusaPredmetId(sp.getId());
            predispitneObavezeRepository.deleteAll(predispitne);
            
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findBySlusaPredmetId(sp.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
        }
        
        // 3. Obrisi sve SlusaPredmet
        slusaPredmetRepository.deleteAll(slusaniPredmeti);
        
        // 4. Dohvati sve Ispite za ovaj DrziPredmet
        List<Ispit> ispiti = ispitRepository.findByDrziPredmetId(drziPredmetId);
        
        // 5. Za svaki Ispit obrisi IzlazakNaIspit i PrijavaIspita
        for (Ispit ispit : ispiti) {
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findByIspitId(ispit.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
            
            List<PrijavaIspita> prijave = prijavaIspitaRepository.findAllByIspitId(ispit.getId());
            prijavaIspitaRepository.deleteAll(prijave);
        }
        
        // 6. Obrisi sve Ispite
        ispitRepository.deleteAll(ispiti);
    }

    public List<PredmetReportDTO> findAllForReportByStudProgram(String studProgramOznaka) {
        return predmetRepository.findAllForReportByStudProgram(studProgramOznaka);
    }


    public AverageGradeResponse getAverageGradeByYearRange(Long predmetId, Integer startYear, Integer endYear) {
        if (startYear > endYear) {
            throw new IllegalArgumentException("Početna godina mora biti manja ili jednaka krajnjoj godini");
        }
        
        Predmet predmet = predmetRepository.findById(predmetId)
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + predmetId + " ne postoji"));
        
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        
        Double averageGrade = izlazakNaIspitRepository.calculateAverageGradeByPredmetAndDateRange(
                predmetId, startDate, endDate);
        
        Long totalPassed = izlazakNaIspitRepository.countPassedExamsByPredmetAndDateRange(
                predmetId, startDate, endDate);
        
        return new AverageGradeResponse(
                predmet.getId(),
                predmet.getNaziv(),
                predmet.getSifra(),
                startYear,
                endYear,
                averageGrade,
                totalPassed
        );
    }


    public List<YearlyGradeStatsResponse> getYearlyGradeStats(Long predmetId, Integer startYear, Integer endYear) {
        if (startYear > endYear) {
            throw new IllegalArgumentException("Početna godina mora biti manja ili jednaka krajnjoj godini");
        }
        
        if (!predmetRepository.existsById(predmetId)) {
            throw new EntityNotFoundException("Predmet sa ID " + predmetId + " ne postoji");
        }
        
        List<YearlyGradeStatsResponse> stats = new ArrayList<>();
        
        for (int year = startYear; year <= endYear; year++) {
            Long totalAttempts = izlazakNaIspitRepository.countTotalExamAttemptsByPredmetAndYear(predmetId, year);
            Long passed = izlazakNaIspitRepository.countPassedExamsByPredmetAndYear(predmetId, year);
            Double avgGrade = izlazakNaIspitRepository.calculateAverageGradeByPredmetAndYear(predmetId, year);
            
            int brStudenata = totalAttempts != null ? totalAttempts.intValue() : 0;
            int polozilo = passed != null ? passed.intValue() : 0;
            Double procenatProlaz = brStudenata > 0 ? (polozilo * 100.0 / brStudenata) : null;
            
            // Dodaj samo ako ima podataka za tu godinu
            if (brStudenata > 0) {
                stats.add(new YearlyGradeStatsResponse(
                    year,
                    brStudenata,
                    polozilo,
                    avgGrade,
                    procenatProlaz
                ));
            }
        }
        
        return stats;
    }

    // vraca statistiku ocena za sve predmete u rasponu godina.
    public List<PredmetGradeStatsResponse> getAllPredmetiGradeStats(Integer startYear, Integer endYear) {
        if (startYear > endYear) {
            throw new IllegalArgumentException("Početna godina mora biti manja ili jednaka krajnjoj godini");
        }
        
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        
        List<PredmetGradeStatsResponse> stats = new ArrayList<>();
        List<Predmet> allPredmeti = new ArrayList<>();
        predmetRepository.findAll().forEach(allPredmeti::add);
        
        for (Predmet predmet : allPredmeti) {
            Double avgGrade = izlazakNaIspitRepository.calculateAverageGradeByPredmetAndDateRange(predmet.getId(), startDate, endDate);
            Long passed = izlazakNaIspitRepository.countPassedExamsByPredmetAndDateRange(predmet.getId(), startDate, endDate);
            
            int polozilo = passed != null ? passed.intValue() : 0;

            if (polozilo > 0) {
                stats.add(new PredmetGradeStatsResponse(
                    predmet.getId(),
                    predmet.getSifra(),
                    predmet.getNaziv(),
                    polozilo, // brStudenata = polozilo
                    polozilo,
                    avgGrade,
                    100.0 // procenatProlaz = 100%
                ));
            }
        }
        
        return stats;
    }
}
