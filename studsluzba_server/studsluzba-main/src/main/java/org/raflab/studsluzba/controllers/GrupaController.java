package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.GrupaRequest;
import org.raflab.studsluzba.controllers.response.GrupaResponse;
import org.raflab.studsluzba.services.GrupaService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/grupa")
@RequiredArgsConstructor
public class GrupaController {

    private final GrupaService grupaService;

    @PostMapping("/add")
    public Long addNewGrupa(@RequestBody @Valid GrupaRequest request) {
        return grupaService.createGrupa(request);
    }

    @GetMapping("/{id}")
    public GrupaResponse getGrupaById(@PathVariable Long id) {
        return grupaService.getGrupaById(id);
    }

    @GetMapping("/all")
    public List<GrupaResponse> getAllGrupe() {
        return grupaService.getAllGrupe();
    }

    @PutMapping("/{id}")
    public GrupaResponse updateGrupa(@PathVariable Long id, @RequestBody @Valid GrupaRequest request) {
        return grupaService.updateGrupa(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteGrupa(@PathVariable Long id) {
        grupaService.deleteGrupa(id);
    }
}
