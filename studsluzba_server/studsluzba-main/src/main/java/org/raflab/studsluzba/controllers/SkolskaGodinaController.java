package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/skolska-godina")
@RequiredArgsConstructor
public class SkolskaGodinaController {

    private final SkolskaGodinaService skolskaGodinaService;

    @PostMapping("/add")
    public Long addSkolskaGodina(@RequestBody @Valid SkolskaGodinaRequest request) {
        return skolskaGodinaService.create(request);
    }

    @GetMapping("/{id}")
    public SkolskaGodinaResponse getSkolskaGodinaById(@PathVariable Long id) {
        return skolskaGodinaService.getById(id);
    }

    @GetMapping("/all")
    public List<SkolskaGodinaResponse> getAllSkolskeGodine() {
        return skolskaGodinaService.getAll();
    }

    @GetMapping("/aktivna")
    public SkolskaGodinaResponse getAktivnaSkolskaGodina() {
        return skolskaGodinaService.getAktivna();
    }

    @PutMapping("/{id}")
    public SkolskaGodinaResponse updateSkolskaGodina(@PathVariable Long id, @RequestBody @Valid SkolskaGodinaRequest request) {
        return skolskaGodinaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteSkolskaGodina(@PathVariable Long id) {
        skolskaGodinaService.delete(id);
    }
}
