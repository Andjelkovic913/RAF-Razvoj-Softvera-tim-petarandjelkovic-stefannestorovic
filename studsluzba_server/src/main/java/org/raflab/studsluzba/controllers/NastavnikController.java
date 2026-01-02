package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.services.NastavnikService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/nastavnik")
public class NastavnikController {

	private final NastavnikService nastavnikService;

	// kreiranje novog nastavnika
	@PostMapping(path = "/add")
	public Long addNewNastavnik(@RequestBody @Valid NastavnikRequest nastavnikRequest) {
		return nastavnikService.save(nastavnikRequest);
	}

	// lsita svih nastavnika
	@GetMapping(path = "/all")
	public List<NastavnikResponse> getAllNastavnik() {
		return nastavnikService.findAll();
	}

	// dobavljanje nastavnika po id-u
	@GetMapping(path = "/{id}")
	public NastavnikResponse getNastavnikById(@PathVariable Long id) {
		return nastavnikService.findById(id);
	}

	// pretraga po imenu i prezimenu
	@GetMapping(path = "/search")
	public List<NastavnikResponse> search(
			@RequestParam(required = false) String ime,
			@RequestParam(required = false) String prezime) {
		return nastavnikService.findByImeAndPrezime(ime, prezime);
	}

	// pretraga nastavnika po zvanju
	@GetMapping(path = "/search/zvanje")
	public List<NastavnikResponse> getNastavniciByZvanje(@RequestParam String zvanje) {
		return nastavnikService.findByZvanje(zvanje);
	}

	// azuriranje nastavnika na osnovu id-a
	@PutMapping(path = "/nastavnik/update/{id}")
	public NastavnikResponse updateNastavnik(@PathVariable Long id, @RequestBody @Valid NastavnikRequest zahtev) {
		return nastavnikService.updateNastavnik(id, zahtev);
	}

	// brisanje nastavnik na osnovu id-a
	@DeleteMapping(path = "/nastavnik/delete/{id}")
	public void deleteNastavnik(@PathVariable Long id) {
		nastavnikService.deleteNastavnik(id);
	}
}
