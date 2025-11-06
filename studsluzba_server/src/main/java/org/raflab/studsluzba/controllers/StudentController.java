package org.raflab.studsluzba.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(path="/api/student")
@AllArgsConstructor
public class StudentController {

	StudentPodaciRepository studentPodaciRepository;
	
	StudentIndeksRepository studentIndeksRepository;
	
	StudentProfileService studentProfileService;

	StudentIndeksService studentIndeksService;

	StudijskiProgramRepository studijskiProgramRepository;

    EntityMappers entityMappers;

	// todo StudentPodaci

	// Kreiranje novog studenta
	@PostMapping(path="/add")
	public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaci) {
		StudentPodaci sp = studentPodaciRepository.save(Converters.toStudentPodaci(studentPodaci));
		return sp.getId();
	}

	// Dohvati sve
	@GetMapping(path="/all")
	public Iterable<StudentPodaciResponse> getAllStudentPodaci() {
		return studentPodaciRepository.findAll().stream()
				.map(entityMappers::fromStudentPodaciToResponse)
				.collect(Collectors.toList());
	}

	// Dohvati sve sa paginacijom
	@GetMapping(path="/svi")
	public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		return studentPodaciRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
				.map(entityMappers::fromStudentPodaciToResponse);
	}

	// dohvati po id-u
	@GetMapping(path="/podaci/{id}")
	public StudentPodaciResponse getStudentPodaci(@PathVariable Long id){
		Optional<StudentPodaci> rez = studentPodaciRepository.findById(id);
		if(rez.isEmpty()) return null;
		return entityMappers.fromStudentPodaciToResponse(rez.get());
	}


// update na onsovu id-a
	@PutMapping(path = "/update/{id}")
	public StudentPodaciResponse updateStudentPodaci(@PathVariable Long id, @RequestBody StudentPodaciRequest zahtev){
		StudentPodaci student = studentPodaciRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Student ne postoji"));
		Converters.updateStudentPodaci(student, zahtev);
		StudentPodaci promenjen = studentPodaciRepository.save(student);
		return entityMappers.fromStudentPodaciToResponse(promenjen);
	}

