package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/drzipredmet")
public class DrziPredmetController {
    @Autowired
    private DrziPredmetService drziPredmetService;

    @GetMapping("/{id}")
    public ResponseEntity<DrziPredmetResponse> getById(@PathVariable Long id) {
        return drziPredmetService
                .findById(id)
                .map(Converters::toDrziPredmetResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
