package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.services.IspitService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/ispit")
@RequiredArgsConstructor
public class IspitController {
    private final IspitService ispitService;

    @PostMapping("/add")
    public Long addNewIspit(@RequestBody @Valid IspitRequest ispitRequest) {
        Ispit ispit = ispitService.save(ispitRequest);
        return ispit.getId();
    }

}
