package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.response.UpisGodineSimpleResponse;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.services.UpisGodineService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/upis-godine")
public class UpisGodineController {

    private final UpisGodineService upisGodineService;

    @PostMapping(path = "/add")
    public Long createUpisGodine(@RequestBody @Valid UpisGodineRequest request) {
        return upisGodineService.createUpisGodine(request);
    }

    @GetMapping(path = "/student/{brojIndeksa}")
    public List<UpisGodineSimpleResponse> getUpisGodineByBrojIndeksa(@PathVariable int brojIndeksa) {
        return upisGodineService.getUpisGodineByBrojIndeksa(brojIndeksa);
    }

    @GetMapping(path = "/{upisGodineId}/predmeti")
    public List<SlusaPredmetResponse> getPredmetiZaUpisGodine(@PathVariable Long upisGodineId) {
        return upisGodineService.getPredmetiZaUpisGodine(upisGodineId);
    }
}

