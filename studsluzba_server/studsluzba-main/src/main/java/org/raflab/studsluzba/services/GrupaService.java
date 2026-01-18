package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.GrupaRequest;
import org.raflab.studsluzba.controllers.response.GrupaResponse;
import org.raflab.studsluzba.model.Grupa;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.GrupaRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupaService {

    private final GrupaRepository grupaRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final PredmetRepository predmetRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final EntityMappers entityMappers;

    @Transactional
    public Long createGrupa(GrupaRequest request) {
        Grupa grupa = new Grupa();
        
        StudijskiProgram sp = studijskiProgramRepository.findById(request.getStudijskiProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program sa ID " + request.getStudijskiProgramId() + " ne postoji"));
        grupa.setStudijskiProgram(sp);
        
        SkolskaGodina sg = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));
        grupa.setSkolskaGodina(sg);
        
        if (request.getPredmetIds() != null && !request.getPredmetIds().isEmpty()) {
            List<Predmet> predmeti = new ArrayList<>();
            predmetRepository.findAllById(request.getPredmetIds()).forEach(predmeti::add);
            grupa.setPredmeti(predmeti);
        }
        
        Grupa saved = grupaRepository.save(grupa);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public GrupaResponse getGrupaById(Long id) {
        Grupa grupa = grupaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupa sa ID " + id + " ne postoji"));
        return toResponse(grupa);
    }

    @Transactional(readOnly = true)
    public List<GrupaResponse> getAllGrupe() {
        return grupaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GrupaResponse updateGrupa(Long id, GrupaRequest request) {
        Grupa grupa = grupaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupa sa ID " + id + " ne postoji"));
        
        StudijskiProgram sp = studijskiProgramRepository.findById(request.getStudijskiProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program sa ID " + request.getStudijskiProgramId() + " ne postoji"));
        grupa.setStudijskiProgram(sp);
        
        SkolskaGodina sg = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));
        grupa.setSkolskaGodina(sg);
        
        if (request.getPredmetIds() != null) {
            List<Predmet> predmeti = new ArrayList<>();
            predmetRepository.findAllById(request.getPredmetIds()).forEach(predmeti::add);
            grupa.setPredmeti(predmeti);
        }
        
        Grupa updated = grupaRepository.save(grupa);
        return toResponse(updated);
    }

    @Transactional
    public void deleteGrupa(Long id) {
        if (!grupaRepository.existsById(id)) {
            throw new EntityNotFoundException("Grupa sa ID " + id + " ne postoji");
        }
        grupaRepository.deleteById(id);
    }

    private GrupaResponse toResponse(Grupa grupa) {
        GrupaResponse response = new GrupaResponse();
        response.setId(grupa.getId());
        response.setStudijskiProgram(entityMappers.fromStudijskiProgramToBasicDTO(grupa.getStudijskiProgram()));
        response.setPredmeti(entityMappers.fromPredmetiToDTO(grupa.getPredmeti()));
        response.setSkolskaGodina(entityMappers.fromSkolskaGodinaToDTO(grupa.getSkolskaGodina()));
        return response;
    }
}
