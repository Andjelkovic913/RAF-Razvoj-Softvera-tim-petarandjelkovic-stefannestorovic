package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredmetService {

    private final PredmetRepository predmetRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public PredmetResponse create(PredmetRequest req) {
        if (predmetRepository.existsBySifra(req.getSifra())) {
            throw new IllegalArgumentException("Sifra predmeta vec postoji");
        }
        StudijskiProgram sp = studijskiProgramRepository.findById(req.getStudProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        Predmet p = converters.toPredmet(req);
        p.setStudProgram(sp);
        Predmet saved = predmetRepository.save(p);
        return entityMappers.fromPredmetToResponse(saved);
    }

    public List<PredmetResponse> getByStudProgram(Long studProgramId, Boolean obavezan) {
        StudijskiProgram sp = studijskiProgramRepository.findById(studProgramId)
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        List<Predmet> predmets = obavezan == null ? predmetRepository.findByStudProgram(sp) : predmetRepository.getPredmetsByStudProgramAndObavezan(sp, obavezan);
        
        return predmets.stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PredmetResponse> getAll() {
        List<Predmet> predmets = new ArrayList<>();
        predmetRepository.findAll().forEach(predmets::add);
        return predmets.stream().map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }

    public PredmetResponse getById(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + id + " ne postoji"));
        return entityMappers.fromPredmetToResponse(predmet);
    }

    public PredmetResponse update(Long id, PredmetRequest req) {
        Predmet existing = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + id + " ne postoji"));

        if (!existing.getSifra().equals(req.getSifra()) && predmetRepository.existsBySifra(req.getSifra())) {
            throw new IllegalArgumentException("Sifra predmeta vec postoji");
        }

        StudijskiProgram sp = studijskiProgramRepository.findById(req.getStudProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        existing.setSifra(req.getSifra());
        existing.setNaziv(req.getNaziv());
        existing.setOpis(req.getOpis());
        existing.setEspb(req.getEspb());
        existing.setSemestar(req.getSemestar());
        existing.setFondCasovaVezbe(req.getFondCasovaVezbe());
        existing.setFondCasovaPredavanja(req.getFondCasovaPredavanja());
        existing.setObavezan(req.isObavezan());
        existing.setStudProgram(sp);

        Predmet updated = predmetRepository.save(existing);
        return entityMappers.fromPredmetToResponse(updated);
    }

    public void delete(Long id) {
        if (!predmetRepository.existsById(id)) {
            throw new EntityNotFoundException("Predmet sa ID " + id + " ne postoji");
        }
        predmetRepository.deleteById(id);
    }
}
