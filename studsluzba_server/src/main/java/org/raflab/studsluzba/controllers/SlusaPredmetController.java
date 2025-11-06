package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.Optional;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/slusapredmet")
public class SlusaPredmetController {

    SlusaPredmetRepository slusaPredmetRepo;

    //pravi novi entitet
    @PostMapping(path = "add")
    public Long creteSlusaPredmet(@RequestBody SlusaPredmetRequest zahtev){
        SlusaPredmet sp = slusaPredmetRepo.save(Converters.toSlusaPredmet(zahtev));
        return sp.getId();
    }

    // dohvati po id
    @GetMapping(path = "slusapredmet/{id}")
    public SlusaPredmet getSlusaPremdetById(@PathVariable Long id){
        return slusaPredmetRepo.findById(id).orElseThrow(
                () -> new EntityExistsException("Ne postoji ovaj SlusaPredmet"));
    }

    // dohvati sve
    @GetMapping(path = "slusapredmet/all")
    public Iterable<SlusaPredmet> getAllSlusaPredmet(){
        return slusaPredmetRepo.findAll();
    }

    // azuriraj po id
    @PutMapping(path="/slusapredmet/{id}")
    public SlusaPredmet updateSlusaPredmet(@PathVariable Long id, @RequestBody SlusaPredmet slusaPredmet) {
        Optional<SlusaPredmet> existing = slusaPredmetRepo.findById(id);
        if(existing.isEmpty()) return null;
        SlusaPredmet toUpdate = existing.get();
        toUpdate.setStudentIndeks(slusaPredmet.getStudentIndeks());
        toUpdate.setDrziPredmet(slusaPredmet.getDrziPredmet());
        toUpdate.setUpisGodine(slusaPredmet.getUpisGodine());
        toUpdate.setStudentVisokaUstanova(slusaPredmet.getStudentVisokaUstanova());
        return slusaPredmetRepo.save(toUpdate);
    }

    // obrisi po id
    @DeleteMapping(path="/slusapredmet/{id}")
    public void deleteSlusaPredmet(@PathVariable Long id) {
        slusaPredmetRepo.deleteById(id);
    }
}
