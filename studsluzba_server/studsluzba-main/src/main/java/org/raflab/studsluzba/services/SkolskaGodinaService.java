package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkolskaGodinaService {

    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final UpisGodineRepository upisGodineRepository;
    private final IspitniRokRepository ispitniRokRepository;
    private final IspitRepository ispitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;

    @Transactional
    public Long create(SkolskaGodinaRequest request) {
        SkolskaGodina skolskaGodina = new SkolskaGodina();
        skolskaGodina.setNaziv(request.getNaziv());
        skolskaGodina.setAktivna(request.isAktivna());

        SkolskaGodina saved = skolskaGodinaRepository.save(skolskaGodina);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public SkolskaGodinaResponse getById(Long id) {
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + id + " ne postoji"));
        return toResponse(skolskaGodina);
    }

    @Transactional(readOnly = true)
    public List<SkolskaGodinaResponse> getAll() {
        List<SkolskaGodina> result = new ArrayList<>();
        skolskaGodinaRepository.findAll().forEach(result::add);
        return result.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SkolskaGodinaResponse getAktivna() {
        SkolskaGodina aktivna = skolskaGodinaRepository.findAll().iterator().hasNext() 
                ? skolskaGodinaRepository.findAll().iterator().next() 
                : null;
        
        if (aktivna == null) {
            throw new EntityNotFoundException("Nema aktivne školske godine");
        }
        
        return toResponse(aktivna);
    }

    @Transactional
    public SkolskaGodinaResponse update(Long id, SkolskaGodinaRequest request) {
        SkolskaGodina existing = skolskaGodinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + id + " ne postoji"));

        existing.setNaziv(request.getNaziv());
        existing.setAktivna(request.isAktivna());

        SkolskaGodina updated = skolskaGodinaRepository.save(existing);
        return toResponse(updated);
    }

    /**
     * Brise skolsku godinu i sve zavisne entitete (kaskadno brisanje).
     * Redosled: IzlazakNaIspit -> PrijavaIspita -> Ispit -> IspitniRok -> UpisGodine -> StudijskiProgram -> SkolskaGodina
     * NAPOMENA: StudijskiProgram se NE brise jer moze pripadati vise skolskih godina
     */
    @Transactional
    public void delete(Long id) {
        if (!skolskaGodinaRepository.existsById(id)) {
            throw new EntityNotFoundException("Školska godina sa ID " + id + " ne postoji");
        }
        
        // 1. Dohvati sve IspitniRokove za ovu skolsku godinu
        List<IspitniRok> ispitniRokovi = ispitniRokRepository.findBySkolskaGodinaId(id);
        
        // 2. Za svaki IspitniRok obrisi zavisne entitete
        for (IspitniRok rok : ispitniRokovi) {
            deleteIspitniRokDependencies(rok.getId());
        }
        
        // 3. Obrisi sve IspitniRokove
        ispitniRokRepository.deleteAll(ispitniRokovi);
        
        // 4. Obrisi sve UpisGodine za ovu skolsku godinu
        List<UpisGodine> upisi = upisGodineRepository.findBySkolskaGodinaId(id);
        upisGodineRepository.deleteAll(upisi);
        
        // 5. Na kraju obrisi samu skolsku godinu
        skolskaGodinaRepository.deleteById(id);
    }
    
    /**
     * Brise sve zavisne entitete za dati IspitniRok.
     */
    private void deleteIspitniRokDependencies(Long ispitniRokId) {
        // 1. Dohvati sve Ispite za ovaj IspitniRok
        List<Ispit> ispiti = ispitRepository.findByIspitniRokId(ispitniRokId);
        
        // 2. Za svaki Ispit obrisi IzlazakNaIspit i PrijavaIspita
        for (Ispit ispit : ispiti) {
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findByIspitId(ispit.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
            
            List<PrijavaIspita> prijave = prijavaIspitaRepository.findAllByIspitId(ispit.getId());
            prijavaIspitaRepository.deleteAll(prijave);
        }
        
        // 3. Obrisi sve Ispite
        ispitRepository.deleteAll(ispiti);
    }

    private SkolskaGodinaResponse toResponse(SkolskaGodina skolskaGodina) {
        SkolskaGodinaResponse response = new SkolskaGodinaResponse();
        response.setId(skolskaGodina.getId());
        response.setNaziv(skolskaGodina.getNaziv());
        response.setAktivna(skolskaGodina.isAktivna());
        return response;
    }
}
