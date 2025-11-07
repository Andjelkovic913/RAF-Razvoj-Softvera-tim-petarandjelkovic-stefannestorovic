package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/predmet")
public class PredmetController {

    @Autowired
    private PredmetService predmetService;
    @Autowired
    private EntityMappers entityMappers;

    @PostMapping("/add")
    public Long addNewPredmet(@RequestBody @Valid PredmetRequest req) {
        return predmetService.create(req).getId();
    }

    @GetMapping(path = "/by-program/{programId}")
    public List<Predmet> getPredmetiByProgram(
            @PathVariable Long programId,
            @RequestParam(required = false) Boolean obavezan
    ) {
        return predmetService.getByStudProgram(programId, obavezan);
    }

    @GetMapping(path = "/all/{programId}")
    public List<PredmetDTO> getAllPredmeti(
            @PathVariable Long programId,
            @RequestParam(required = false ) Boolean obavezan
    ) {
        return entityMappers.fromPredmetiToDTO(predmetService.getByStudProgram(programId, obavezan));
    }

    @GetMapping(path = "/svi")
    public List<PredmetResponse> getAllPredmeti(){
        return predmetService.getAll().stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }
}
