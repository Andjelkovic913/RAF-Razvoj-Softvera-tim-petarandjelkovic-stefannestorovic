package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UplataRequest;
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

    /**
     * Kreira novu uplatu sa dohvatanjem trenutnog kursa EUR-a
     * POST /api/uplata/add
     */
    @PostMapping(path = "/add")
    public Long addNewUplata(@Valid @RequestBody UplataRequest uplataRequest) {
        return uplataService.createUplata(uplataRequest);
    }

    /**
     * Dohvata sve uplate
     * GET /api/uplata/all
     */
    @GetMapping(path = "/all")
    public List<UplataResponse> getAllUplate() {
        return uplataService.getAllUplate();
    }

    /**
     * Dohvata uplatu po ID-u
     * GET /api/uplata/{id}
     */
    @GetMapping(path = "/{id}")
    public UplataResponse getUplataById(@PathVariable Long id) {
        return uplataService.getUplataById(id);
    }

    /**
     * Dohvata sve uplate za određenog studenta
     * GET /api/uplata/student/{studentIndeksId}
     */
    @GetMapping(path = "/student/{studentIndeksId}")
    public List<UplataResponse> getUplateByStudentIndeksId(@PathVariable Long studentIndeksId) {
        return uplataService.getUplateByStudentIndeksId(studentIndeksId);
    }
}
