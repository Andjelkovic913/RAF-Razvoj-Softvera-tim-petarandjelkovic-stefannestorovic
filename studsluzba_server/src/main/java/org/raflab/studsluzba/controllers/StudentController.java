package org.raflab.studsluzba.controllers;

import java.util.List;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(path="/api/student")
@AllArgsConstructor
public class StudentController {

	private final StudentService studentService;
	private final StudentProfileService studentProfileService;

	// todo StudentPodaci

	@PostMapping(path="/add")
	public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaci) {
		return studentService.createStudentPodaci(studentPodaci);
	}

	@GetMapping(path="/all")
	public List<StudentPodaciResponse> getAllStudentPodaci() {
		return studentService.getAllStudentPodaci();
	}

	@GetMapping(path="/svi")
	public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		return studentService.getAllStudentPodaciPaginated(page, size);
	}

	@GetMapping(path="/podaci/{id}")
	public StudentPodaciResponse getStudentPodaci(@PathVariable Long id){
		return studentService.getStudentPodaciById(id);
	}

	@PutMapping(path = "/update/{id}")
	public StudentPodaciResponse updateStudentPodaci(@PathVariable Long id, @RequestBody StudentPodaciRequest zahtev){
		return studentService.updateStudentPodaci(id, zahtev);
	}

	@DeleteMapping(path = "/{id}")
	public void deleteStudentPodaci(@PathVariable Long id){
		studentService.deleteStudentPodaci(id);
	}

	// todo StudentIndeks

	@PostMapping(path="/saveindeks")
	public Long saveIndeks(@RequestBody StudentIndeksRequest request) {
		return studentService.saveIndeks(request);
	}

    @GetMapping(path="/indeks/{id}")
    public StudentIndeksResponse getStudentIndeks(@PathVariable Long id){
    	return studentService.getStudentIndeksById(id);
    }

    @GetMapping(path="/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci){
        return studentService.getIndeksiForStudentPodaciId(idStudentPodaci);
    }

    @GetMapping(path="/fastsearch")
    public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
      return studentService.fastSearch(indeksShort);
    }

    @GetMapping(path="/emailsearch")
    public StudentIndeksResponse emailSearch(@RequestParam String studEmail) {
      return studentService.emailSearch(studEmail);
    }

    @GetMapping(path="/search")
    public Page<StudentDTO> search(@RequestParam (required = false) String ime,
								   @RequestParam (required = false) String prezime,
								   @RequestParam (required = false) String studProgram,
								   @RequestParam (required = false) Integer godina,
								   @RequestParam (required = false) Integer broj,
								   @RequestParam(defaultValue = "0") Integer page,
								   @RequestParam(defaultValue = "10") Integer size) {
		return studentService.search(ime, prezime, studProgram, godina, broj, page, size);
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

    @GetMapping(path="/webprofile/email")
    public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
    	StudentIndeksResponse si = studentService.emailSearch(studEmail);
    	if(si != null) {
    		return studentProfileService.getStudentWebProfile(si.getId());
    	}
    	return null;
    }

	@PutMapping(path = "/update/{studProgramOznaka}/{godina}/{broj}")
	public StudentPodaciResponse updateStudentPodaci(@PathVariable String studProgramOznaka,
													 @PathVariable int godina,
													 @PathVariable int broj,
													 @Valid @RequestBody StudentPodaciRequest request) {
		return studentService.updateStudentPodaciByIndeks(studProgramOznaka, godina, broj, request);
	}

    @GetMapping(path="/podaci/prekoindeksa/broj/{broj}")
    public StudentPodaciResponse getStudentPodaciByBroj(@PathVariable int broj,
                                                        @RequestParam(required = false) String studProgramOznaka,
                                                        @RequestParam(required = false) Integer godina) {
        return studentService.getStudentPodaciByBroj(broj, studProgramOznaka, godina);
    }



}
