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

    // 2.3 -dodavanje novog predmeta na studijski program (šifra predmeta mora biti jedinstvena)
    @PostMapping("/add")
    public Long addNewPredmet(@RequestBody @Valid PredmetRequest req) { 
        return predmetService.create(req).getId();
    }

    // 2.1 - spisak svih predmeta na studijskom programu (opciono filtriranje po obaveznim predmetima)
    @GetMapping(path = "/by-program/{programId}")
    public List<PredmetResponse> getPredmetiByProgram(
            @PathVariable Long programId,
            @RequestParam(required = false) Boolean obavezan) { // spiak svih predmeta na stuijskom programu
        return predmetService.getByStudProgram(programId, obavezan);
    }

    // 2.2 - spisak svih predmeta sa svim podacima o predmetu
    @GetMapping(path = "/svi")
    public List<PredmetResponse> getAllPredmeti(){// spisak svih predmeta sa svim podacima o predmetu
        return predmetService.getAll();
    }

    // 2.4 - selekcija predmeta preko ID-a
    @GetMapping(path = "/{id}")
    public PredmetResponse getPredmetById(@PathVariable Long id) {
        return predmetService.getById(id);
    }

    // ažuriranje podataka o predmetu
    @PutMapping(path = "/{id}")
    public PredmetResponse updatePredmet(@PathVariable Long id, @RequestBody @Valid PredmetRequest req) {
        return predmetService.update(id, req);
    }

    // brisanje predmeta
    @DeleteMapping(path = "/{id}")
    public void deletePredmet(@PathVariable Long id) {
        predmetService.delete(id);
    }
}
