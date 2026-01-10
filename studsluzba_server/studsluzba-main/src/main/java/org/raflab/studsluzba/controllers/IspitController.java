package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.services.IspitService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ispit")
@RequiredArgsConstructor
public class IspitController {
    private final IspitService ispitService;

    @PostMapping("/add")
    public Long addNewIspit(@RequestBody @Valid IspitRequest ispitRequest) {
        return ispitService.save(ispitRequest);
    }

    @GetMapping("/{id}")
    public IspitResponse getIspitById(@PathVariable Long id) {
        return ispitService.getById(id);
    }

    @GetMapping("/all")
    public List<IspitResponse> getAllIspiti() {
        return ispitService.getAll();
    }

    @PutMapping("/{id}")
    public IspitResponse updateIspit(@PathVariable Long id, @RequestBody @Valid IspitRequest ispitRequest) {
        return ispitService.update(id, ispitRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteIspit(@PathVariable Long id) {
        ispitService.delete(id);
    }

}
