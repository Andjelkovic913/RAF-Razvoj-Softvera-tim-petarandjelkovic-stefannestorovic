package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SlusaPredmetService {

    private RestTemplate restTemplate;
    private String baseUrl;

    private static final String SLUSA_PREDMET_API_PATH = "/api/slusapredmet";

    /**
     * Prijavljuje studenta na predmet (kreira SlusaPredmet vezu).
     * 
     * @param studentIndeksId ID student indeksa
     * @param drziPredmetId ID DrziPredmet (predmet + nastavnik)
     * @return ID kreiranog SlusaPredmet zapisa
     */
    public Long prijaviStudentaNaPredmet(Long studentIndeksId, Long drziPredmetId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            request.put("studentIndeksId", studentIndeksId);
            request.put("drziPredmetId", drziPredmetId);
            // upisGodineId je opciono, ne šaljemo ga

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    baseUrl + SLUSA_PREDMET_API_PATH + "/add",
                    new HttpEntity<>(request, headers),
                    Long.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error enrolling student to subject: " + errorMessage);
            throw new RuntimeException("Greška pri prijavi studenta na predmet: " + extractErrorMessage(errorMessage));
        } catch (Exception e) {
            System.err.println("Error enrolling student to subject: " + e.getMessage());
            throw new RuntimeException("Greška pri prijavi studenta na predmet: " + e.getMessage());
        }
        return null;
    }

    /**
     * Vraća listu studenata koji slušaju predmet za dati DrziPredmet ID.
     * Koristi endpoint iz RaspodelaNastaveController.
     */
    public List<org.raflab.studsluzba.dtos.StudentDTO> getStudentiZaDrziPredmet(Long drziPredmetId) {
        try {
            String url = baseUrl + "/api/professor/raspodelanastave/slusapredmetaktivna/" + drziPredmetId;
            
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<org.raflab.studsluzba.dtos.StudentDTO> result = new ArrayList<>();
                for (Map<String, Object> item : response.getBody()) {
                    org.raflab.studsluzba.dtos.StudentDTO dto = new org.raflab.studsluzba.dtos.StudentDTO();
                    
                    // Map StudentIndeks fields
                    if (item.get("id") instanceof Number) {
                        dto.setIdIndeks(((Number) item.get("id")).longValue());
                    }
                    if (item.get("broj") instanceof Number) {
                        dto.setBroj(((Number) item.get("broj")).intValue());
                    }
                    if (item.get("godina") instanceof Number) {
                        dto.setGodinaUpisa(((Number) item.get("godina")).intValue());
                    }
                    dto.setStudProgramOznaka(getStringValue(item, "studProgramOznaka"));
                    if (item.get("aktivan") instanceof Boolean) {
                        dto.setAktivanIndeks((Boolean) item.get("aktivan"));
                    }
                    
                    // Extract student data from nested "student" object
                    if (item.get("student") instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> student = (Map<String, Object>) item.get("student");
                        dto.setIme(getStringValue(student, "ime"));
                        dto.setPrezime(getStringValue(student, "prezime"));
                    }
                    
                    result.add(dto);
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Error fetching students for DrziPredmet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Greška pri učitavanju studenata: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private String extractErrorMessage(String errorBody) {
        try {
            if (errorBody.contains("message")) {
                int start = errorBody.indexOf("message") + 10;
                int end = errorBody.indexOf("\"", start);
                if (end > start) {
                    return errorBody.substring(start, end);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return errorBody;
    }
}
