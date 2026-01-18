package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.services.IspitniRokService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ispitni-rok")
@RequiredArgsConstructor
public class IspitniRokController {

    private final IspitniRokService ispitniRokService;

    @PostMapping("/add")
    public Long addIspitniRok(@RequestBody @Valid IspitniRokRequest request) {
        return ispitniRokService.create(request);
    }

    @GetMapping("/{id}")
    public IspitniRokResponse getIspitniRokById(@PathVariable Long id) {
        return ispitniRokService.getById(id);
    }

    @GetMapping("/all")
    public List<IspitniRokResponse> getAllIspitniRokovi() {
        return ispitniRokService.getAll();
    }

    @PutMapping("/{id}")
    public IspitniRokResponse updateIspitniRok(@PathVariable Long id, @RequestBody @Valid IspitniRokRequest request) {
        return ispitniRokService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteIspitniRok(@PathVariable Long id) {
        ispitniRokService.delete(id);
    }
}