// delete za id
	@DeleteMapping(path = "/{id}")
	public void deleteStudentPodaci(@PathVariable Long id){
		studentPodaciRepository.deleteById(id);
	}

	// todo StudentIndeks

	//Kreiranje novog indeksa
	@PostMapping(path="/saveindeks")
	public Long saveIndeks(@RequestBody StudentIndeksRequest request) {
		StudentIndeks studentIndeks = Converters.toStudentIndeks(request);

		int nextBroj = studentIndeksService.findBroj(request.getGodina(), request.getStudProgramOznaka());
		studentIndeks.setBroj(nextBroj);

		Optional<StudentPodaci> studentPodaci = studentPodaciRepository.findById(request.getStudentId());
		if(studentPodaci.isEmpty()) return null;		//TODO - throw error if not exist
		studentIndeks.setStudent(studentPodaci.get());

		List<StudijskiProgram> studijskiProgrami = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka());
		if (studijskiProgrami.isEmpty()) {
			throw new RuntimeException("No StudijskiProgram found with oznaka: " + request.getStudProgramOznaka());
		}
		studentIndeks.setStudijskiProgram(studijskiProgrami.get(0));

		try {
			StudentIndeks savedStudentIndeks = studentIndeksRepository.save(studentIndeks);
			return savedStudentIndeks.getId();
		} catch (DataIntegrityViolationException e) {
			// TODO
			throw new RuntimeException("Duplicate entry for broj, godina, and studProgramOznaka", e);
		} catch (Exception e) {
			throw new RuntimeException("Error while saving the StudentIndeks.", e);
		}
	}

    // dohvatanje po id-u
    @GetMapping(path="/indeks/{id}")
    public StudentIndeksResponse getStudentIndeks(@PathVariable Long id){
    	Optional<StudentIndeks> rez = studentIndeksRepository.findById(id);
    	if(rez.isEmpty()) return null;
    	else {
    		StudentIndeks retVal = rez.get();    		
    		return entityMappers.fromStudentIndexToResponse(retVal);
    	}
    	    	
    }

	//Lista indeksa za Studenta
    @GetMapping(path="/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci){
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci)
                .stream()
                .map(entityMappers::fromStudentIndexToResponse) // map each entity to response
                .collect(Collectors.toList());
    }

	// Pretraga po indeksu
    @GetMapping(path="/fastsearch")  // salje se string oblika rn1923 - smer godina broj
    public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
      String[] parsedData = ParseUtils.parseIndeks(indeksShort);
      if(parsedData!=null) {
    	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));	
    	  return entityMappers.fromStudentIndexToResponse(si);
      }else return null;
    }

	// Pretraga po mejlu
    @GetMapping(path="/emailsearch")  // salje se email studenta
    public StudentIndeksResponse emailSearch(@RequestParam String studEmail) {
      String[] parsedData = ParseUtils.parseEmail(studEmail);
      if(parsedData!=null) {
    	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));	
    	  return entityMappers.fromStudentIndexToResponse(si);
      }else return null;
    }

	// Napredna pretraga
    @GetMapping(path="/search")  // pretraga po imenu, prezimenu i elementima indeksa
    public Page<StudentDTO> search(@RequestParam (required = false) String ime,
								   @RequestParam (required = false) String prezime,
								   @RequestParam (required = false) String studProgram,
								   @RequestParam (required = false) Integer godina,
								   @RequestParam (required = false) Integer broj,
								   @RequestParam(defaultValue = "0") Integer page,
								   @RequestParam(defaultValue = "10") Integer size) {

		if(studProgram==null && godina == null && broj==null) { // pretrazivanje studenata bez indeksa
    		Page<StudentPodaci> spList = studentPodaciRepository.findStudent(ime, prezime, PageRequest.of(page, size, Sort.by("id").descending()));
			return spList.map(EntityMappers::fromStudentPodaciToDTO);
    	}
    	Page<StudentIndeks> siList = studentIndeksRepository.findStudentIndeks(ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()));
		return siList.map(EntityMappers::fromStudentIndeksToDTO);
    }

	// profil studenta
    @GetMapping(path="/profile/{studentIndeksId}")  
    public StudentProfileDTO getStudentProfile(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentProfile(studentIndeksId);   	
    }

	// web profil
    @GetMapping(path="/webprofile/{studentIndeksId}")  
    public StudentWebProfileDTO getStudentWebProfile(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentWebProfile(studentIndeksId);   	
    }

	// dobijanje web profila na osnovu mejla
    @GetMapping(path="/webprofile/email")  
    public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
    	 String[] parsedData = ParseUtils.parseEmail(studEmail);
         if(parsedData!=null) {
       	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));
       	  if(si!=null)
       		  return studentProfileService.getStudentWebProfile(si.getId());   	
         }
         return null;
    }

	
	// azurira osnovne podatke studenta na osnovu njegovor indeksa
	@PutMapping(path = "/update/{studProgramOznaka}/{godina}/{broj}")
	public StudentPodaciResponse updateStudentPodaci(@PathVariable String studProgramOznaka,
													 @PathVariable int godina,
													 @PathVariable int broj,
													 @Valid @RequestBody StudentPodaciRequest request) {
		StudentIndeks indeks = studentIndeksRepository.findStudentIndeks(studProgramOznaka.toUpperCase(), godina, broj);
		if (indeks == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Student sa indeksom %s %d/%d ne postoji", studProgramOznaka, broj, godina));
		}

		StudentPodaci studentPodaci = indeks.getStudent();
		applyStudentPodaciUpdates(studentPodaci, request);

		StudentPodaci saved = studentPodaciRepository.save(studentPodaci);
		return entityMappers.fromStudentPodaciToResponse(saved);
	}

    private void applyStudentPodaciUpdates(StudentPodaci target, StudentPodaciRequest source) {
        target.setIme(source.getIme());
        target.setPrezime(source.getPrezime());
        target.setSrednjeIme(source.getSrednjeIme());
        target.setJmbg(source.getJmbg());
        target.setDatumRodjenja(source.getDatumRodjenja());
        target.setMestoRodjenja(source.getMestoRodjenja());
        target.setMestoPrebivalista(source.getMestoPrebivalista());
        target.setDrzavaRodjenja(source.getDrzavaRodjenja());
        target.setDrzavljanstvo(source.getDrzavljanstvo());
        target.setNacionalnost(source.getNacionalnost());
        target.setPol(source.getPol());
        target.setAdresa(source.getAdresa());
        target.setBrojTelefonaMobilni(source.getBrojTelefonaMobilni());
        target.setBrojTelefonaFiksni(source.getBrojTelefonaFiksni());
        target.setEmailPrivatni(source.getEmailPrivatni());
        target.setEmailFakultetski(source.getEmailFakultetski());
        target.setBrojLicneKarte(source.getBrojLicneKarte());
        target.setLicnuKartuIzdao(source.getLicnuKartuIzdao());
        target.setMestoStanovanja(source.getMestoStanovanja());
        target.setAdresaStanovanja(source.getAdresaStanovanja());
    }



}
