package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.response.FailedSubjectsResponse;
import org.raflab.studsluzba.controllers.response.ObnovaGodineResponse;
import org.raflab.studsluzba.services.ObnovaGodineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/obnova-godine")
@RequiredArgsConstructor
public class ObnovaGodineController {

    private final ObnovaGodineService obnovaGodineService;

    @GetMapping("/student-indeks/{studentIndeksId}")
    public List<ObnovaGodineResponse> getObnovaGodineByStudentIndeks(@PathVariable Long studentIndeksId) {
        return obnovaGodineService.getObnovaGodineByStudentIndeks(studentIndeksId);
    }

    @GetMapping("/failed-subjects/{studentIndeksId}")
    public FailedSubjectsResponse getFailedSubjects(@PathVariable Long studentIndeksId) {
        return obnovaGodineService.getFailedSubjectsForStudent(studentIndeksId);
    }

    @PostMapping("/obnovi")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createObnovaGodine(@RequestBody @Valid ObnovaGodineRequest request) {
        return obnovaGodineService.createObnovaGodine(request);
    }
}
