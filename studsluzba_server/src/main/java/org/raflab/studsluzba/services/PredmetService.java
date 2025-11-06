package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredmetService {

    @Autowired
    private PredmetRepository predmetRepository;

    public Predmet save(Predmet predmet) {
        return predmetRepository.save(predmet);
    }

}
