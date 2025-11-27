package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.VrstaStudijaRequest;
import org.raflab.studsluzba.controllers.response.VrstaStudijaResponse;
import org.raflab.studsluzba.services.VrstaStudijaService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/vrsta-studija")
@RequiredArgsConstructor
public class VrstaStudijaController {

    private final VrstaStudijaService vrstaStudijaService;

    @PostMapping("/add")
    public Long addNewVrstaStudija(@RequestBody @Valid VrstaStudijaRequest request) {
        return vrstaStudijaService.createVrstaStudija(request);
    }

    @GetMapping("/{id}")
    public VrstaStudijaResponse getVrstaStudijaById(@PathVariable Long id) {
        return vrstaStudijaService.getVrstaStudijaById(id);
    }

    @GetMapping("/all")
    public List<VrstaStudijaResponse> getAllVrsteStudija() {
        return vrstaStudijaService.getAllVrsteStudija();
    }

    @PutMapping("/{id}")
    public VrstaStudijaResponse updateVrstaStudija(@PathVariable Long id, @RequestBody @Valid VrstaStudijaRequest request) {
        return vrstaStudijaService.updateVrstaStudija(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteVrstaStudija(@PathVariable Long id) {
        vrstaStudijaService.deleteVrstaStudija(id);
    }
}
