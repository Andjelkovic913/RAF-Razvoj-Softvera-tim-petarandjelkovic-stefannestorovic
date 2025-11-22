package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkolskaGodinaService {

    private final SkolskaGodinaRepository skolskaGodinaRepository;

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

    @Transactional
    public void delete(Long id) {
        if (!skolskaGodinaRepository.existsById(id)) {
            throw new EntityNotFoundException("Školska godina sa ID " + id + " ne postoji");
        }
        skolskaGodinaRepository.deleteById(id);
    }

    private SkolskaGodinaResponse toResponse(SkolskaGodina skolskaGodina) {
        SkolskaGodinaResponse response = new SkolskaGodinaResponse();
        response.setId(skolskaGodina.getId());
        response.setNaziv(skolskaGodina.getNaziv());
        response.setAktivna(skolskaGodina.isAktivna());
        return response;
    }
}
