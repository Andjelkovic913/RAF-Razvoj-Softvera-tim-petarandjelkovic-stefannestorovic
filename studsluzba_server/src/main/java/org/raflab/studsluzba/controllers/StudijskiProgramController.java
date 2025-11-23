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

    // dodavanje novog studijskog programa
    @PostMapping(path = "/add")
    public Long addStudijskiProgram(@RequestBody StudijskiProgramRequest zahtev){
        return studijskiProgramService.createStudijskiProgram(zahtev);
    }

    // spisak svih studijskih programa (sortirano opadajuće)
    @GetMapping(path = "/all/sorted")
    public List<StudijskiProgramResponse> getAllStudProgramiSortedDesc(){
        return studijskiProgramService.getAllSortedDesc();
    }

    // spisak svih oznaka studijskih programa
    @GetMapping(path="/oznaka/all")
    public List<String> getAllStudProgramOznaka() {
        return studijskiProgramService.getAllOznaka();
    }

    // ažuriranje studijskog programa
    @PostMapping(path = "/update/{id}")
    public StudijskiProgramResponse updateStudijskiProgram(@PathVariable Long id, @RequestBody StudijskiProgramRequest zahtev){
        return studijskiProgramService.updateStudijskiProgram(id, zahtev);
    }

    // brisanje studijskog programa
    @DeleteMapping(path = "/{id}")
    public void deleteStudijskiProgram(@PathVariable Long id){
        studijskiProgramService.deleteStudijskiProgram(id);
    }
}
