package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.services.NastavnikZvanjeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/nastavnik-zvanje")
@RequiredArgsConstructor
public class NastavnikZvanjeController {

    private final NastavnikZvanjeService nastavnikZvanjeService;

    @PostMapping("/add")
    public Long addNewNastavnikZvanje(@RequestBody @Valid NastavnikZvanjeRequest request) {
        return nastavnikZvanjeService.createNastavnikZvanje(request);
    }

    @GetMapping("/{id}")
    public NastavnikZvanjeResponse getNastavnikZvanjeById(@PathVariable Long id) {
        return nastavnikZvanjeService.getNastavnikZvanjeById(id);
    }

    @GetMapping("/all")
    public List<NastavnikZvanjeResponse> getAllNastavnikZvanja() {
        return nastavnikZvanjeService.getAllNastavnikZvanja();
    }

    @GetMapping("/nastavnik/{nastavnikId}")
    public List<NastavnikZvanjeResponse> getZvanjaByNastavnikId(@PathVariable Long nastavnikId) {
        return nastavnikZvanjeService.getZvanjaByNastavnikId(nastavnikId);
    }

    @PutMapping("/{id}")
    public NastavnikZvanjeResponse updateNastavnikZvanje(@PathVariable Long id, @RequestBody @Valid NastavnikZvanjeRequest request) {
        return nastavnikZvanjeService.updateNastavnikZvanje(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteNastavnikZvanje(@PathVariable Long id) {
        nastavnikZvanjeService.deleteNastavnikZvanje(id);
    }
}
