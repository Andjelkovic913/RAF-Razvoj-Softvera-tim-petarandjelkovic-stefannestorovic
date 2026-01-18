package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredispitneObavezeRequest;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.model.PredispitneObaveze;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.repositories.PredispitneObavezeRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredispitneObavezeService {

    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    @Transactional
    public Long create(PredispitneObavezeRequest request) {
        SlusaPredmet slusaPredmet = slusaPredmetRepository.findById(request.getSlusaPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("SlusaPredmet sa ID " + request.getSlusaPredmetId() + " ne postoji"));
        
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));

        PredispitneObaveze obaveze = new PredispitneObaveze();
        obaveze.setVrsta(request.getVrsta());
        obaveze.setMaxBodova(request.getMaxBodova());
        obaveze.setOsvojeniBodovi(request.getOsvojeniBodovi() != null ? request.getOsvojeniBodovi() : 0);
        obaveze.setSlusaPredmet(slusaPredmet);
        obaveze.setSkolskaGodina(skolskaGodina);

        PredispitneObaveze saved = predispitneObavezeRepository.save(obaveze);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public PredispitneObavezeResponse getById(Long id) {
        PredispitneObaveze obaveze = predispitneObavezeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predispitne obaveze sa ID " + id + " ne postoje"));
        return toResponse(obaveze);
    }

    @Transactional(readOnly = true)
    public List<PredispitneObavezeResponse> getAll() {
        List<PredispitneObaveze> result = new ArrayList<>();
        predispitneObavezeRepository.findAll().forEach(result::add);
        return result.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PredispitneObavezeResponse update(Long id, PredispitneObavezeRequest request) {
        PredispitneObaveze existing = predispitneObavezeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predispitne obaveze sa ID " + id + " ne postoje"));

        SlusaPredmet slusaPredmet = slusaPredmetRepository.findById(request.getSlusaPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("SlusaPredmet sa ID " + request.getSlusaPredmetId() + " ne postoji"));
        
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));

        existing.setVrsta(request.getVrsta());
        existing.setMaxBodova(request.getMaxBodova());
        existing.setOsvojeniBodovi(request.getOsvojeniBodovi() != null ? request.getOsvojeniBodovi() : 0);
        existing.setSlusaPredmet(slusaPredmet);
        existing.setSkolskaGodina(skolskaGodina);

        PredispitneObaveze updated = predispitneObavezeRepository.save(existing);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!predispitneObavezeRepository.existsById(id)) {
            throw new EntityNotFoundException("Predispitne obaveze sa ID " + id + " ne postoje");
        }
        predispitneObavezeRepository.deleteById(id);
    }

    private PredispitneObavezeResponse toResponse(PredispitneObaveze obaveze) {
        PredispitneObavezeResponse response = new PredispitneObavezeResponse();
        response.setId(obaveze.getId());
        response.setVrsta(obaveze.getVrsta());
        response.setMaxBodova(obaveze.getMaxBodova());
        response.setOsvojeniBodovi(obaveze.getOsvojeniBodovi());
        response.setSlusaPredmetId(obaveze.getSlusaPredmet() != null ? obaveze.getSlusaPredmet().getId() : null);
        response.setSkolskaGodinaId(obaveze.getSkolskaGodina() != null ? obaveze.getSkolskaGodina().getId() : null);
        return response;
    }
}
