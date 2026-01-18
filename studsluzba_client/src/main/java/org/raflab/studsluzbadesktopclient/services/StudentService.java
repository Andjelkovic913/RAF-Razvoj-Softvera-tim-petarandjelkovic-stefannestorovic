package org.raflab.studsluzbadesktopclient.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RestController
@AllArgsConstructor
public class StudentService {
	
	private RestTemplate restTemplate;
	private WebClient webClient;
	private String baseUrl;
	
	private final String STUDENT_URL_PATH = "/api/student";

    private String createURL(String pathEnd) {
		return baseUrl + STUDENT_URL_PATH + "/" + pathEnd;
	}

	public List<StudentDTO> searchStudent(String ime) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("search"));
		builder.queryParam("ime", ime);
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<Map<String, Object>>() {}
		);
		if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
			return List.of();

		Object contentObj = response.getBody().get("content");
		if (!(contentObj instanceof List<?> list))
			return List.of();

		List<StudentDTO> result = new ArrayList<>();
		for (Object item : list) {
			if (!(item instanceof Map<?, ?> map)) continue;

			StudentDTO dto = new StudentDTO();
			Object imeVal = map.get("ime");
			Object prezimeVal = map.get("prezime");
			Object godinaUpisaVal = map.get("godinaUpisa");

			if (imeVal instanceof String) dto.setIme((String) imeVal);
			if (prezimeVal instanceof String) dto.setPrezime((String) prezimeVal);
			if (godinaUpisaVal instanceof Number) dto.setGodinaUpisa(((Number) godinaUpisaVal).intValue());

			result.add(dto);
		}
		return result;
	}

	public Integer saveStudent(StudentDTO student) {
		StudentPodaciRequest request = new StudentPodaciRequest();
		request.setIme(student.getIme());
		request.setPrezime(student.getPrezime());
		request.setSrednjeIme(student.getSrednjeIme());
		request.setJmbg(student.getJmbg());
		
		if (student.getDatumRodjenja() != null) {
			request.setDatumRodjenja(student.getDatumRodjenja().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
		}
		
		request.setMestoRodjenja(student.getMestoRodjenja());
		request.setMestoPrebivalista(student.getMestoStanovanja());
		request.setDrzavaRodjenja(student.getDrzavaRodjenja());
		request.setDrzavljanstvo(student.getDrzavljanstvo());
		request.setNacionalnost(student.getNacionalnost());
		request.setPol(student.getPol() != null && student.getPol().equals("M") ? 'M' : 'Z');
		request.setAdresa(student.getAdresa());
		request.setBrojTelefonaMobilni(student.getBrojTelefona());
		request.setEmailPrivatni(student.getEmailPrivatni());
		request.setEmailFakultetski(student.getEmailFakultet());
		request.setBrojLicneKarte(student.getLicnaKarta());
		request.setMestoStanovanja(student.getMestoStanovanja());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("add"));
		
		try {
			ResponseEntity<Long> response = restTemplate.postForEntity(builder.toUriString(), new HttpEntity<>(request, headers), Long.class);
			if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null)
				return response.getBody().intValue();
			else return null;
		} catch (Exception e) {
			System.err.println("Greška pri čuvanju studenta: " + e.getMessage());
			throw e;
		}
	}

	public Boolean updateStudentKontaktPodaci(StudentKontaktPodaciRequest kontaktPodaci) {
		if (kontaktPodaci.getStudentId() == null) {
			System.err.println("Student ID je obavezan za update kontakt podataka");
			return false;
		}

		try {
			// prvo dohvati postojece podatke studenta
			UriComponentsBuilder getBuilder = UriComponentsBuilder.fromHttpUrl(createURL("podaci/" + kontaktPodaci.getStudentId()));
			ResponseEntity<Map<String, Object>> getResponse = restTemplate.exchange(
					getBuilder.toUriString(),
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<Map<String, Object>>() {}
			);

			if (getResponse.getStatusCode() != HttpStatus.OK || getResponse.getBody() == null) {
				System.err.println("Student sa ID " + kontaktPodaci.getStudentId() + " ne postoji");
				return false;
			}

			Map<String, Object> postojeciPodaci = getResponse.getBody();

			StudentPodaciRequest request = new StudentPodaciRequest();
			request.setIme((String) postojeciPodaci.get("ime"));
			request.setPrezime((String) postojeciPodaci.get("prezime"));
			request.setSrednjeIme((String) postojeciPodaci.get("srednjeIme"));
			request.setJmbg((String) postojeciPodaci.get("jmbg"));
			
			if (postojeciPodaci.get("datumRodjenja") != null) {
				request.setDatumRodjenja(java.time.LocalDate.parse(postojeciPodaci.get("datumRodjenja").toString()));
			}
			
			request.setMestoRodjenja((String) postojeciPodaci.get("mestoRodjenja"));
			request.setMestoPrebivalista((String) postojeciPodaci.get("mestoPrebivalista"));
			request.setDrzavaRodjenja((String) postojeciPodaci.get("drzavaRodjenja"));
			request.setDrzavljanstvo((String) postojeciPodaci.get("drzavljanstvo"));
			request.setNacionalnost((String) postojeciPodaci.get("nacionalnost"));
			
			if (postojeciPodaci.get("pol") != null) {
				request.setPol(postojeciPodaci.get("pol").toString().charAt(0));
			}
			
			request.setBrojLicneKarte((String) postojeciPodaci.get("brojLicneKarte"));
			request.setLicnuKartuIzdao((String) postojeciPodaci.get("licnuKartuIzdao"));

			request.setAdresa((String) postojeciPodaci.get("adresa"));

			request.setAdresaStanovanja(kontaktPodaci.getAdresa());
			request.setBrojTelefonaMobilni(kontaktPodaci.getBrojTelefonaMobilni());
			request.setBrojTelefonaFiksni(kontaktPodaci.getBrojTelefonaFiksni());
			request.setEmailPrivatni(kontaktPodaci.getEmailPrivatni());
			request.setEmailFakultetski(kontaktPodaci.getEmailFakultetski());
			request.setMestoStanovanja(kontaktPodaci.getMestoStanovanja());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("update/" + kontaktPodaci.getStudentId()));
			ResponseEntity<Object> response = restTemplate.exchange(
					builder.toUriString(),
					HttpMethod.PUT,
					new HttpEntity<>(request, headers),
					Object.class
			);
			return response.getStatusCode() == HttpStatus.OK;
		} catch (Exception e) {
			System.err.println("Greška pri ažuriranju kontakt podataka: " + e.getMessage());
			return false;
		}
	}

    public List<StudentDTO> sviStudenti() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("all"));
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(builder.toUriString(), StudentDTO[].class, HttpMethod.GET);
        System.out.println("DEBUG sviStudenti: Response status: " + response.getStatusCode());
        if(response.getStatusCode()==HttpStatus.OK && response.getBody() != null) {
            System.out.println("DEBUG sviStudenti: Number of students: " + response.getBody().length);
            if (response.getBody().length > 0) {
                System.out.println("DEBUG sviStudenti: First student: " + response.getBody()[0].getIme() + " " + response.getBody()[0].getPrezime() + " " + response.getBody()[0].getGodinaUpisa());
            }
            return List.of(response.getBody());
        }
        else return null;
    }

    /**
     * Fetches optimized data for report generation.
     * Uses dedicated endpoint with single optimized query.
     */
    public List<StudentReportDTO> getStudentsForReport() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("report"));
        ResponseEntity<StudentReportDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(), StudentReportDTO[].class, HttpMethod.GET);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return List.of(response.getBody());
        }
        return List.of();
    }

    /**
     * Async reactive version using WebClient and Mono.
     * Returns Mono that can be subscribed to like in SearchStudentController example.
     */
    public Mono<List<StudentReportDTO>> getStudentsForReportAsync() {
        return webClient.get()
                .uri(createURL("report"))
                .retrieve()
                .bodyToMono(StudentReportDTO[].class)
                .map(array -> List.of(array));
    }

    public List<StudentDTO> searchByNameAndSurname(String ime, String prezime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("search"));
        
        // Normalize Serbian diacritics on client side before sending to server
        if (ime != null && !ime.trim().isEmpty()) {
            String normalizedIme = org.raflab.studsluzbadesktopclient.utils.StringUtils.normalizeSerbianDiacritics(ime);
            builder.queryParam("ime", normalizedIme);
        }
        if (prezime != null && !prezime.trim().isEmpty()) {
            String normalizedPrezime = org.raflab.studsluzbadesktopclient.utils.StringUtils.normalizeSerbianDiacritics(prezime);
            builder.queryParam("prezime", normalizedPrezime);
        }
        
        builder.queryParam("page", 0);
        builder.queryParam("size", 100);
        
        String url = builder.toUriString();
