package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentSrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.StudentSrednjaSkolaResponse;
import org.raflab.studsluzba.services.StudentSrednjaSkolaManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student-srednja-skola")
public class StudentSrednjaSkolaController {

    private final StudentSrednjaSkolaManagementService studentSrednjaSkolaManagementService;

    @PostMapping("/add")
    public Long addStudentToSrednjaSkola(@RequestBody @Valid StudentSrednjaSkolaRequest request) {
        return studentSrednjaSkolaManagementService.addStudentToSrednjaSkola(request);
    }

    @GetMapping("/{id}")
    public StudentSrednjaSkolaResponse getStudentSrednjaSkolaById(@PathVariable Long id) {
        return studentSrednjaSkolaManagementService.getStudentSrednjaSkolaById(id);
    }

    @GetMapping("/student/{studentPodaciId}")
    public List<StudentSrednjaSkolaResponse> getStudentSrednjeSkolaByStudentId(@PathVariable Long studentPodaciId) {
        return studentSrednjaSkolaManagementService.getStudentSrednjeSkolaByStudentId(studentPodaciId);
    }

    @PutMapping("/{id}")
    public StudentSrednjaSkolaResponse updateStudentSrednjaSkola(@PathVariable Long id, @RequestBody @Valid StudentSrednjaSkolaRequest request) {
        return studentSrednjaSkolaManagementService.updateStudentSrednjaSkola(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentSrednjaSkola(@PathVariable Long id) {
        studentSrednjaSkolaManagementService.deleteStudentSrednjaSkola(id);
    }
}




