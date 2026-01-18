package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.AverageGradeDTO;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.PredmetReportDTO;
import org.raflab.studsluzba.dtos.PredmetRequest;
import org.raflab.studsluzba.dtos.YearlyGradeStatsDTO;
import org.raflab.studsluzba.dtos.PredmetGradeStatsDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PredmetService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
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


//    public List<PredmetReportDTO> getPredmetiForReport(String studProgramOznaka) {
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("report/" + studProgramOznaka));
//        ResponseEntity<PredmetReportDTO[]> response = restTemplate.getForEntity(
//                builder.toUriString(), PredmetReportDTO[].class);
//        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//            return List.of(response.getBody());
//        }
//        return List.of();
//    }


    public Mono<List<PredmetDTO>> getAllPredmetiAsync() {
        return webClient.get()
                .uri(PREDMET_URL_PATH + "/svi")
                .retrieve()
                .bodyToMono(PredmetDTO[].class)
                .map(arr -> {
                    List<PredmetDTO> result = new ArrayList<>();
                    if (arr != null) {
                        result.addAll(Arrays.asList(arr));
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<>());
    }


    public Mono<List<PredmetDTO>> getPredmetiByProgramAsync(Long programId, Boolean obavezan) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(PREDMET_URL_PATH + "/by-program/" + programId);
                    if (obavezan != null) {
                        uriBuilder.queryParam("obavezan", obavezan);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(PredmetDTO[].class)
                .map(arr -> {
                    List<PredmetDTO> result = new ArrayList<>();
                    if (arr != null) {
                        result.addAll(Arrays.asList(arr));
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<>());
    }


     //asinhrono dohvatanje statistike ocena za sve predmete
    public Mono<List<PredmetGradeStatsDTO>> getAllPredmetiGradeStatsAsync(Integer startYear, Integer endYear) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PREDMET_URL_PATH + "/all-stats")
                        .queryParam("startYear", startYear)
                        .queryParam("endYear", endYear)
                        .build())
                .retrieve() // ceka odgovor
                .bodyToMono(PredmetGradeStatsDTO[].class)
                .map(arr -> {
                    List<PredmetGradeStatsDTO> result = new ArrayList<>();
                    if (arr != null) {
                        result.addAll(Arrays.asList(arr));
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<>()); // ne puca nego prazna lista
    }


    public Mono<List<YearlyGradeStatsDTO>> getYearlyGradeStatsAsync(Long predmetId, Integer startYear, Integer endYear) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PREDMET_URL_PATH + "/" + predmetId + "/yearly-stats")
                        .queryParam("startYear", startYear)
                        .queryParam("endYear", endYear)
                        .build())
                .retrieve()
                .bodyToMono(YearlyGradeStatsDTO[].class)
                .map(arr -> {
                    List<YearlyGradeStatsDTO> result = new ArrayList<>();
                    if (arr != null) {
                        result.addAll(Arrays.asList(arr));
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<>());
    }


    public Mono<AverageGradeDTO> getAverageGradeByYearRangeAsync(Long predmetId, Integer startYear, Integer endYear) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PREDMET_URL_PATH + "/" + predmetId + "/average-grade")
                        .queryParam("startYear", startYear)
                        .queryParam("endYear", endYear)
                        .build())
                .retrieve()
                .bodyToMono(AverageGradeDTO.class)
                .onErrorResume(e -> Mono.empty());
    }
}
