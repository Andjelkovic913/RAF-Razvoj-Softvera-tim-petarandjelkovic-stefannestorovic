package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetAddRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/drzipredmet")
@RequiredArgsConstructor
public class DrziPredmetController {
    
    private final DrziPredmetService drziPredmetService;

    @PostMapping("/add")
    public Long addDrziPredmet(@RequestBody @Valid DrziPredmetAddRequest request) {
        return drziPredmetService.create(request);
    }

    @GetMapping("/{id}")
    public DrziPredmetResponse getById(@PathVariable Long id) {
        return drziPredmetService.findById(id);
    }

    @GetMapping("/all")
    public List<DrziPredmetResponse> getAll() {
        return drziPredmetService.getAll();
    }

    @PutMapping("/{id}")
    public DrziPredmetResponse updateDrziPredmet(@PathVariable Long id, @RequestBody @Valid DrziPredmetAddRequest request) {
        return drziPredmetService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteDrziPredmet(@PathVariable Long id) {
        drziPredmetService.delete(id);
    }
}
