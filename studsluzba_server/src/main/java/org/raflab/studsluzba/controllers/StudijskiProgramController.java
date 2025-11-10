package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.services.StudijskiProgramService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/api/studijskiprogram")
@AllArgsConstructor
public class StudijskiProgramController{

    private final StudijskiProgramService studijskiProgramService;

    @PostMapping(path = "/add")
    public Long addStudijskiProgram(@RequestBody StudijskiProgramRequest zahtev){
        return studijskiProgramService.createStudijskiProgram(zahtev);
    }

    @GetMapping(path = "/all/sorted")
    public List<StudijskiProgramResponse> getAllStudProgramiSortedDesc(){
        return studijskiProgramService.getAllSortedDesc();
    }

    @GetMapping(path="/oznaka/all")
    public List<String> getAllStudProgramOznaka() {
        return studijskiProgramService.getAllOznaka();
    }

    @PostMapping(path = "/update/{id}")
    public StudijskiProgramResponse updateStudijskiProgram(@PathVariable Long id, @RequestBody StudijskiProgramRequest zahtev){
        return studijskiProgramService.updateStudijskiProgram(id, zahtev);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStudijskiProgram(@PathVariable Long id){
        studijskiProgramService.deleteStudijskiProgram(id);
    }
}
