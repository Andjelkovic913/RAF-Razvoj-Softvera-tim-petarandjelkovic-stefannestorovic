package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PredmetService {

    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;

    public Predmet create(PredmetRequest req) {
        if (predmetRepository.existsBySifra(req.getSifra())) {
            throw new IllegalArgumentException("Šifra predmeta već postoji");
        }
        StudijskiProgram sp = studijskiProgramRepository.findById(req.getStudProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        Predmet p = Converters.toPredmet(req);
        p.setStudProgram(sp);
        return predmetRepository.save(p);
    }

    public List<Predmet> getByStudProgram(Long studProgramId, Boolean obavezan) {
        StudijskiProgram sp = studijskiProgramRepository.findById(studProgramId)
                .orElseThrow(() -> new EntityNotFoundException("Studijski program ne postoji"));

        if (obavezan == null) {
            List<Predmet> svi = new ArrayList<>();
            svi.addAll(predmetRepository.getPredmetsByStudProgramAndObavezan(sp, true));
            svi.addAll(predmetRepository.getPredmetsByStudProgramAndObavezan(sp, false));
            return svi;
        }

        return predmetRepository.getPredmetsByStudProgramAndObavezan(sp, obavezan);
    }
    public List<Predmet> getAll() {
        List<Predmet> result = new ArrayList<>();
        predmetRepository.findAll().forEach(result::add);
        return result;
    }
}
