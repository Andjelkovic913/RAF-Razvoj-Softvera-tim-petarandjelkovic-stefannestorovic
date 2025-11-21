package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.response.IzlazakNaIspitResponse;
import org.raflab.studsluzba.services.IzlazakNaIspitService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/izlazak-na-ispit")
@RequiredArgsConstructor
public class IzlazakNaIspitController {

    private final IzlazakNaIspitService izlazakNaIspitService;

    @GetMapping("/student-indeks/{id}/passed")
    public Page<IzlazakNaIspitResponse> getPassedExams(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return izlazakNaIspitService.getPassedExams(id, page, size);
    }

    @GetMapping("/student-indeks/{id}/failed")
    public Page<IzlazakNaIspitResponse> getFailedExams(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return izlazakNaIspitService.getFailedExams(id, page, size);
    }

    @GetMapping("/all")
    public Page<IzlazakNaIspitResponse> getAllExamResults(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return izlazakNaIspitService.getAllExamResults(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createExamResult(@RequestBody IzlazakNaIspitRequest request) {
        return izlazakNaIspitService.createExamResult(request);
    }
}
