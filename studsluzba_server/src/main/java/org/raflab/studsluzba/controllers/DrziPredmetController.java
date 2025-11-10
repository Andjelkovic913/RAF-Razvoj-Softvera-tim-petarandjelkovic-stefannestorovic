package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/drzipredmet")
@RequiredArgsConstructor
public class DrziPredmetController {
    
    private final DrziPredmetService drziPredmetService;

    @GetMapping("/{id}")
    public DrziPredmetResponse getById(@PathVariable Long id) {
        return drziPredmetService.findById(id);
    }
}
