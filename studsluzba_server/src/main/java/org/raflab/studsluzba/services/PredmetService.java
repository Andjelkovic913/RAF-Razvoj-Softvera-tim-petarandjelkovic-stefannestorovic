package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
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

    public Predmet save(Predmet predmet) {
        return predmetRepository.save(predmet);
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

}
