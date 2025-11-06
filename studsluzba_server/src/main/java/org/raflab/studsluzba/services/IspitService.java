package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IspitService {
    @Autowired
    private IspitRepository ispitRepository;

    public Ispit save(Ispit ispit) {
        return ispitRepository.save(ispit);
    }
}
