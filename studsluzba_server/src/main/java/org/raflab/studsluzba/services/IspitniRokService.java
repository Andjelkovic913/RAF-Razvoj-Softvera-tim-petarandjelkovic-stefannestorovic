package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IspitniRokService {

    private final IspitniRokRepository ispitniRokRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    @Transactional
    public Long create(IspitniRokRequest request) {
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));

        IspitniRok ispitniRok = new IspitniRok();
        ispitniRok.setDatumPocetka(request.getDatumPocetka());
        ispitniRok.setDatumZavrsetka(request.getDatumZavrsetka());
        ispitniRok.setTipRoka(request.getTipRoka());
        ispitniRok.setSkolskaGodina(skolskaGodina);

        IspitniRok saved = ispitniRokRepository.save(ispitniRok);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public IspitniRokResponse getById(Long id) {
        IspitniRok ispitniRok = ispitniRokRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispitni rok sa ID " + id + " ne postoji"));
        return toResponse(ispitniRok);
    }

    @Transactional(readOnly = true)
    public List<IspitniRokResponse> getAll() {
        List<IspitniRok> result = new ArrayList<>();
        ispitniRokRepository.findAll().forEach(result::add);
        return result.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public IspitniRokResponse update(Long id, IspitniRokRequest request) {
        IspitniRok existing = ispitniRokRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispitni rok sa ID " + id + " ne postoji"));

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("Školska godina sa ID " + request.getSkolskaGodinaId() + " ne postoji"));

        existing.setDatumPocetka(request.getDatumPocetka());
        existing.setDatumZavrsetka(request.getDatumZavrsetka());
        existing.setTipRoka(request.getTipRoka());
        existing.setSkolskaGodina(skolskaGodina);

        IspitniRok updated = ispitniRokRepository.save(existing);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!ispitniRokRepository.existsById(id)) {
            throw new EntityNotFoundException("Ispitni rok sa ID " + id + " ne postoji");
        }
        ispitniRokRepository.deleteById(id);
    }

    private IspitniRokResponse toResponse(IspitniRok ispitniRok) {
        IspitniRokResponse response = new IspitniRokResponse();
        response.setId(ispitniRok.getId());
        response.setDatumPocetka(ispitniRok.getDatumPocetka());
        response.setDatumZavrsetka(ispitniRok.getDatumZavrsetka());
        response.setTipRoka(ispitniRok.getTipRoka());
        response.setSkolskaGodinaId(ispitniRok.getSkolskaGodina() != null ? ispitniRok.getSkolskaGodina().getId() : null);
        return response;
    }
}
