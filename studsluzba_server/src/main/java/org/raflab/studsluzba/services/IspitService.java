package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IspitService {
    private final IspitRepository ispitRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;
    private final DrziPredmetRepository drziPredmetRepository;
    private final PredmetRepository predmetRepository;
    private final IspitniRokRepository ispitniRokRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    public Long save(IspitRequest request) {
        Ispit ispit = converters.toIspit(request);
        DrziPredmet dp = drziPredmetRepository.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("DrziPredmet not found: " + request.getDrziPredmetId()));
        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("Predmet not found: " + request.getPredmetId()));
        IspitniRok rok = ispitniRokRepository.findById(request.getIspitniRokId())
                .orElseThrow(() -> new EntityNotFoundException("IspitniRok not found: " + request.getIspitniRokId()));
        SkolskaGodina godina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("SkolskaGodina not found: " + request.getSkolskaGodinaId()));

        ispit.setDrziPredmet(dp);
        ispit.setPredmet(predmet);
        ispit.setIspitniRok(rok);
        ispit.setSkolskaGodina(godina);
        Ispit saved = ispitRepository.save(ispit);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public IspitResponse getById(Long id) {
        Ispit ispit = ispitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispit sa ID " + id + " ne postoji"));
        return toIspitResponse(ispit);
    }

    @Transactional(readOnly = true)
    public List<IspitResponse> getAll() {
        List<Ispit> result = new ArrayList<>();
        ispitRepository.findAll().forEach(result::add);
        return result.stream()
                .map(this::toIspitResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public IspitResponse update(Long id, IspitRequest request) {
        Ispit existing = ispitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispit sa ID " + id + " ne postoji"));

        DrziPredmet dp = drziPredmetRepository.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("DrziPredmet not found: " + request.getDrziPredmetId()));
        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("Predmet not found: " + request.getPredmetId()));
        IspitniRok rok = ispitniRokRepository.findById(request.getIspitniRokId())
                .orElseThrow(() -> new EntityNotFoundException("IspitniRok not found: " + request.getIspitniRokId()));
        SkolskaGodina godina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new EntityNotFoundException("SkolskaGodina not found: " + request.getSkolskaGodinaId()));

        existing.setDatumOdrzavanja(request.getDatumOdrzavanja());
        existing.setVremePocetka(request.getVremePocetka());
        existing.setVremeZavrsetka(request.getVremeZavrsetka());
        existing.setDrziPredmet(dp);
        existing.setPredmet(predmet);
        existing.setIspitniRok(rok);
        existing.setSkolskaGodina(godina);

        Ispit updated = ispitRepository.save(existing);
        return toIspitResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!ispitRepository.existsById(id)) {
            throw new EntityNotFoundException("Ispit sa ID " + id + " ne postoji");
        }
        ispitRepository.deleteById(id);
    }

    private IspitResponse toIspitResponse(Ispit ispit) {
        IspitResponse response = new IspitResponse();
        response.setId(ispit.getId());
        response.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        response.setVremePocetka(ispit.getVremePocetka());
        response.setVremeZavrsetka(ispit.getVremeZavrsetka());
        response.setDrziPredmet(ispit.getDrziPredmet() != null ? converters.toDrziPredmetResponse(ispit.getDrziPredmet()) : null);
        response.setPredmet(ispit.getPredmet() != null ? entityMappers.fromPredmetToResponse(ispit.getPredmet()) : null);
        response.setIspitniRokId(ispit.getIspitniRok() != null ? ispit.getIspitniRok().getId() : null);
        response.setSkolskaGodinaId(ispit.getSkolskaGodina() != null ? ispit.getSkolskaGodina().getId() : null);
        response.setZakljucen(ispit.isZakljucen());
        return response;
    }
}
