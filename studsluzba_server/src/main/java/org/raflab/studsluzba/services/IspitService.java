package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitRequest;
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
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class IspitService {
    private final IspitRepository ispitRepository;
    private final Converters converters;
    private final DrziPredmetRepository drziPredmetRepository;
    private final PredmetRepository predmetRepository;
    private final IspitniRokRepository ispitniRokRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    public Ispit save(IspitRequest request) {
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
        return ispitRepository.save(ispit);
    }
}