//        System.out.println("DEBUG: Search URL: " + url);
//        System.out.println("DEBUG: Ime parameter: '" + ime + "'");
//        System.out.println("DEBUG: Prezime parameter: '" + prezime + "'");
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
//        System.out.println("DEBUG: Response status: " + response.getStatusCode());
//        System.out.println("DEBUG: Response body: " + response.getBody());
        
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return List.of();

        Object contentObj = response.getBody().get("content");
//        System.out.println("DEBUG: Content object: " + contentObj);
//        System.out.println("DEBUG: Content is List: " + (contentObj instanceof List<?>));
        
        if (!(contentObj instanceof List<?> list))
            return List.of();
        
//        System.out.println("DEBUG: Number of results: " + list.size());

        List<StudentDTO> result = new ArrayList<>();
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) continue;

            StudentDTO dto = new StudentDTO();
            if (map.get("idIndeks") instanceof Number) dto.setIdIndeks(((Number) map.get("idIndeks")).longValue());
            if (map.get("idStudentPodaci") instanceof Number) dto.setIdStudentPodaci(((Number) map.get("idStudentPodaci")).longValue());
            if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
            if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
            if (map.get("godinaUpisa") instanceof Number) dto.setGodinaUpisa(((Number) map.get("godinaUpisa")).intValue());
            if (map.get("studProgramOznaka") instanceof String) dto.setStudProgramOznaka((String) map.get("studProgramOznaka"));
            if (map.get("broj") instanceof Number) dto.setBroj(((Number) map.get("broj")).intValue());
            if (map.get("aktivanIndeks") instanceof Boolean) dto.setAktivanIndeks((Boolean) map.get("aktivanIndeks"));

            result.add(dto);
        }
        return result;
    }

    public List<StudentDTO> searchByIndex(Integer broj, Integer godina, String studProgram) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("search"));
        
        if (broj != null) {
            builder.queryParam("broj", broj);
        }
        if (godina != null) {
            builder.queryParam("godina", godina);
        }
        if (studProgram != null && !studProgram.trim().isEmpty()) {
            builder.queryParam("studProgram", studProgram);
        }
        
        builder.queryParam("page", 0);
        builder.queryParam("size", 100);
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return List.of();

        Object contentObj = response.getBody().get("content");
        if (!(contentObj instanceof List<?> list))
            return List.of();

        List<StudentDTO> result = new ArrayList<>();
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) continue;

            StudentDTO dto = new StudentDTO();
            if (map.get("idIndeks") instanceof Number) dto.setIdIndeks(((Number) map.get("idIndeks")).longValue());
            if (map.get("idStudentPodaci") instanceof Number) dto.setIdStudentPodaci(((Number) map.get("idStudentPodaci")).longValue());
            if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
            if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
            if (map.get("godinaUpisa") instanceof Number) dto.setGodinaUpisa(((Number) map.get("godinaUpisa")).intValue());
            if (map.get("studProgramOznaka") instanceof String) dto.setStudProgramOznaka((String) map.get("studProgramOznaka"));
            if (map.get("broj") instanceof Number) dto.setBroj(((Number) map.get("broj")).intValue());
            if (map.get("aktivanIndeks") instanceof Boolean) dto.setAktivanIndeks((Boolean) map.get("aktivanIndeks"));

            result.add(dto);
        }
        return result;
    }

    public List<StudentBySrednjaSkolaDTO> searchByHighSchool(Long srednjaSkolaId) {
        if (srednjaSkolaId == null) {
            return List.of();
        }
        
        String url = createURL("srednja-skola/" + srednjaSkolaId);
        
        ResponseEntity<StudentBySrednjaSkolaDTO[]> response = restTemplate.getForEntity(
                url,
                StudentBySrednjaSkolaDTO[].class
        );
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return List.of(response.getBody());
        }
        return List.of();
    }



