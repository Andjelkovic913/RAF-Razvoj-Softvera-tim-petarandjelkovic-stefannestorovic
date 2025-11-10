package org.raflab.studsluzba.controllers;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.services.NastavnikService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/nastavnik")
public class NastavnikController {

	private final NastavnikService nastavnikService;
    private final NastavnikRepository nastavnikRepository;
    private final EntityMappers entityMappers;
    private final Converters converters;


	// kreiranje novog nastavnika
	@PostMapping(path = "/add")
	public Long addNewNastavnik(@RequestBody @Valid NastavnikRequest nastavnikRequest) {
		Nastavnik nastavnik = nastavnikService.save(converters.toNastavnik(nastavnikRequest));
		return nastavnik.getId();
	}

	// lsita svih nastavnika
	@GetMapping(path = "/all")
	public List<NastavnikResponse> getAllNastavnik() {
		return converters.toNastavnikResponseList(nastavnikService.findAll());
	}

	// dobavljanje nastavnika po id-u
	@GetMapping(path = "/{id}")
	public NastavnikResponse getNastavnikById(@PathVariable Long id)
	{
		Optional<Nastavnik> rez = nastavnikService.findById(id);

		return rez.map(converters::toNastavnikResponse).orElse(null);
	}

	// pretraga po imenu i prezimenu
	@GetMapping(path = "/search")
	public List<NastavnikResponse> search(
			@RequestParam(required = false) String ime,
			@RequestParam(required = false) String prezime){

        return converters.toNastavnikResponseList(nastavnikService.findByImeAndPrezime(ime, prezime));
	}

	//azuriranje nastavnika na osnovu id-a
	@PutMapping(path = "/nastavnik/update/{id}")
	public NastavnikResponse updateNastavnik(@PathVariable Long id, @RequestBody NastavnikRequest zahtev){
		Nastavnik nastavnik = nastavnikRepository.findById(id).orElseThrow(
				() -> new EntityExistsException("Nastavnik ne postoji"));
		converters.updateNastavnik(nastavnik, zahtev);
		Nastavnik promenjen = nastavnikRepository.save(nastavnik);
		return entityMappers.fromNastavnikToResponse(promenjen);
	}

	//brisanje nastavnik na osnovu id-a
	@DeleteMapping(path = "/nastavnik/delete/{id}")
	public void deleteNastavnik(@PathVariable Long id){
		nastavnikRepository.deleteById(id);
	}

	
}
