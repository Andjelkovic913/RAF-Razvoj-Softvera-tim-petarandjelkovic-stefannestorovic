package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentVisokaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.StudentVisokaUstanovaResponse;
import org.raflab.studsluzba.services.StudentVisokaUstanovaService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/student-visoka-ustanova")
@RequiredArgsConstructor
public class StudentVisokaUstanovaController {

    private final StudentVisokaUstanovaService studentVisokaUstanovaService;

    @PostMapping("/add")
    public Long addStudentToVisokaUstanova(@RequestBody @Valid StudentVisokaUstanovaRequest request) {
        return studentVisokaUstanovaService.addStudentToVisokaUstanova(request);
    }

    @GetMapping("/{id}")
    public StudentVisokaUstanovaResponse getStudentVisokaUstanovaById(@PathVariable Long id) {
        return studentVisokaUstanovaService.getStudentVisokaUstanovaById(id);
    }

    @GetMapping("/student/{studentPodaciId}")
    public List<StudentVisokaUstanovaResponse> getStudentVisokeUstanoveByStudentId(@PathVariable Long studentPodaciId) {
        return studentVisokaUstanovaService.getStudentVisokeUstanoveByStudentId(studentPodaciId);
    }

    @PutMapping("/{id}")
    public StudentVisokaUstanovaResponse updateStudentVisokaUstanova(@PathVariable Long id, @RequestBody @Valid StudentVisokaUstanovaRequest request) {
        return studentVisokaUstanovaService.updateStudentVisokaUstanova(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentVisokaUstanova(@PathVariable Long id) {
        studentVisokaUstanovaService.deleteStudentVisokaUstanova(id);
    }
}
