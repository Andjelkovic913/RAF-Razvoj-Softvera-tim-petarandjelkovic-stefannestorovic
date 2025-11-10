package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/predmet")
@RequiredArgsConstructor
public class PredmetController {

    private final PredmetService predmetService;
    private final EntityMappers entityMappers;

    @PostMapping("/add")
    public Long addNewPredmet(@RequestBody @Valid PredmetRequest req) {
        return predmetService.create(req).getId();
    }

    @GetMapping(path = "/by-program/{programId}")
    public List<PredmetResponse> getPredmetiByProgram(
            @PathVariable Long programId,
            @RequestParam(required = false) Boolean obavezan
    ) {
        return predmetService.getByStudProgram(programId, obavezan).stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
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
