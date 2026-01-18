package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.services.SlusaPredmetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/slusapredmet")
public class SlusaPredmetController {

    private final SlusaPredmetService slusaPredmetService;

    @PostMapping(path = "add")
    public Long createSlusaPredmet(@RequestBody SlusaPredmetRequest zahtev){
        return slusaPredmetService.createSlusaPredmet(zahtev);
    }

    @GetMapping(path = "slusapredmet/{id}")
    public SlusaPredmetResponse getSlusaPremdetById(@PathVariable Long id){
        return slusaPredmetService.getSlusaPredmetById(id);
    }

    @GetMapping(path = "slusapredmet/all")
    public List<SlusaPredmetResponse> getAllSlusaPredmet(){
        return slusaPredmetService.getAllSlusaPredmet();
    }

    @PutMapping(path="/slusapredmet/{id}")
    public SlusaPredmetResponse updateSlusaPredmet(@PathVariable Long id, @RequestBody SlusaPredmetRequest zahtev) {
        return slusaPredmetService.updateSlusaPredmet(id, zahtev);
    }

    @DeleteMapping(path="/slusapredmet/{id}")
    public void deleteSlusaPredmet(@PathVariable Long id) {
        slusaPredmetService.deleteSlusaPredmet(id);
    }
}
