package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NastavnikZvanjeService {

    private final NastavnikZvanjeRepository nastavnikZvanjeRepository;
    private final NastavnikRepository nastavnikRepository;

    @Transactional
    public Long createNastavnikZvanje(NastavnikZvanjeRequest request) {
        NastavnikZvanje zvanje = new NastavnikZvanje();
        
        Nastavnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + request.getNastavnikId() + " ne postoji"));
        
        zvanje.setNastavnik(nastavnik);
        zvanje.setDatumIzbora(request.getDatumIzbora());
        zvanje.setNaucnaOblast(request.getNaucnaOblast());
        zvanje.setUzaNaucnaOblast(request.getUzaNaucnaOblast());
        zvanje.setZvanje(request.getZvanje());
        zvanje.setAktivno(request.isAktivno());
        
        NastavnikZvanje saved = nastavnikZvanjeRepository.save(zvanje);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public NastavnikZvanjeResponse getNastavnikZvanjeById(Long id) {
        NastavnikZvanje zvanje = nastavnikZvanjeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik zvanje sa ID " + id + " ne postoji"));
        return toResponse(zvanje);
    }

    @Transactional(readOnly = true)
    public List<NastavnikZvanjeResponse> getAllNastavnikZvanja() {
        return nastavnikZvanjeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NastavnikZvanjeResponse> getZvanjaByNastavnikId(Long nastavnikId) {
        return nastavnikZvanjeRepository.findAll().stream()
                .filter(z -> z.getNastavnik() != null && z.getNastavnik().getId().equals(nastavnikId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NastavnikZvanjeResponse updateNastavnikZvanje(Long id, NastavnikZvanjeRequest request) {
        NastavnikZvanje zvanje = nastavnikZvanjeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik zvanje sa ID " + id + " ne postoji"));
        
        Nastavnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + request.getNastavnikId() + " ne postoji"));
        
        zvanje.setNastavnik(nastavnik);
        zvanje.setDatumIzbora(request.getDatumIzbora());
        zvanje.setNaucnaOblast(request.getNaucnaOblast());
        zvanje.setUzaNaucnaOblast(request.getUzaNaucnaOblast());
        zvanje.setZvanje(request.getZvanje());
        zvanje.setAktivno(request.isAktivno());
        
        NastavnikZvanje updated = nastavnikZvanjeRepository.save(zvanje);
        return toResponse(updated);
    }

    @Transactional
    public void deleteNastavnikZvanje(Long id) {
        if (!nastavnikZvanjeRepository.existsById(id)) {
            throw new EntityNotFoundException("Nastavnik zvanje sa ID " + id + " ne postoji");
        }
        nastavnikZvanjeRepository.deleteById(id);
    }

    private NastavnikZvanjeResponse toResponse(NastavnikZvanje zvanje) {
        NastavnikZvanjeResponse response = new NastavnikZvanjeResponse();
        response.setId(zvanje.getId());
        response.setDatumIzbora(zvanje.getDatumIzbora());
        response.setNaucnaOblast(zvanje.getNaucnaOblast());
        response.setUzaNaucnaOblast(zvanje.getUzaNaucnaOblast());
        response.setZvanje(zvanje.getZvanje());
        response.setAktivno(zvanje.isAktivno());
        return response;
    }
}
