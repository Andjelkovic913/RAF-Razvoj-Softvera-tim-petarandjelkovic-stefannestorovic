package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredispitneObavezeRequest;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.services.PredispitneObavezeService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/predispitne-obaveze")
@RequiredArgsConstructor
public class PredispitneObavezeController {

    private final PredispitneObavezeService predispitneObavezeService;

    @PostMapping("/add")
    public Long addPredispitneObaveze(@RequestBody @Valid PredispitneObavezeRequest request) {
        return predispitneObavezeService.create(request);
    }

    @GetMapping("/{id}")
    public PredispitneObavezeResponse getPredispitneObavezeById(@PathVariable Long id) {
        return predispitneObavezeService.getById(id);
    }

    @GetMapping("/all")
    public List<PredispitneObavezeResponse> getAllPredispitneObaveze() {
        return predispitneObavezeService.getAll();
    }

    @PutMapping("/{id}")
    public PredispitneObavezeResponse updatePredispitneObaveze(@PathVariable Long id, @RequestBody @Valid PredispitneObavezeRequest request) {
        return predispitneObavezeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePredispitneObaveze(@PathVariable Long id) {
        predispitneObavezeService.delete(id);
    }
}
