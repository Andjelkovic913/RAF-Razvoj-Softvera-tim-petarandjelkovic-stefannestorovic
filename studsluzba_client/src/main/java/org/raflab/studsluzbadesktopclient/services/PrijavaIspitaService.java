package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.IzlazakNaIspitDTO;
import org.raflab.studsluzba.dtos.PrijavaIspitaDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PrijavaIspitaService {

    private RestTemplate restTemplate;
    private String baseUrl;

    private static final String PRIJAVA_API_PATH = "/api/prijava-ispita";
    private static final String IZLAZAK_API_PATH = "/api/izlazak-na-ispit";

    /**
     * Creates a new exam registration (prijava ispita).
     * Server-side validation checks if student is eligible.
     */
    public Long prijaviIspit(Long studentIndeksId, Long ispitId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            request.put("studentIndeksId", studentIndeksId);
            request.put("ispitId", ispitId);
            request.put("datumPrijave", LocalDate.now().toString());

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    baseUrl + PRIJAVA_API_PATH + "/add",
                    new HttpEntity<>(request, headers),
                    Long.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error registering for exam: " + errorMessage);
            throw new RuntimeException(extractErrorMessage(errorMessage));
        } catch (Exception e) {
            System.err.println("Error registering for exam: " + e.getMessage());
            throw new RuntimeException("Greška pri prijavi ispita: " + e.getMessage());
        }
        return null;
    }

    /**
     * Creates exam result (izlazak na ispit) with points and grade.
     */
    public Long unosRezultata(IzlazakNaIspitDTO dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            // Server expects prijavaIspitaId, NOT ispitId!
            request.put("prijavaIspitaId", dto.getPrijavaIspitaId());
            request.put("slusaPredmetId", dto.getSlusaPredmetId());
            request.put("osvojeniPredispitniPoeni", dto.getOsvojeniPredispitniPoeni());
            request.put("osvojeniIspitniPoeni", dto.getOsvojeniIspitniPoeni());
            // Calculate total points
            int ukupno = (dto.getOsvojeniPredispitniPoeni() != null ? dto.getOsvojeniPredispitniPoeni() : 0) 
                       + (dto.getOsvojeniIspitniPoeni() != null ? dto.getOsvojeniIspitniPoeni() : 0);
            request.put("ukupnoPoena", ukupno);
            request.put("ocena", dto.getOcena());
            request.put("ponistio", dto.isPonistio());
            if (dto.getNapomena() != null) {
                request.put("napomena", dto.getNapomena());
            }

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    baseUrl + IZLAZAK_API_PATH,
                    new HttpEntity<>(request, headers),
                    Long.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error entering exam result: " + errorMessage);
            throw new RuntimeException(extractErrorMessage(errorMessage));
        } catch (Exception e) {
            System.err.println("Error entering exam result: " + e.getMessage());
            throw new RuntimeException("Greška pri unosu rezultata: " + e.getMessage());
        }
        return null;
    }

    /**
     * Gets all exam results, optionally filtered by exam.
     */
    public List<IzlazakNaIspitDTO> getRezultatiByIspit(Long ispitId, int page, int size) {
        try {
            String url = baseUrl + IZLAZAK_API_PATH + "/all?page=" + page + "&size=" + size;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object content = response.getBody().get("content");
                if (content instanceof List<?>) {
                    List<IzlazakNaIspitDTO> result = new ArrayList<>();
                    for (Object item : (List<?>) content) {
                        if (item instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> data = (Map<String, Object>) item;
                            IzlazakNaIspitDTO dto = mapToIzlazakDTO(data);
                            // Filter by ispitId if specified
                            if (ispitId == null || ispitId.equals(dto.getIspitId())) {
                                result.add(dto);
                            }
                        }
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching exam results: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Gets the number of attempts for a student on a specific subject.
     */
    public Long getBrojPokusaja(Long studentIndeksId, Long predmetId) {
        try {
            String url = baseUrl + IZLAZAK_API_PATH + "/student/" + studentIndeksId + "/predmet/" + predmetId + "/broj-pokusaja";
            ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error fetching attempt count: " + e.getMessage());
        }
        return 0L;
    }

    private IzlazakNaIspitDTO mapToIzlazakDTO(Map<String, Object> data) {
        IzlazakNaIspitDTO dto = new IzlazakNaIspitDTO();

        if (data.get("id") instanceof Number) {
            dto.setId(((Number) data.get("id")).longValue());
        }
        if (data.get("ispitId") instanceof Number) {
            dto.setIspitId(((Number) data.get("ispitId")).longValue());
        }
        if (data.get("studentIndeksId") instanceof Number) {
            dto.setStudentIndeksId(((Number) data.get("studentIndeksId")).longValue());
        }

        // Student name - try multiple possible field names from server
        String ime = (String) data.get("ime");
        if (ime == null) ime = (String) data.get("studentIme");
        dto.setStudentIme(ime);
        
        String prezime = (String) data.get("prezime");
        if (prezime == null) prezime = (String) data.get("studentPrezime");
        dto.setStudentPrezime(prezime);

        // Predmet info
        dto.setPredmetNaziv((String) data.get("predmetNaziv"));
        dto.setPredmetSifra((String) data.get("predmetSifra"));
        if (data.get("datumOdrzavanja") != null) {
            try {
                dto.setDatumOdrzavanja(LocalDate.parse(data.get("datumOdrzavanja").toString()));
            } catch (Exception e) { /* ignore */ }
        }

        if (data.get("broj") instanceof Number) {
            dto.setBrojIndeksa(((Number) data.get("broj")).intValue());
        }
        if (data.get("godina") instanceof Number) {
            dto.setGodinaIndeksa(((Number) data.get("godina")).intValue());
        }
        dto.setStudProgramOznaka((String) data.get("studProgramOznaka"));

        // Results
        if (data.get("osvojeniPredispitniPoeni") instanceof Number) {
            dto.setOsvojeniPredispitniPoeni(((Number) data.get("osvojeniPredispitniPoeni")).intValue());
        }
        if (data.get("osvojeniIspitniPoeni") instanceof Number) {
            dto.setOsvojeniIspitniPoeni(((Number) data.get("osvojeniIspitniPoeni")).intValue());
        }
        if (data.get("ukupnoPoena") instanceof Number) {
            dto.setUkupnoPoena(((Number) data.get("ukupnoPoena")).intValue());
        }
        if (data.get("ocena") instanceof Number) {
            dto.setOcena(((Number) data.get("ocena")).intValue());
        }
        if (data.get("ponistio") instanceof Boolean) {
            dto.setPonistio((Boolean) data.get("ponistio"));
        }
        dto.setNapomena((String) data.get("napomena"));

        return dto;
    }

    private String extractErrorMessage(String responseBody) {
        try {
            if (responseBody.contains("message")) {
                int start = responseBody.indexOf("\"message\":\"") + 11;
                int end = responseBody.indexOf("\"", start);
                if (start > 10 && end > start) {
                    return responseBody.substring(start, end);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return responseBody;
    }
}
