package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikObrazovanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikObrazovanjeResponse;
import org.raflab.studsluzba.services.NastavnikObrazovanjeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/nastavnik-obrazovanje")
@RequiredArgsConstructor
public class NastavnikObrazovanjeController {

    private final NastavnikObrazovanjeService nastavnikObrazovanjeService;

    @PostMapping("/add")
    public Long addNewNastavnikObrazovanje(@RequestBody @Valid NastavnikObrazovanjeRequest request) {
        return nastavnikObrazovanjeService.createNastavnikObrazovanje(request);
    }

    @GetMapping("/{id}")
    public NastavnikObrazovanjeResponse getNastavnikObrazovanjeById(@PathVariable Long id) {
        return nastavnikObrazovanjeService.getNastavnikObrazovanjeById(id);
    }

    @GetMapping("/all")
    public List<NastavnikObrazovanjeResponse> getAllNastavnikObrazovanja() {
        return nastavnikObrazovanjeService.getAllNastavnikObrazovanja();
    }

    @GetMapping("/nastavnik/{nastavnikId}")
    public List<NastavnikObrazovanjeResponse> getObrazovanjaByNastavnikId(@PathVariable Long nastavnikId) {
        return nastavnikObrazovanjeService.getObrazovanjaByNastavnikId(nastavnikId);
    }

    @PutMapping("/{id}")
    public NastavnikObrazovanjeResponse updateNastavnikObrazovanje(@PathVariable Long id, @RequestBody @Valid NastavnikObrazovanjeRequest request) {
        return nastavnikObrazovanjeService.updateNastavnikObrazovanje(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteNastavnikObrazovanje(@PathVariable Long id) {
        nastavnikObrazovanjeService.deleteNastavnikObrazovanje(id);
    }
}
