package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.dtos.PrijavljeniStudentDTO;
import org.raflab.studsluzba.services.PrijavaIspitaService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/prijava-ispita")
@RequiredArgsConstructor
public class PrijavaIspitaController {

    private final PrijavaIspitaService prijavaIspitaService;

    // 3.3 prijava ispita za izabrani ispitni rok i predmet koji student sluša
    @PostMapping("/add")
    public Long addPrijavaIspita(@RequestBody @Valid PrijavaIspitaRequest request) {
        return prijavaIspitaService.create(request);
    }

    // 3.1 svi prijavljeni studenti za ispit
    @GetMapping("/{ispitId}/students")
    public List<PrijavljeniStudentDTO> getPrijavljeniStudenti(@PathVariable Long ispitId) {
        return prijavaIspitaService.getPrijavljeniStudenti(ispitId);
    }
}

