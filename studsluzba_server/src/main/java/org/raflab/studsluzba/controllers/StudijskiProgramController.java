package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="/api/studijskiprogram")
@AllArgsConstructor
public class StudijskiProgramController{

    final StudijskiProgramRepository studijskiProgramRepository;
    final SkolskaGodinaRepository skolskaGodinaRepository;
    private final EntityMappers entityMappers;


    // kreiranje stud porgrama
    @PostMapping(path = "/add")
    public Long addStudijskiProgram(@RequestBody StudijskiProgramRequest zahtev){
        StudijskiProgram studijskiProgram = Converters.toStudijskiProgram(zahtev);
        if(zahtev.getSkolskaGodinaId() != null){
            Optional<SkolskaGodina> sg = skolskaGodinaRepository.findById(zahtev.getSkolskaGodinaId());
            if(sg.isPresent())
                studijskiProgram.setSkolskaGodina(sg.get());
        }
        studijskiProgramRepository.save(studijskiProgram);
        return studijskiProgram.getId();
    }

    // lista sortirana po godini od najnovije
    @GetMapping(path = "/all/sorted")
    public List<StudijskiProgram> getAllStudProgramiSortedDesc(){
        return studijskiProgramRepository.getAllSortedByGodinaDesc();
    }

    // lista oznaka programa
    @GetMapping(path="/oznaka/all")
    public Iterable<String> getAllStudProgramOznaka() {
        return studijskiProgramRepository.findAllOznaka();
    }

    // azuriranje programa po id-u
    @PostMapping(path = "/update/{id}")
    public StudijskiProgramResponse updateStudijskiProgram(@PathVariable Long id, @RequestBody StudijskiProgramRequest zahtev){
        StudijskiProgram program = studijskiProgramRepository.findById(id).orElseThrow(
                () -> new EntityExistsException("Ne postoji ovaj studijski program")
        );
        Converters.updateStudijskiProgram(program, zahtev);
        StudijskiProgram novi = studijskiProgramRepository.save(program);
        return entityMappers.fromStudijskiProgramToResponse(novi);
    }

    // brisanje programa
    @DeleteMapping(path = "/{id}")
    public void deleteStudijskiProgram(@PathVariable Long id){
        studijskiProgramRepository.deleteById(id);
    }
}
