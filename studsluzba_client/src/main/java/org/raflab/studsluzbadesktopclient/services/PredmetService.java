package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.AverageGradeDTO;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.PredmetReportDTO;
import org.raflab.studsluzba.dtos.PredmetRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PredmetService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    private final String PREDMET_URL_PATH = "/api/predmet";

    private String createURL(String pathEnd) {
        return baseUrl + PREDMET_URL_PATH + "/" + pathEnd;
    }

    public List<PredmetDTO> getPredmetiByProgram(Long programId, Boolean obavezan) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("by-program/" + programId));
            
            if (obavezan != null) {
                builder.queryParam("obavezan", obavezan);
            }

            ResponseEntity<PredmetDTO[]> response = restTemplate.getForEntity(
                    builder.toUriString(),
                    PredmetDTO[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            return List.of();
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju predmeta: " + e.getMessage());
            return List.of();
        }
    }

    public List<PredmetDTO> getAllPredmeti() {
        try {
            ResponseEntity<PredmetDTO[]> response = restTemplate.getForEntity(
                    createURL("svi"),
                    PredmetDTO[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            return List.of();
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju svih predmeta: " + e.getMessage());
            return List.of();
        }
    }

    public PredmetDTO getPredmetById(Long id) {
        try {
            ResponseEntity<PredmetDTO> response = restTemplate.getForEntity(
                    createURL(String.valueOf(id)),
                    PredmetDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju predmeta: " + e.getMessage());
            return null;
        }
    }

    public Long addPredmet(PredmetRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    createURL("add"),
                    new HttpEntity<>(request, headers),
                    Long.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri dodavanju predmeta: " + e.getMessage());
            throw e;
        }
    }

    public PredmetDTO updatePredmet(Long id, PredmetRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<PredmetDTO> response = restTemplate.exchange(
                    createURL(String.valueOf(id)),
                    HttpMethod.PUT,
                    new HttpEntity<>(request, headers),
                    PredmetDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri ažuriranju predmeta: " + e.getMessage());
            throw e;
        }
    }

    public Boolean deletePredmet(Long id) {
        try {
            restTemplate.delete(createURL(String.valueOf(id)));
            return true;
        } catch (Exception e) {
            System.err.println("Greška pri brisanju predmeta: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> getAllDrziPredmet() {
        try {
            String url = baseUrl + "/api/drzipredmet/all";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju drzi predmet: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Fetches optimized data for Predmeti report generation by study program.
     */
    public List<PredmetReportDTO> getPredmetiForReport(String studProgramOznaka) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("report/" + studProgramOznaka));
        ResponseEntity<PredmetReportDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(), PredmetReportDTO[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return List.of(response.getBody());
        }
        return List.of();
    }

    /**
     * Izračunava prosečnu ocenu studenata na predmetu za raspon godina.
     * @param predmetId ID predmeta
     * @param startYear početna godina
     * @param endYear krajnja godina
     * @return AverageGradeDTO sa prosečnom ocenom i brojem položenih ispita
     */
    public AverageGradeDTO getAverageGradeByYearRange(Long predmetId, Integer startYear, Integer endYear) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL(predmetId + "/average-grade"));
            builder.queryParam("startYear", startYear);
            builder.queryParam("endYear", endYear);

            ResponseEntity<AverageGradeDTO> response = restTemplate.getForEntity(
                    builder.toUriString(),
                    AverageGradeDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri izračunavanju prosečne ocene: " + e.getMessage());
            throw new RuntimeException("Greška pri izračunavanju prosečne ocene: " + e.getMessage(), e);
        }
    }
}
