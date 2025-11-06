package org.raflab.studsluzba.controllers;

import javassist.NotFoundException;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/predmet")
public class PredmetController {

    @Autowired
    private PredmetService predmetService;

    @PostMapping(path = "/add")
    public Long addNewPredmet(@RequestBody @Valid PredmetRequest predmetRequest) {
        Predmet predmet = predmetService.save(Converters.toPredmet(predmetRequest));
        return predmet.getId();
    }
}
