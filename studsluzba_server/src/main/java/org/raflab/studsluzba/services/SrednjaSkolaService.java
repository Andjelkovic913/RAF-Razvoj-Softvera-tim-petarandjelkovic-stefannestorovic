package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.model.SrednjaSkola;
import org.raflab.studsluzba.repositories.SrednjaSkolaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SrednjaSkolaService {

    private final SrednjaSkolaRepository srednjaSkolaRepository;
    private final EntityMappers entityMappers;

    /**
     * Kreira novu srednju školu.
     */
    @Transactional
    public Long createSrednjaSkola(SrednjaSkolaRequest request) {
        SrednjaSkola ss = new SrednjaSkola();
        ss.setNaziv(request.getNaziv());
        ss.setVrsta(SrednjaSkola.VrstaSkole.valueOf(request.getVrsta().toUpperCase()));
        ss.setMesto(request.getMesto());
        ss.setNapomena(request.getNapomena());
        
        SrednjaSkola saved = srednjaSkolaRepository.save(ss);
        return saved.getId();
    }

    /**
     * Pronalazi srednju školu po ID-u.
     */
    @Transactional(readOnly = true)
    public SrednjaSkolaResponse getSrednjaSkolaById(Long id) {
        SrednjaSkola ss = srednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Srednja škola sa ID " + id + " ne postoji"));
        return entityMappers.fromSrednjaSkolaToResponse(ss);
    }

    /**
     * Pronalazi sve srednje škole.
     */
    @Transactional(readOnly = true)
    public List<SrednjaSkolaResponse> getAllSrednjeSkole() {
        List<SrednjaSkola> skole = StreamSupport.stream(
                srednjaSkolaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return entityMappers.fromSrednjaSkolaListToResponse(skole);
    }

    /**
     * Ažurira srednju školu.
     */
    @Transactional
    public SrednjaSkolaResponse updateSrednjaSkola(Long id, SrednjaSkolaRequest request) {
        SrednjaSkola ss = srednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Srednja škola sa ID " + id + " ne postoji"));
        
        ss.setNaziv(request.getNaziv());
        ss.setVrsta(SrednjaSkola.VrstaSkole.valueOf(request.getVrsta().toUpperCase()));
        ss.setMesto(request.getMesto());
        ss.setNapomena(request.getNapomena());
        
        SrednjaSkola updated = srednjaSkolaRepository.save(ss);
        return entityMappers.fromSrednjaSkolaToResponse(updated);
    }

    /**
     * Briše srednju školu.
     */
    @Transactional
    public void deleteSrednjaSkola(Long id) {
        if (!srednjaSkolaRepository.existsById(id)) {
            throw new EntityNotFoundException("Srednja škola sa ID " + id + " ne postoji");
        }
        srednjaSkolaRepository.deleteById(id);
    }
}
