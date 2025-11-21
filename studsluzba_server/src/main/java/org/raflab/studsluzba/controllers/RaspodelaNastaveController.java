package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetAddRequest;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.dtos.StudentIndeksDTO;
import org.raflab.studsluzba.services.RaspodelaNastaveService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/professor/raspodelanastave")
public class RaspodelaNastaveController {

    private final RaspodelaNastaveService raspodelaNastaveService;

    @GetMapping(path = "/drzipredmet/aktivna/nastavnik/{idNastavnika}")
    public List<PredmetResponse> getDrziPredmetUAktivnojSkolskojGodini(@PathVariable Long idNastavnika) {
        return raspodelaNastaveService.getPredmetiZaNastavnikaUAktivnojSkolskojGodini(idNastavnika);
    }

    @GetMapping(path = "/slusapredmetaktivna/{idPredmeta}/{idNastavnika}")
    public List<StudentIndeksDTO> getSlusaPredmetUAktivnojSkolskojGodini(@PathVariable Long idPredmeta, @PathVariable Long idNastavnika) {
        return raspodelaNastaveService.getStudentiSlusaPredmetAktivnaGodina(idPredmeta, idNastavnika);
    }

    @GetMapping(path = "/slusapredmetaktivna/{idDrziPredmet}")
    public List<StudentIndeksDTO> getSlusaPredmetUAktivnojSkolskojGodiniForDrziPredmet(@PathVariable Long idDrziPredmet) {
        return raspodelaNastaveService.getStudentiSlusaPredmetZaDrziPredmet(idDrziPredmet);
    }

    @GetMapping(path = "/neslusapredmetaktivna/{idDrziPredmet}")
    public List<StudentIndeksDTO> getNeSlusaPredmetUAktivnojSkolskojGodiniForDrziPredmet(@PathVariable Long idDrziPredmet) {
        return raspodelaNastaveService.getStudentiNeSlusajuDrziPredmet(idDrziPredmet);
    }

    @PostMapping(path="/drzipredmet/add")
    public Long addDrziPredmet(@RequestBody @Valid DrziPredmetAddRequest request) {
        return raspodelaNastaveService.addDrziPredmet(request);
    }

    @PostMapping(path="/slusapredmet/add")
    public Long addSlusaPredmet(@RequestBody @Valid SlusaPredmetRequest request) {
        return raspodelaNastaveService.addSlusaPredmet(request);
    }

    @DeleteMapping(path="/drzipredmet/{id}")
    public void deleteDrziPredmet(@PathVariable Long id) {
        raspodelaNastaveService.deleteDrziPredmet(id);
    }
}
