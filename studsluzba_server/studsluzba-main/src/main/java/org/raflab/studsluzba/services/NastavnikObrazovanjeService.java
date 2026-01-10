package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikObrazovanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikObrazovanjeResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikObrazovanje;
import org.raflab.studsluzba.repositories.NastavnikObrazovanjeRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class NastavnikObrazovanjeService {

    private final NastavnikObrazovanjeRepository nastavnikObrazovanjeRepository;
    private final NastavnikRepository nastavnikRepository;

    @Transactional
    public Long createNastavnikObrazovanje(NastavnikObrazovanjeRequest request) {
        NastavnikObrazovanje obrazovanje = new NastavnikObrazovanje();
        
        Nastavnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + request.getNastavnikId() + " ne postoji"));
        
        obrazovanje.setNastavnik(nastavnik);
        obrazovanje.setUstanova(request.getUstanova());
        obrazovanje.setStepenStudija(request.getStepenStudija());
        obrazovanje.setGodinaZavrsetka(request.getGodinaZavrsetka());
        obrazovanje.setZvanje(request.getZvanje());
        
        NastavnikObrazovanje saved = nastavnikObrazovanjeRepository.save(obrazovanje);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public NastavnikObrazovanjeResponse getNastavnikObrazovanjeById(Long id) {
        NastavnikObrazovanje obrazovanje = nastavnikObrazovanjeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik obrazovanje sa ID " + id + " ne postoji"));
        return toResponse(obrazovanje);
    }

    @Transactional(readOnly = true)
    public List<NastavnikObrazovanjeResponse> getAllNastavnikObrazovanja() {
        return StreamSupport.stream(nastavnikObrazovanjeRepository.findAll().spliterator(), false)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NastavnikObrazovanjeResponse> getObrazovanjaByNastavnikId(Long nastavnikId) {
        return StreamSupport.stream(nastavnikObrazovanjeRepository.findAll().spliterator(), false)
                .filter(o -> o.getNastavnik() != null && o.getNastavnik().getId().equals(nastavnikId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NastavnikObrazovanjeResponse updateNastavnikObrazovanje(Long id, NastavnikObrazovanjeRequest request) {
        NastavnikObrazovanje obrazovanje = nastavnikObrazovanjeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik obrazovanje sa ID " + id + " ne postoji"));
        
        Nastavnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + request.getNastavnikId() + " ne postoji"));
        
        obrazovanje.setNastavnik(nastavnik);
        obrazovanje.setUstanova(request.getUstanova());
        obrazovanje.setStepenStudija(request.getStepenStudija());
        obrazovanje.setGodinaZavrsetka(request.getGodinaZavrsetka());
        obrazovanje.setZvanje(request.getZvanje());
        
        NastavnikObrazovanje updated = nastavnikObrazovanjeRepository.save(obrazovanje);
        return toResponse(updated);
    }

    @Transactional
    public void deleteNastavnikObrazovanje(Long id) {
        if (!nastavnikObrazovanjeRepository.existsById(id)) {
            throw new EntityNotFoundException("Nastavnik obrazovanje sa ID " + id + " ne postoji");
        }
        nastavnikObrazovanjeRepository.deleteById(id);
    }

    private NastavnikObrazovanjeResponse toResponse(NastavnikObrazovanje obrazovanje) {
        NastavnikObrazovanjeResponse response = new NastavnikObrazovanjeResponse();
        response.setId(obrazovanje.getId());
        response.setUstanova(obrazovanje.getUstanova());
        response.setStepenStudija(obrazovanje.getStepenStudija());
        response.setGodinaZavrsetka(obrazovanje.getGodinaZavrsetka());
        response.setZvanje(obrazovanje.getZvanje());
        
        if (obrazovanje.getNastavnik() != null) {
            response.setNastavnikId(obrazovanje.getNastavnik().getId());
            response.setNastavnikIme(obrazovanje.getNastavnik().getIme());
            response.setNastavnikPrezime(obrazovanje.getNastavnik().getPrezime());
        }
        
        return response;
    }
}
