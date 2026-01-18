package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.services.SrednjaSkolaService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/srednja-skola")
@RequiredArgsConstructor
public class SrednjaSkolaController {

    private final SrednjaSkolaService srednjaSkolaService;

    @PostMapping("/add")
    public SrednjaSkolaResponse addNewSrednjaSkola(@RequestBody @Valid SrednjaSkolaRequest request) {
        return srednjaSkolaService.createSrednjaSkolaAndReturn(request);
    }

    @GetMapping("/{id}")
    public SrednjaSkolaResponse getSrednjaSkolaById(@PathVariable Long id) {
        return srednjaSkolaService.getSrednjaSkolaById(id);
    }

    @GetMapping("/all")
    public List<SrednjaSkolaResponse> getAllSrednjeSkole() {
        return srednjaSkolaService.getAllSrednjeSkole();
    }

    @PutMapping("/{id}")
    public SrednjaSkolaResponse updateSrednjaSkola(@PathVariable Long id, @RequestBody @Valid SrednjaSkolaRequest request) {
        return srednjaSkolaService.updateSrednjaSkola(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteSrednjaSkola(@PathVariable Long id) {
        srednjaSkolaService.deleteSrednjaSkola(id);
    }
}
