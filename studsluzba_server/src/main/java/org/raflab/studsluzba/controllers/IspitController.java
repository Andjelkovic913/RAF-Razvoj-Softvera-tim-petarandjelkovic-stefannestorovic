package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.services.IspitService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/ispit")
public class IspitController {
    @Autowired
    private IspitService ispitService;

    @PostMapping("/add")
    public Long addNewIspit(@RequestBody @Valid IspitRequest ispitRequest) {
        Ispit ispit = ispitService.save(Converters.toIspit(ispitRequest));
        return ispit.getId();
    }

}
