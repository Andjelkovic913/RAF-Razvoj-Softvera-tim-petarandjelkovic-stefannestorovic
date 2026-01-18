package org.raflab.studsluzba.controllers;

import java.util.List;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.StudentIndeksRequest;
import org.raflab.studsluzba.dtos.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentBySrednjaSkolaResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzba.dtos.StudentProfileDTO;
import org.raflab.studsluzba.dtos.StudentReportDTO;
import org.raflab.studsluzba.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.services.StudentSrednjaSkolaService;
import org.raflab.studsluzba.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(path="/api/student")
@AllArgsConstructor
public class StudentController {

	private final StudentService studentService;
	private final StudentProfileService studentProfileService;
	private final StudentSrednjaSkolaService studentSrednjaSkolaService;

	// todo StudentPodaci

	@PostMapping(path="/add")
	public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaci) {
		return studentService.createStudentPodaci(studentPodaci);
	}

	@GetMapping(path="/all")
	public List<StudentPodaciResponse> getAllStudentPodaci() {
		return studentService.getAllStudentPodaci();
	}

	@GetMapping(path="/studijski-programi")
	public List<String> getAllStudijskiProgrami() {
		return studentService.getAllStudijskiProgrami();
	}

	@GetMapping(path="/report")
	public List<StudentReportDTO> getStudentsForReport() {
		return studentService.getStudentsForReport();
	}

	@GetMapping(path="/all-paginated")
	public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		return studentService.getAllStudentPodaciPaginated(page, size);
	}

//  selekcija studenta (njegovih licnih podataka) preko id-a
	@GetMapping(path="/podaci/{id}")
	public StudentPodaciResponse getStudentPodaciById(@PathVariable Long id){
		return studentService.getStudentPodaciById(id);
	}

	// 1.1 - selekcija studenta (njegovih ličnih podataka) preko broja indeksa, studProgramOznaka i godine
	@GetMapping(path="/podaci/prekoindeksa/broj/{broj}")
	public StudentPodaciResponse getStudentPodaciByBroj
	(@PathVariable int broj, @RequestParam String studProgramOznaka, @RequestParam Integer godina) {
		if (godina < 1950 || godina > 2027) {
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.BAD_REQUEST,
					"Godina mora biti validna [1950-2027]");
		}
		return studentService.getStudentPodaciByBroj(broj, studProgramOznaka, godina);
	}

	@PutMapping(path = "/update/{id}")
	public StudentPodaciResponse updateStudentPodaciById(@PathVariable Long id, @RequestBody StudentPodaciRequest zahtev){
		return studentService.updateStudentPodaci(id, zahtev);
	}

	@DeleteMapping(path = "/delete/{id}")
	public void deleteStudentPodaciById(@PathVariable Long id){
		studentService.deleteStudentPodaci(id);
	}

	// todo StudentIndeks

	@PostMapping(path="/saveindeks")
	public Long saveIndeks(@RequestBody StudentIndeksRequest request) {
		return studentService.saveIndeks(request);
	}

    @GetMapping(path="/indeks/{id}")
    public StudentIndeksResponse getStudentIndeksById(@PathVariable Long id){
    	return studentService.getStudentIndeksById(id);
    }

    @GetMapping(path="/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci){
        return studentService.getIndeksiForStudentPodaciId(idStudentPodaci);
    }

    @DeleteMapping(path="/indeks/{id}")
    public void deleteStudentIndeks(@PathVariable Long id){
        studentService.deleteStudentIndeks(id);
    }

    @PutMapping(path="/aktiviraj/{studentIndeksId}")
    public void aktivirajIndeks(@PathVariable Long studentIndeksId){
        studentService.aktivirajIndeks(studentIndeksId);
    }

	// 1.10 - selekcija studenata na osnovu imena i/ili prezimena (može samo ime, ili samo prezime ili oba da se unesu), paginirano
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
    public StudentProfileDTO getStudentProfileByIndeksId(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentProfile(studentIndeksId);
    }

	// web profil
    @GetMapping(path="/webprofile/{studentIndeksId}")
    public StudentWebProfileDTO getStudentWebProfileByIndeksId(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentWebProfile(studentIndeksId);
    }

    @GetMapping(path="/webprofile/email")
    public StudentWebProfileDTO getStudentWebProfileByEmail(@RequestParam String studEmail) {
    	StudentIndeksResponse si = studentService.emailSearch(studEmail);
    	if(si != null) {
    		return studentProfileService.getStudentWebProfile(si.getId());
    	}
    	return null;
    }

	@PutMapping(path = "/update/{studProgramOznaka}/{godina}/{broj}")
	public StudentPodaciResponse updateStudentPodaciByIndeks(@PathVariable String studProgramOznaka,
													 @PathVariable int godina,
													 @PathVariable int broj,
													 @Valid @RequestBody StudentPodaciRequest request) {
		return studentService.updateStudentPodaciByIndeks(studProgramOznaka, godina, broj, request);
	}


    // 1.11 - selekcija svih upisanih studenata koji su završili određenu srednju školu
    @GetMapping(path="/srednja-skola/{srednjaSkolaId}")
    public List<StudentBySrednjaSkolaResponse> getEnrolledStudentsBySrednjaSkola(@PathVariable Long srednjaSkolaId) {
        return studentSrednjaSkolaService.findEnrolledStudentsBySrednjaSkola(srednjaSkolaId);
    }

 // @GetMapping(path="/fastsearch")
    // public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
    //   return studentService.fastSearch(indeksShort);
    // }

    // @GetMapping(path="/emailsearch")
    // public StudentIndeksResponse emailSearch(@RequestParam String studEmail) {
    //   return studentService.emailSearch(studEmail);
    // }


}