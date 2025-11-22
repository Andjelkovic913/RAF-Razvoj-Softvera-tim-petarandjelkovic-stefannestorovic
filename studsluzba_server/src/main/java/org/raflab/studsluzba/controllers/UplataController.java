package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.DodajUplataRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.PreostaliIznosResponse;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.services.UplataService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/uplata")
@AllArgsConstructor
public class UplataController {

    private final UplataService uplataService;

    // 1.8 - dodavanje nove uplate (čuva se datum uplate, iznos u dinarima i srednji kurs).
    // Trenutni srednji kurs ne treba da ima predefinisanu vrednost, nego ga treba dohvatiti api pozivom
    @PostMapping(path = "/add")
    public Long addNewUplata(@Valid @RequestBody UplataRequest uplataRequest) {
        return uplataService.createUplata(uplataRequest);
    }


    @GetMapping(path = "/all")
    public List<UplataResponse> getAllUplate() {
        return uplataService.getAllUplate();
    }


    @GetMapping(path = "/{id}")
    public UplataResponse getUplataById(@PathVariable Long id) {
        return uplataService.getUplataById(id);
    }


    @GetMapping(path = "/student/{studentIndeksId}")
    public List<UplataResponse> getUplateByStudentIndeksId(@PathVariable Long studentIndeksId) {
        return uplataService.getUplateByStudentIndeksId(studentIndeksId);
    }

    // Skracena verzija od "/add", samo automatski uzima aktivnu školsku godinu i tip uplate kao SKOLARINA
    @PostMapping(path = "/dodaj")
    public Long dodajUplata(@Valid @RequestBody DodajUplataRequest request) {
        return uplataService.dodajUplata(request);
    }

    // 1.9 - selekcija preostalog iznosa za uplatu u evrima i dinarima. Iznos školarine je predefinisana vrednost od 3000e
    @GetMapping(path = "/preostalo/{studentIndeksId}")
    public PreostaliIznosResponse getPreostaliIznos(@PathVariable Long studentIndeksId) {
        return uplataService.getPreostaliIznos(studentIndeksId);
    }
}
