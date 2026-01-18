package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.VisokoskolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoskolskaUstanovaResponse;
import org.raflab.studsluzba.services.VisokoskolskaUstanovaService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/visokoskolska-ustanova")
@RequiredArgsConstructor
public class VisokoskolskaUstanovaController {

    private final VisokoskolskaUstanovaService visokaUstanovaService;

    @PostMapping("/add")
    public VisokoskolskaUstanovaResponse addNewVisokoskolskaUstanova(@RequestBody @Valid VisokoskolskaUstanovaRequest request) {
        return visokaUstanovaService.createVisokoskolskaUstanovaAndReturn(request);
    }

    @GetMapping("/{id}")
    public VisokoskolskaUstanovaResponse getVisokoskolskaUstanovaById(@PathVariable Long id) {
        return visokaUstanovaService.getVisokoskolskaUstanovaById(id);
    }

    @GetMapping("/all")
    public List<VisokoskolskaUstanovaResponse> getAllVisokoskolskeUstanove() {
        return visokaUstanovaService.getAllVisokoskolskeUstanove();
    }

    @PutMapping("/{id}")
    public VisokoskolskaUstanovaResponse updateVisokoskolskaUstanova(@PathVariable Long id, @RequestBody @Valid VisokoskolskaUstanovaRequest request) {
        return visokaUstanovaService.updateVisokoskolskaUstanova(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteVisokoskolskaUstanova(@PathVariable Long id) {
        visokaUstanovaService.deleteVisokoskolskaUstanova(id);
    }
}
