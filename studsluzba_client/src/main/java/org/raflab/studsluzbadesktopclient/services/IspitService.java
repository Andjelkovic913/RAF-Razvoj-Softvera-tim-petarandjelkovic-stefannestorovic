package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.IspitDTO;
import org.raflab.studsluzba.dtos.PrijavljeniStudentDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IspitService {

    private RestTemplate restTemplate;
    private WebClient webClient;
    private String baseUrl;

    private static final String API_PATH = "/api/ispit";
    private static final String PRIJAVA_API_PATH = "/api/prijava-ispita";

    private String createURL(String pathEnd) {
        return baseUrl + API_PATH + "/" + pathEnd;
    }

    public List<IspitDTO> getAll() {
        try {
            String url = baseUrl + API_PATH + "/all";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<IspitDTO> result = new ArrayList<>();
                for (Map<String, Object> item : response.getBody()) {
                    result.add(mapToDTO(item));
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Error fetching ispiti: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<IspitDTO> getByIspitniRok(Long ispitniRokId) {
        List<IspitDTO> allIspiti = getAll();
        return allIspiti.stream()
                .filter(i -> ispitniRokId.equals(i.getIspitniRokId()))
                .toList();
    }

    public IspitDTO getById(Long id) {
        try {
            String url = createURL(id.toString());
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return mapToDTO(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Error fetching ispit: " + e.getMessage());
        }
        return null;
    }

    public Long create(IspitDTO dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            request.put("datumOdrzavanja", dto.getDatumOdrzavanja().toString());
            request.put("vremePocetka", dto.getVremePocetka().toString());
            request.put("vremeZavrsetka", dto.getVremeZavrsetka().toString());
            request.put("drziPredmetId", dto.getDrziPredmetId());
            request.put("predmetId", dto.getPredmetId());  // Server requires this!
            request.put("ispitniRokId", dto.getIspitniRokId());
            request.put("skolskaGodinaId", dto.getSkolskaGodinaId());  // Server requires this!

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    createURL("add"),
                    new HttpEntity<>(request, headers),
                    Long.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error creating ispit: " + e.getMessage());
            throw new RuntimeException("Greška pri kreiranju ispita: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Long id, IspitDTO dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            request.put("datumOdrzavanja", dto.getDatumOdrzavanja().toString());
            if (dto.getVremePocetka() != null) {
                request.put("vremePocetka", dto.getVremePocetka().toString());
            }
            if (dto.getVremeZavrsetka() != null) {
                request.put("vremeZavrsetka", dto.getVremeZavrsetka().toString());
            }
            request.put("drziPredmetId", dto.getDrziPredmetId());
            request.put("ispitniRokId", dto.getIspitniRokId());

            ResponseEntity<Object> response = restTemplate.exchange(
                    createURL(id.toString()),
                    HttpMethod.PUT,
                    new HttpEntity<>(request, headers),
                    Object.class
            );

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Error updating ispit: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(Long id) {
        try {
            restTemplate.delete(createURL(id.toString()));
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting ispit: " + e.getMessage());
        }
        return false;
    }

    public List<PrijavljeniStudentDTO> getPrijavljeniStudenti(Long ispitId) {
        try {
            String url = baseUrl + PRIJAVA_API_PATH + "/" + ispitId + "/students";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<PrijavljeniStudentDTO> result = new ArrayList<>();
                for (Map<String, Object> item : response.getBody()) {
                    System.out.println("DEBUG: Prijavljeni student data: " + item);
                    PrijavljeniStudentDTO dto = new PrijavljeniStudentDTO();
                    if (item.get("prijavaId") instanceof Number) {
                        dto.setPrijavaId(((Number) item.get("prijavaId")).longValue());
                    } else {
                        System.out.println("WARNING: prijavaId is null or not a Number for student: " + item.get("ime") + " " + item.get("prezime"));
                    }
                    if (item.get("studentIndeksId") instanceof Number) {
                        dto.setStudentIndeksId(((Number) item.get("studentIndeksId")).longValue());
                    }
                    if (item.get("slusaPredmetId") instanceof Number) {
                        dto.setSlusaPredmetId(((Number) item.get("slusaPredmetId")).longValue());
                    }
                    dto.setIme((String) item.get("ime"));
                    dto.setPrezime((String) item.get("prezime"));
                    if (item.get("broj") instanceof Number) {
                        dto.setBroj(((Number) item.get("broj")).intValue());
                    }
                    if (item.get("godina") instanceof Number) {
                        dto.setGodina(((Number) item.get("godina")).intValue());
                    }
                    dto.setStudProgramOznaka((String) item.get("studProgramOznaka"));
                    if (item.get("predispitneObaveze") instanceof Number) {
                        dto.setPredispitneObaveze(((Number) item.get("predispitneObaveze")).doubleValue());
                    }
                    result.add(dto);
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Error fetching prijavljeni studenti: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private IspitDTO mapToDTO(Map<String, Object> data) {
        IspitDTO dto = new IspitDTO();

        if (data.get("id") instanceof Number) {
            dto.setId(((Number) data.get("id")).longValue());
        }
        if (data.get("datumOdrzavanja") != null) {
            dto.setDatumOdrzavanja(LocalDate.parse(data.get("datumOdrzavanja").toString()));
        }
        if (data.get("vremePocetka") != null) {
            try {
                dto.setVremePocetka(LocalTime.parse(data.get("vremePocetka").toString()));
            } catch (Exception e) { /* ignore */ }
        }
        if (data.get("vremeZavrsetka") != null) {
            try {
                dto.setVremeZavrsetka(LocalTime.parse(data.get("vremeZavrsetka").toString()));
            } catch (Exception e) { /* ignore */ }
        }
        if (data.get("ispitniRokId") instanceof Number) {
            dto.setIspitniRokId(((Number) data.get("ispitniRokId")).longValue());
        }
        if (data.get("skolskaGodinaId") instanceof Number) {
            dto.setSkolskaGodinaId(((Number) data.get("skolskaGodinaId")).longValue());
        }
        if (data.get("zakljucen") instanceof Boolean) {
            dto.setZakljucen((Boolean) data.get("zakljucen"));
        }

        // Extract predmet info from nested objects
        Object predmetObj = data.get("predmet");
        if (predmetObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> predmet = (Map<String, Object>) predmetObj;
            dto.setPredmetNaziv((String) predmet.get("naziv"));
            dto.setPredmetSifra((String) predmet.get("sifra"));
        }

        Object drziPredmetObj = data.get("drziPredmet");
        if (drziPredmetObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> drziPredmet = (Map<String, Object>) drziPredmetObj;
            if (drziPredmet.get("id") instanceof Number) {
                dto.setDrziPredmetId(((Number) drziPredmet.get("id")).longValue());
            }
            dto.setNastavnikIme((String) drziPredmet.get("nastavnikIme"));
        }

        return dto;
    }

    // ==================== ASINHRONE METODE (WebClient) ====================

    /**
     * Asinhrono dohvatanje svih ispita.
     */
    public Mono<List<IspitDTO>> getAllAsync() {
        return webClient.get()
                .uri(API_PATH + "/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(list -> {
                    List<IspitDTO> result = new ArrayList<>();
                    if (list != null) {
                        for (Map<String, Object> item : list) {
                            result.add(mapToDTO(item));
                        }
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<>());
    }

    /**
     * Asinhrono dohvatanje ispita po ispitnom roku.
     */
    public Mono<List<IspitDTO>> getByIspitniRokAsync(Long ispitniRokId) {
        return getAllAsync()
                .map(list -> list.stream()
                        .filter(i -> ispitniRokId.equals(i.getIspitniRokId()))
                        .toList());
    }

    /**
     * Asinhrono dohvatanje prijavljenih studenata za ispit.
     */
    public Mono<List<PrijavljeniStudentDTO>> getPrijavljeniStudentiAsync(Long ispitId) {
        return webClient.get()
                .uri(PRIJAVA_API_PATH + "/" + ispitId + "/students")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(list -> {
                    List<PrijavljeniStudentDTO> result = new ArrayList<>();
                    if (list != null) {
                        for (Map<String, Object> item : list) {
                            PrijavljeniStudentDTO dto = new PrijavljeniStudentDTO();
                            if (item.get("prijavaId") instanceof Number) {
                                dto.setPrijavaId(((Number) item.get("prijavaId")).longValue());
                            }
                            if (item.get("studentIndeksId") instanceof Number) {
                                dto.setStudentIndeksId(((Number) item.get("studentIndeksId")).longValue());
                            }
                            if (item.get("slusaPredmetId") instanceof Number) {
                                dto.setSlusaPredmetId(((Number) item.get("slusaPredmetId")).longValue());
                            }
                            dto.setIme((String) item.get("ime"));
                            dto.setPrezime((String) item.get("prezime"));
                            if (item.get("broj") instanceof Number) {
                                dto.setBroj(((Number) item.get("broj")).intValue());
                            }
                            if (item.get("godina") instanceof Number) {
                                dto.setGodina(((Number) item.get("godina")).intValue());
                            }
                            dto.setStudProgramOznaka((String) item.get("studProgramOznaka"));
                            if (item.get("predispitneObaveze") instanceof Number) {
                                dto.setPredispitneObaveze(((Number) item.get("predispitneObaveze")).doubleValue());
                            }
                            result.add(dto);
                        }
                    }
                    return result;
                })
                .onErrorReturn(new ArrayList<>());
    }
}
