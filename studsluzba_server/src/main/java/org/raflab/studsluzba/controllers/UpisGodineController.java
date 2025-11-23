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

    // 1.5 - upis studenta na godinu (dodati novi upis za studenta i predmete koje sluša, a koji će inicijalno biti nepoloženi)
    @PostMapping(path = "/add")
    public Long createUpisGodine(@RequestBody @Valid UpisGodineRequest request) {
        return upisGodineService.createUpisGodine(request);
    }

    // 1.4 - pregled svih upisanih godina za broj indeksa
    @GetMapping(path = "/student/{brojIndeksa}")
    public List<UpisGodineSimpleResponse> getUpisGodineByBrojIndeksa(@PathVariable int brojIndeksa) {
        return upisGodineService.getUpisGodineByBrojIndeksa(brojIndeksa);
    }

    @GetMapping(path = "/{upisGodineId}/predmeti")
    public List<SlusaPredmetResponse> getPredmetiZaUpisGodine(@PathVariable Long upisGodineId) {
        return upisGodineService.getPredmetiZaUpisGodine(upisGodineId);
    }
}

