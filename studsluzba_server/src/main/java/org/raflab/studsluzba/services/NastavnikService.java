package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.NastavnikReportDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class NastavnikService {

    private final NastavnikRepository nastavnikRepository;
    private final NastavnikZvanjeRepository nastavnikZvanjeRepository;
    private final NastavnikObrazovanjeRepository nastavnikObrazovanjeRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final IspitRepository ispitRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public Long save(NastavnikRequest request) {
        Nastavnik nastavnik = converters.toNastavnik(request);
        if (request.getZvanjeIds() != null && !request.getZvanjeIds().isEmpty()) {
            List<NastavnikZvanje> zvanja = nastavnikZvanjeRepository.findAllById(request.getZvanjeIds());
            nastavnik.setZvanja(new HashSet<>(zvanja));
        }
        Nastavnik saved = nastavnikRepository.save(nastavnik);
        return saved.getId();
    }

    public List<NastavnikResponse> findAll() {
        return StreamSupport.stream(nastavnikRepository.findAll().spliterator(), false)
                .map(entityMappers::fromNastavnikToResponse)
                .collect(Collectors.toList());
    }

    public List<NastavnikReportDTO> findAllForReport() {
        return nastavnikRepository.findAllForReport();
    }

    public NastavnikResponse findById(Long id) {
        Nastavnik nastavnik = nastavnikRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + id + " ne postoji"));
        return entityMappers.fromNastavnikToResponse(nastavnik);
    }

    public List<NastavnikResponse> findByImeAndPrezime(String ime, String prezime) {
        return nastavnikRepository.findByImeAndPrezime(ime, prezime).stream()
                .map(entityMappers::fromNastavnikToResponse)
                .collect(Collectors.toList());
    }

    public List<NastavnikResponse> findByZvanje(String zvanje) {
        List<NastavnikZvanje> nastavnikZvanja = nastavnikZvanjeRepository.findByZvanje(zvanje);
        
        if (nastavnikZvanja == null || nastavnikZvanja.isEmpty()) {
            return List.of();
        }
        
        return nastavnikZvanja.stream()
                .map(nz -> entityMappers.fromNastavnikToResponse(nz.getNastavnik()))
                .collect(Collectors.toList());
    }

    public List<NastavnikResponse> findByEmail(String email) {
        List<Nastavnik> nastavnici = nastavnikRepository.findByEmail(email);
        
        if (nastavnici == null || nastavnici.isEmpty()) {
            return List.of();
        }
        
        return nastavnici.stream()
                .map(entityMappers::fromNastavnikToResponse)
                .collect(Collectors.toList());
    }

    public NastavnikResponse updateNastavnik(Long id, NastavnikRequest request) {
        Nastavnik nastavnik = nastavnikRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + id + " ne postoji"));
        converters.updateNastavnik(nastavnik, request);
        if (request.getZvanjeIds() != null) {
            List<NastavnikZvanje> zvanja = nastavnikZvanjeRepository.findAllById(request.getZvanjeIds());
            nastavnik.setZvanja(new HashSet<>(zvanja));
        }
        Nastavnik updated = nastavnikRepository.save(nastavnik);
        return entityMappers.fromNastavnikToResponse(updated);
    }

    /**
     * Brise nastavnika i sve zavisne entitete (kaskadno brisanje).
     * Redosled: IzlazakNaIspit -> PrijavaIspita -> Ispit -> PredispitneObaveze -> SlusaPredmet -> DrziPredmet -> NastavnikZvanje -> NastavnikObrazovanje -> Nastavnik
     */
    @Transactional
    public void deleteNastavnik(Long id) {
        if (!nastavnikRepository.existsById(id)) {
            throw new EntityNotFoundException("Nastavnik sa ID " + id + " ne postoji");
        }
        
        // 1. Dohvati sve DrziPredmet za ovog nastavnika
        List<DrziPredmet> drziPredmeti = drziPredmetRepository.findByNastavnikId(id);
        
        // 2. Za svaki DrziPredmet obrisi zavisne entitete
        for (DrziPredmet dp : drziPredmeti) {
            deleteDrziPredmetDependencies(dp.getId());
        }
        
        // 3. Obrisi sve DrziPredmet
        drziPredmetRepository.deleteAll(drziPredmeti);
        
        // 4. Obrisi NastavnikZvanje
        List<NastavnikZvanje> zvanja = nastavnikZvanjeRepository.findByNastavnikId(id);
        nastavnikZvanjeRepository.deleteAll(zvanja);
        
        // 5. Obrisi NastavnikObrazovanje
        List<NastavnikObrazovanje> obrazovanja = nastavnikObrazovanjeRepository.findByNastavnikId(id);
        nastavnikObrazovanjeRepository.deleteAll(obrazovanja);
        
        // 6. Na kraju obrisi samog nastavnika
        nastavnikRepository.deleteById(id);
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
}