//      Asinhrona pretraga studenata po imenu i prezimenu.
//      Koristi WebClient za ne-blokirajući poziv - UI ostaje responzivan.

    public Mono<List<StudentDTO>> searchByNameAndSurnameAsync(String ime, String prezime) {
        String normalizedIme = (ime != null && !ime.trim().isEmpty()) 
            ? org.raflab.studsluzbadesktopclient.utils.StringUtils.normalizeSerbianDiacritics(ime) : null;
        String normalizedPrezime = (prezime != null && !prezime.trim().isEmpty()) 
            ? org.raflab.studsluzbadesktopclient.utils.StringUtils.normalizeSerbianDiacritics(prezime) : null;

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(STUDENT_URL_PATH + "/search");
                    if (normalizedIme != null) uriBuilder.queryParam("ime", normalizedIme);
                    if (normalizedPrezime != null) uriBuilder.queryParam("prezime", normalizedPrezime);
                    uriBuilder.queryParam("page", 0);
                    uriBuilder.queryParam("size", 100);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(this::parseStudentListFromPagedResponse)
                .onErrorReturn(List.of());
    }


    public Mono<List<StudentDTO>> searchByIndexAsync(Integer broj, Integer godina, String studProgram) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(STUDENT_URL_PATH + "/search");
                    if (broj != null) uriBuilder.queryParam("broj", broj);
                    if (godina != null) uriBuilder.queryParam("godina", godina);
                    if (studProgram != null && !studProgram.trim().isEmpty()) {
                        uriBuilder.queryParam("studProgram", studProgram);
                    }
                    uriBuilder.queryParam("page", 0);
                    uriBuilder.queryParam("size", 100);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(this::parseStudentListFromPagedResponse)
                .onErrorReturn(List.of());
    }


    public Mono<List<StudentBySrednjaSkolaDTO>> searchByHighSchoolAsync(Long srednjaSkolaId) {
        if (srednjaSkolaId == null) {
            return Mono.just(new ArrayList<StudentBySrednjaSkolaDTO>());
        }
        
        return webClient.get()
                .uri(STUDENT_URL_PATH + "/srednja-skola/" + srednjaSkolaId)
                .retrieve()
                .bodyToMono(StudentBySrednjaSkolaDTO[].class)
                .map(arr -> {
                    List<StudentBySrednjaSkolaDTO> result = new ArrayList<>();
                    if (arr != null) {
                        result.addAll(java.util.Arrays.asList(arr));
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<StudentBySrednjaSkolaDTO>());
    }


    private List<StudentDTO> parseStudentListFromPagedResponse(Map<String, Object> responseBody) {
        if (responseBody == null) return List.of();
        
        Object contentObj = responseBody.get("content");
        if (!(contentObj instanceof List<?> list)) return List.of();

        List<StudentDTO> result = new ArrayList<>();
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) continue;

            StudentDTO dto = new StudentDTO();
            if (map.get("idIndeks") instanceof Number) dto.setIdIndeks(((Number) map.get("idIndeks")).longValue());
            if (map.get("idStudentPodaci") instanceof Number) dto.setIdStudentPodaci(((Number) map.get("idStudentPodaci")).longValue());
            if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
            if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
            if (map.get("godinaUpisa") instanceof Number) dto.setGodinaUpisa(((Number) map.get("godinaUpisa")).intValue());
            if (map.get("studProgramOznaka") instanceof String) dto.setStudProgramOznaka((String) map.get("studProgramOznaka"));
            if (map.get("broj") instanceof Number) dto.setBroj(((Number) map.get("broj")).intValue());
            if (map.get("aktivanIndeks") instanceof Boolean) dto.setAktivanIndeks((Boolean) map.get("aktivanIndeks"));

            result.add(dto);
        }
        return result;
    }


    public Long saveStudentIndeks(StudentIndeksRequest indeksRequest) {
        if (indeksRequest.getStudentId() == null) {
            System.err.println("Student ID je obavezan za kreiranje indeksa");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("saveindeks"));
            
            ResponseEntity<Long> response = restTemplate.postForEntity(
                    builder.toUriString(), 
                    new HttpEntity<>(indeksRequest, headers), 
                    Long.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri kreiranju indeksa: " + e.getMessage());
            throw e;
        }
    }

    public Boolean aktivirajIndeks(Long studentIndeksId) {
        if (studentIndeksId == null) {
            System.err.println("Student indeks ID je obavezan za aktivaciju");
            return false;
        }

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("aktiviraj/" + studentIndeksId));
            
            ResponseEntity<Void> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.PUT,
                    null,
                    Void.class
            );
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Greška pri aktivaciji indeksa: " + e.getMessage());
            return false;
        }
    }

    public StudentProfileDTO getStudentProfile(Long studentPodaciId, Long indeksId) {
        if (studentPodaciId == null) {
            System.err.println("Student ID je obavezan za učitavanje profila");
            return null;
        }

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("podaci/" + studentPodaciId));
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                System.err.println("Student sa ID " + studentPodaciId + " ne postoji");
                return null;
            }

            Map<String, Object> data = response.getBody();
            StudentProfileDTO profile = new StudentProfileDTO();

            profile.setIdStudentPodaci(studentPodaciId);
            profile.setIdIndeks(indeksId);
            profile.setIme(getStringValue(data, "ime"));
            profile.setPrezime(getStringValue(data, "prezime"));
            profile.setSrednjeIme(getStringValue(data, "srednjeIme"));
            profile.setJmbg(getStringValue(data, "jmbg"));
            
            if (data.get("datumRodjenja") != null) {
                try {
                    profile.setDatumRodjenja(java.time.LocalDate.parse(data.get("datumRodjenja").toString()));
                } catch (Exception e) {
                    //
                }
            }
            
            profile.setMestoRodjenja(getStringValue(data, "mestoRodjenja"));
            profile.setDrzavaRodjenja(getStringValue(data, "drzavaRodjenja"));
            profile.setPol(getStringValue(data, "pol"));
            profile.setDrzavljanstvo(getStringValue(data, "drzavljanstvo"));
            profile.setNacionalnost(getStringValue(data, "nacionalnost"));
            profile.setBrojLicneKarte(getStringValue(data, "brojLicneKarte"));
            
            profile.setAdresa(getStringValue(data, "adresa"));
            profile.setMestoStanovanja(getStringValue(data, "mestoStanovanja"));
            profile.setEmailPrivatni(getStringValue(data, "emailPrivatni"));
            profile.setEmailFakultetski(getStringValue(data, "emailFakultetski"));
            profile.setBrojTelefona(getStringValue(data, "brojTelefonaMobilni"));

            if (indeksId != null) {
                try {
                    UriComponentsBuilder indeksBuilder = UriComponentsBuilder.fromHttpUrl(createURL("indeks/" + indeksId));
                    ResponseEntity<Map<String, Object>> indeksResponse = restTemplate.exchange(
                            indeksBuilder.toUriString(),
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {}
                    );
                    
                    if (indeksResponse.getStatusCode() == HttpStatus.OK && indeksResponse.getBody() != null) {
                        Map<String, Object> indeksData = indeksResponse.getBody();
                        profile.setStudijskiProgram(getStringValue(indeksData, "studProgramOznaka"));
                        if (indeksData.get("broj") instanceof Number) {
                            profile.setBrojIndeksa(((Number) indeksData.get("broj")).intValue());
                        }
                        if (indeksData.get("godina") instanceof Number) {
                            profile.setGodinaUpisa(((Number) indeksData.get("godina")).intValue());
                        }
                        profile.setNacinFinansiranja(getStringValue(indeksData, "nacinFinansiranja"));
                        if (indeksData.get("aktivan") instanceof Boolean) {
                            profile.setAktivanIndeks((Boolean) indeksData.get("aktivan"));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Greška pri učitavanju indeksa: " + e.getMessage());
                }
            }

            profile.setPolozeniIspiti(new ArrayList<>());
            profile.setNepolozeniIspiti(new ArrayList<>());
            profile.setUplate(new ArrayList<>());
            profile.setUpisi(new ArrayList<>());
            
            profile.setUkupnoEspb(0);
            profile.setProsecnaOcena(0.0);

            fetchStudentExams(profile, indeksId);
            fetchStudentPayments(profile, indeksId);
            fetchStudentEnrollments(profile, indeksId, profile.getBrojIndeksa());
            fetchStudentHighSchool(profile, studentPodaciId);

            return profile;
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju profila studenta: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void fetchStudentExams(StudentProfileDTO profile, Long indeksId) {
        if (indeksId == null) return;
        
        List<StudentExamDTO> polozeni = new ArrayList<>();
        List<StudentExamDTO> nepolozeni = new ArrayList<>();
        int totalEspb = 0;
        double totalOcena = 0;
        int polozeniCount = 0;
        
        // Fetch passed exams
        try {
            String passedUrl = baseUrl + "/api/izlazak-na-ispit/student-indeks/" + indeksId + "/passed";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(passedUrl)
                    .queryParam("page", 0)
                    .queryParam("size", 100);
            
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object content = response.getBody().get("content");
                if (content instanceof List<?>) {
                    for (Object item : (List<?>) content) {
                        if (item instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> exam = (Map<String, Object>) item;
                            StudentExamDTO dto = mapExamResponse(exam, true);
                            polozeni.add(dto);
                            if (dto.getEspb() != null) totalEspb += dto.getEspb();
                            if (dto.getOcena() != null) {
                                totalOcena += dto.getOcena();
                                polozeniCount++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("API za polo\u017eene ispite nije dostupan: " + e.getMessage());
        }
        
        // Fetch failed exams
        try {
            String failedUrl = baseUrl + "/api/izlazak-na-ispit/student-indeks/" + indeksId + "/failed";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(failedUrl)
                    .queryParam("page", 0)
                    .queryParam("size", 100);
            
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object content = response.getBody().get("content");
                if (content instanceof List<?>) {
                    for (Object item : (List<?>) content) {
                        if (item instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> exam = (Map<String, Object>) item;
                            StudentExamDTO dto = mapExamResponse(exam, false);
                            nepolozeni.add(dto);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("API za nepolo\u017eene ispite nije dostupan: " + e.getMessage());
        }
        
        profile.setPolozeniIspiti(polozeni);
        profile.setNepolozeniIspiti(nepolozeni);
        profile.setUkupnoEspb(totalEspb);
        profile.setProsecnaOcena(polozeniCount > 0 ? totalOcena / polozeniCount : 0.0);
    }
    
    private StudentExamDTO mapExamResponse(Map<String, Object> exam, boolean polozen) {
        StudentExamDTO dto = new StudentExamDTO();
        dto.setPredmetNaziv(getStringValue(exam, "predmetNaziv"));
        dto.setPredmetSifra(getStringValue(exam, "predmetSifra"));
        dto.setIspitniRok(getStringValue(exam, "ispitniRok"));
        
        if (exam.get("ocena") instanceof Number) {
            dto.setOcena(((Number) exam.get("ocena")).intValue());
        }
        if (exam.get("osvojeniPredispitniPoeni") instanceof Number) {
            dto.setPredispitneObaveze(((Number) exam.get("osvojeniPredispitniPoeni")).doubleValue());
        }
        if (exam.get("osvojeniIspitniPoeni") instanceof Number) {
            dto.setPoeniIspit(((Number) exam.get("osvojeniIspitniPoeni")).doubleValue());
        }
        if (exam.get("ukupnoPoena") instanceof Number) {
            dto.setUkupnoPoena(((Number) exam.get("ukupnoPoena")).doubleValue());
        }
        
        java.time.LocalDate datumPolaganja = null;
        if (exam.get("datumOdrzavanja") != null) {
            try {
                datumPolaganja = java.time.LocalDate.parse(exam.get("datumOdrzavanja").toString());
                dto.setDatumPolaganja(datumPolaganja);
            } catch (Exception e) { /* ignore */ }
        }
        
        // Map ESPB from server response
        if (exam.get("predmetEspb") instanceof Number) {
            dto.setEspb(((Number) exam.get("predmetEspb")).intValue());
        } else {
            dto.setEspb(6); // Default ESPB if not provided
        }
        
        // Calculate godinaStudija based on when the exam was taken (datumOdrzavanja - godinaUpisa)
        // This shows in which year of study the student actually took the exam
        if (datumPolaganja != null && exam.get("godina") instanceof Number) {
            int godinaUpisa = ((Number) exam.get("godina")).intValue();
            int godinaPolaganja = datumPolaganja.getYear();
            int godinaStudija = godinaPolaganja - godinaUpisa + 1;
            
            // Clamp to minimum 1
            if (godinaStudija < 1) godinaStudija = 1;
            
            dto.setGodinaStudija(godinaStudija);
        }
        
        dto.setPolozen(polozen);
        
        return dto;
    }

    private void fetchStudentPayments(StudentProfileDTO profile, Long indeksId) {
        if (indeksId == null) return;
        
        try {
            String url = baseUrl + "/api/uplata/student/" + indeksId;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<StudentPaymentDTO> uplate = new ArrayList<>();
                
                for (Map<String, Object> payment : response.getBody()) {
                    StudentPaymentDTO dto = new StudentPaymentDTO();
                    
                    // Map from UplataResponse fields
                    if (payment.get("id") instanceof Number) {
                        dto.setId(((Number) payment.get("id")).longValue());
                    }
                    if (payment.get("datumUplate") != null) {
                        try {
                            String dateStr = payment.get("datumUplate").toString();
                            if (dateStr.contains("T")) {
                                dto.setDatumUplate(java.time.LocalDate.parse(dateStr.substring(0, 10)));
                            } else {
                                dto.setDatumUplate(java.time.LocalDate.parse(dateStr));
                            }
                        } catch (Exception e) { /* ignore */ }
                    }
                    if (payment.get("iznosRsd") instanceof Number) {
                        dto.setIznos(java.math.BigDecimal.valueOf(((Number) payment.get("iznosRsd")).doubleValue()));
                    }
                    
                    // Map tip to svrhaUplate
                    Object tip = payment.get("tip");
                    if (tip != null) {
                        dto.setSvrhaUplate(tip.toString());
                    }
                    
                    // Map status
                    Object status = payment.get("status");
                    if (status != null) {
                        dto.setStatus(status.toString());
                    }
                    
                    dto.setNacinPlacanja(getStringValue(payment, "opis"));
                    
                    uplate.add(dto);
                }
                
                profile.setUplate(uplate);
            }
        } catch (Exception e) {
            System.err.println("API za uplate nije dostupan: " + e.getMessage());
        }
    }

    private void fetchStudentEnrollments(StudentProfileDTO profile, Long indeksId, Integer brojIndeksa) {
        List<StudentEnrollmentDTO> upisi = new ArrayList<>();
        
        // Fetch upisi (enrollments) by broj indeksa
        if (brojIndeksa != null) {
            try {
                String url = baseUrl + "/api/upis-godine/student/" + brojIndeksa;
                ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}
                );
                
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    for (Map<String, Object> enrollment : response.getBody()) {
                        StudentEnrollmentDTO dto = new StudentEnrollmentDTO();
                        dto.setTipUpisa("UPIS");
                        dto.setSkolskaGodina(getStringValue(enrollment, "skolskaGodinaNaziv"));
                        dto.setStudijskiProgram(profile.getStudijskiProgram());
                        dto.setNacinFinansiranja(profile.getNacinFinansiranja());
                        
                        if (enrollment.get("godina") instanceof Number) {
                            dto.setGodinaStudija(((Number) enrollment.get("godina")).intValue());
                        }
                        if (enrollment.get("prenetiESPB") instanceof Number) {
                            dto.setOstvareniEspb(((Number) enrollment.get("prenetiESPB")).intValue());
                        }
                        if (enrollment.get("datumUpisa") != null) {
                            try {
                                dto.setDatumUpisa(java.time.LocalDate.parse(enrollment.get("datumUpisa").toString()));
                            } catch (Exception e) { /* ignore */ }
                        }
                        dto.setAktivan(true);
                        
                        upisi.add(dto);
                    }
                }
            } catch (Exception e) {
                System.err.println("API za upise nije dostupan: " + e.getMessage());
            }
        }
        
        // Fetch obnove (renewals) by indeks ID
        if (indeksId != null) {
            try {
                String url = baseUrl + "/api/obnova-godine/student-indeks/" + indeksId;
                ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}
                );
                
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    for (Map<String, Object> renewal : response.getBody()) {
                        StudentEnrollmentDTO dto = new StudentEnrollmentDTO();
                        dto.setTipUpisa("OBNOVA");
                        dto.setSkolskaGodina(getStringValue(renewal, "skolskaGodinaNaziv"));
                        dto.setStudijskiProgram(getStringValue(renewal, "studProgramOznaka"));
                        
                        if (renewal.get("godina") instanceof Number) {
                            dto.setGodinaStudija(((Number) renewal.get("godina")).intValue());
                        }
                        if (renewal.get("prenetiESPB") instanceof Number) {
                            dto.setOstvareniEspb(((Number) renewal.get("prenetiESPB")).intValue());
                        }
                        if (renewal.get("datumUpisa") != null) {
                            try {
                                dto.setDatumUpisa(java.time.LocalDate.parse(renewal.get("datumUpisa").toString()));
                            } catch (Exception e) { /* ignore */ }
                        }
                        dto.setNacinFinansiranja(getStringValue(renewal, "razlogObnove"));
                        dto.setAktivan(false);
                        
                        upisi.add(dto);
                    }
                }
            } catch (Exception e) {
                System.err.println("API za obnove nije dostupan: " + e.getMessage());
            }
        }
        
        // Sort by date in ascending order (oldest first)
        upisi.sort((e1, e2) -> {
            if (e1.getDatumUpisa() == null && e2.getDatumUpisa() == null) return 0;
            if (e1.getDatumUpisa() == null) return 1;
            if (e2.getDatumUpisa() == null) return -1;
            return e1.getDatumUpisa().compareTo(e2.getDatumUpisa());
        });
        
        profile.setUpisi(upisi);
    }

    private void fetchStudentHighSchool(StudentProfileDTO profile, Long studentPodaciId) {
        if (studentPodaciId == null) return;
        
        try {
            String url = baseUrl + "/api/student-srednja-skola/student/" + studentPodaciId;
//            System.out.println("DEBUG: Fetching high school from: " + url);
            
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
//            System.out.println("DEBUG: High school response status: " + response.getStatusCode());
//            System.out.println("DEBUG: High school response body: " + response.getBody());
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().isEmpty()) {
                // Take the first high school record (most recent or primary)
                Map<String, Object> highSchool = response.getBody().get(0);
//                System.out.println("DEBUG: High school data: " + highSchool);
                
                String naziv = getStringValue(highSchool, "srednjaSkolaNaziv");
                String mesto = getStringValue(highSchool, "srednjaSkolaMesto");
//                System.out.println("DEBUG: Naziv: " + naziv + ", Mesto: " + mesto);
                
                profile.setSrednjaSkolaNaziv(naziv);
                profile.setSrednjaSkolaMesto(mesto);
                
                if (highSchool.get("uspehIzSrednjeSkole") instanceof Number) {
                    profile.setUspehSrednjaSkola(((Number) highSchool.get("uspehIzSrednjeSkole")).doubleValue());
                }
                if (highSchool.get("uspehSaPrijemnog") instanceof Number) {
                    profile.setUspehPrijemni(((Number) highSchool.get("uspehSaPrijemnog")).doubleValue());
                }
            } else {
                System.out.println("DEBUG: No high school data returned or empty response");
            }
        } catch (Exception e) {
            System.err.println("ERROR: API za srednju školu nije dostupan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value instanceof String ? (String) value : null;
    }

    public List<String> getAllStudijskiProgrami() {
        try {
            String url = createURL("studijski-programi");
            ResponseEntity<List<String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return List.of();
        } catch (Exception e) {
            System.err.println("Greška pri ucitavanju studijskih programa: " + e.getMessage());
            return List.of();
        }
    }

    public boolean dodajUplatu(Long studentIndeksId, Long skolskaGodinaId, double iznosRsd, String opis) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("studentIndeksId", studentIndeksId);
            request.put("skolskaGodinaId", skolskaGodinaId);
            request.put("iznosRsd", iznosRsd);
            request.put("tip", "SKOLARINA");
            request.put("opis", opis);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = baseUrl + "/api/uplata/add";
            ResponseEntity<Long> response = restTemplate.postForEntity(url, entity, Long.class);

            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null;
        } catch (Exception e) {
            System.err.println("Greška pri dodavanju uplate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Double> getPreostaliIznos(Long studentIndeksId) {
        try {
            String url = baseUrl + "/api/uplata/preostalo/" + studentIndeksId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Double> result = new HashMap<>();
                Map body = response.getBody();
                result.put("preostaloRSD", body.get("preostaloRSD") != null ? ((Number) body.get("preostaloRSD")).doubleValue() : 0.0);
                result.put("preostaloEUR", body.get("preostaloEUR") != null ? ((Number) body.get("preostaloEUR")).doubleValue() : 0.0);
                result.put("ukupnoUplacenoRSD", body.get("ukupnoUplacenoRSD") != null ? ((Number) body.get("ukupnoUplacenoRSD")).doubleValue() : 0.0);
                result.put("ukupnoUplacenoEUR", body.get("ukupnoUplacenoEUR") != null ? ((Number) body.get("ukupnoUplacenoEUR")).doubleValue() : 0.0);
                return result;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri dobijanju preostalog iznosa: " + e.getMessage());
            return null;
        }
    }

    public List<Map<String, Object>> getSkolskeGodine() {
        try {
            String url = baseUrl + "/api/skolska-godina/all";
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return List.of();
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju školskih godina: " + e.getMessage());
            return List.of();
        }
    }


    public Long createUpisGodine(Long studentIndeksId, Long skolskaGodinaId, Integer godina, Integer prenetiESPB) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("studentIndeksId", studentIndeksId);
            request.put("skolskaGodinaId", skolskaGodinaId);
            request.put("godina", godina);
            request.put("datumUpisa", java.time.LocalDate.now().toString());
            if (prenetiESPB != null) {
                request.put("prenetiESPB", prenetiESPB);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = baseUrl + "/api/upis-godine/add";
            ResponseEntity<Long> response = restTemplate.postForEntity(url, entity, Long.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri upisu godine: " + e.getMessage());
            throw new RuntimeException(extractErrorMessage(e), e);
        }
    }


    public Long createObnovaGodine(Long studentIndeksId, Long skolskaGodinaId, Integer godina, 
                                    Long prethodniUpisId, Integer prenetiESPB, String razlogObnove,
                                    List<Long> nextYearDrziPredmetIds) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("studentIndeksId", studentIndeksId);
            request.put("skolskaGodinaId", skolskaGodinaId);
            request.put("godina", godina);
            if (prethodniUpisId != null) {
                request.put("prethodniUpisId", prethodniUpisId);
            }
            if (prenetiESPB != null) {
                request.put("prenetiESPB", prenetiESPB);
            }
            if (razlogObnove != null && !razlogObnove.isEmpty()) {
                request.put("razlogObnove", razlogObnove);
            }
            if (nextYearDrziPredmetIds != null && !nextYearDrziPredmetIds.isEmpty()) {
                request.put("nextYearDrziPredmetIds", nextYearDrziPredmetIds);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = baseUrl + "/api/obnova-godine/obnovi";
            ResponseEntity<Long> response = restTemplate.postForEntity(url, entity, Long.class);

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri obnovi godine: " + e.getMessage());
            throw new RuntimeException(extractErrorMessage(e), e);
        }
    }


    public Map<String, Object> getFailedSubjects(Long studentIndeksId) {
        try {
            String url = baseUrl + "/api/obnova-godine/failed-subjects/" + studentIndeksId;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri dobijanju nepoloženih predmeta: " + e.getMessage());
            return null;
        }
    }


    public Integer getCurrentStudentYear(Long indeksId, Integer brojIndeksa) {
        if (brojIndeksa == null) return 1;
        
        try {
            String url = baseUrl + "/api/upis-godine/student/" + brojIndeksa;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                int maxGodina = 0;
                for (Map<String, Object> enrollment : response.getBody()) {
                    if (enrollment.get("godina") instanceof Number) {
                        int godina = ((Number) enrollment.get("godina")).intValue();
                        if (godina > maxGodina) {
                            maxGodina = godina;
                        }
                    }
                }
                return maxGodina > 0 ? maxGodina : 1;
            }
            return 1;
        } catch (Exception e) {
            System.err.println("Greška pri dobijanju trenutne godine studenta: " + e.getMessage());
            return 1;
        }
    }


    public Integer getPrenetiESPBForYear(Integer brojIndeksa, Integer godina) {
        if (brojIndeksa == null || godina == null) return 0;
        
        try {
            String url = baseUrl + "/api/upis-godine/student/" + brojIndeksa;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Find the most recent enrollment for the given year
                Map<String, Object> latestEnrollment = null;
                for (Map<String, Object> enrollment : response.getBody()) {
                    if (enrollment.get("godina") instanceof Number) {
                        int enrollmentGodina = ((Number) enrollment.get("godina")).intValue();
                        if (enrollmentGodina == godina) {
                            latestEnrollment = enrollment;
                            // Assuming list is ordered by date, take the last one
                        }
                    }
                }
                
                if (latestEnrollment != null && latestEnrollment.get("prenetiESPB") instanceof Number) {
                    return ((Number) latestEnrollment.get("prenetiESPB")).intValue();
                }
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Greška pri dobijanju prenetih ESPB: " + e.getMessage());
            return 0;
        }
    }

    private String extractErrorMessage(Exception e) {
        String message = e.getMessage();
        if (message != null && message.contains("\"message\":\"")) {
            int start = message.indexOf("\"message\":\"") + 11;
            int end = message.indexOf("\"", start);
            if (end > start) {
                return message.substring(start, end);
            }
        }
        if (message != null && message.contains(":")) {
            // Try to get meaningful part of error
            String[] parts = message.split(":");
            if (parts.length > 1) {
                return parts[parts.length - 1].trim().replace("\"", "").replace("}", "");
            }
        }
        return message != null ? message : "Nepoznata greška";
    }
}
