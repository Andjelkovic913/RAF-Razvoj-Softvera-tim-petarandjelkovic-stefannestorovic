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
            throw new IllegalArgumentException("Šifra predmeta već postoji");
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

        List<Predmet> predmets;
        if (obavezan == null) {
            List<Predmet> svi = new ArrayList<>();
            svi.addAll(predmetRepository.getPredmetsByStudProgramAndObavezan(sp, true));
            svi.addAll(predmetRepository.getPredmetsByStudProgramAndObavezan(sp, false));
            predmets = svi;
        } else {
            predmets = predmetRepository.getPredmetsByStudProgramAndObavezan(sp, obavezan);
        }
        
        return predmets.stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PredmetResponse> getAll() {
        List<Predmet> result = new ArrayList<>();
        predmetRepository.findAll().forEach(result::add);
        return result.stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }
}
