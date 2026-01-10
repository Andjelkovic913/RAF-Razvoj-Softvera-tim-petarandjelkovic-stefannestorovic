package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.VrstaStudijaRequest;
import org.raflab.studsluzba.controllers.response.VrstaStudijaResponse;
import org.raflab.studsluzba.model.VrstaStudija;
import org.raflab.studsluzba.repositories.VrstaStudijaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VrstaStudijaService {

    private final VrstaStudijaRepository vrstaStudijaRepository;

    @Transactional
    public Long createVrstaStudija(VrstaStudijaRequest request) {
        VrstaStudija vrstaStudija = new VrstaStudija();
        vrstaStudija.setSkraceniNaziv(request.getSkraceniNaziv());
        vrstaStudija.setPunNaziv(request.getPunNaziv());
        
        VrstaStudija saved = vrstaStudijaRepository.save(vrstaStudija);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public VrstaStudijaResponse getVrstaStudijaById(Long id) {
        VrstaStudija vrstaStudija = vrstaStudijaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vrsta studija sa ID " + id + " ne postoji"));
        return toResponse(vrstaStudija);
    }

    @Transactional(readOnly = true)
    public List<VrstaStudijaResponse> getAllVrsteStudija() {
        return vrstaStudijaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public VrstaStudijaResponse updateVrstaStudija(Long id, VrstaStudijaRequest request) {
        VrstaStudija vrstaStudija = vrstaStudijaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vrsta studija sa ID " + id + " ne postoji"));
        
        vrstaStudija.setSkraceniNaziv(request.getSkraceniNaziv());
        vrstaStudija.setPunNaziv(request.getPunNaziv());
        
        VrstaStudija updated = vrstaStudijaRepository.save(vrstaStudija);
        return toResponse(updated);
    }

    @Transactional
    public void deleteVrstaStudija(Long id) {
        if (!vrstaStudijaRepository.existsById(id)) {
            throw new EntityNotFoundException("Vrsta studija sa ID " + id + " ne postoji");
        }
        vrstaStudijaRepository.deleteById(id);
    }

    private VrstaStudijaResponse toResponse(VrstaStudija vrstaStudija) {
        VrstaStudijaResponse response = new VrstaStudijaResponse();
        response.setId(vrstaStudija.getId());
        response.setSkraceniNaziv(vrstaStudija.getSkraceniNaziv());
        response.setPunNaziv(vrstaStudija.getPunNaziv());
        return response;
    }
}
