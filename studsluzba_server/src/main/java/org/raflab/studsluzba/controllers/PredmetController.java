package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.services.PredmetService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/predmet")
@RequiredArgsConstructor
public class PredmetController {

    private final PredmetService predmetService;

    @PostMapping("/add")
    public Long addNewPredmet(@RequestBody @Valid PredmetRequest req) {
        return predmetService.create(req).getId();
    }

    @GetMapping(path = "/by-program/{programId}")
    public List<PredmetResponse> getPredmetiByProgram(
            @PathVariable Long programId,
            @RequestParam(required = false) Boolean obavezan
    ) {
        return predmetService.getByStudProgram(programId, obavezan);
    }

    @GetMapping(path = "/all/{programId}")
    public List<PredmetResponse> getAllPredmeti(
            @PathVariable Long programId,
            @RequestParam(required = false ) Boolean obavezan
    ) {
        return predmetService.getByStudProgram(programId, obavezan);
    }

    @GetMapping(path = "/svi")
    public List<PredmetResponse> getAllPredmeti(){
        return predmetService.getAll();
    }

    @GetMapping(path = "/{id}")
    public PredmetResponse getPredmetById(@PathVariable Long id) {
        return predmetService.getById(id);
    }

    @PutMapping(path = "/{id}")
    public PredmetResponse updatePredmet(@PathVariable Long id, @RequestBody @Valid PredmetRequest req) {
        return predmetService.update(id, req);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePredmet(@PathVariable Long id) {
        predmetService.delete(id);
    }
}
