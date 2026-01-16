package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.IspitniRokDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IspitniRokService {

    private RestTemplate restTemplate;
    private String baseUrl;

    private static final String API_PATH = "/api/ispitni-rok";

    private String createURL(String pathEnd) {
        return baseUrl + API_PATH + "/" + pathEnd;
    }

    public List<IspitniRokDTO> getAll() {
        try {
            String url = baseUrl + API_PATH + "/all";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<IspitniRokDTO> result = new ArrayList<>();
                for (Map<String, Object> item : response.getBody()) {
                    result.add(mapToDTO(item));
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Error fetching ispitni rokovi: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public IspitniRokDTO getById(Long id) {
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
            System.err.println("Error fetching ispitni rok: " + e.getMessage());
        }
        return null;
    }

    public Long create(IspitniRokDTO dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = Map.of(
                    "datumPocetka", dto.getDatumPocetka().toString(),
                    "datumZavrsetka", dto.getDatumZavrsetka().toString(),
                    "tipRoka", dto.getTipRoka(),
                    "skolskaGodinaId", dto.getSkolskaGodinaId()
            );

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    createURL("add"),
                    new HttpEntity<>(request, headers),
                    Long.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error creating ispitni rok: " + e.getMessage());
            throw new RuntimeException("Greška pri kreiranju ispitnog roka: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Long id, IspitniRokDTO dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = Map.of(
                    "datumPocetka", dto.getDatumPocetka().toString(),
                    "datumZavrsetka", dto.getDatumZavrsetka().toString(),
                    "tipRoka", dto.getTipRoka(),
                    "skolskaGodinaId", dto.getSkolskaGodinaId()
            );

            ResponseEntity<Object> response = restTemplate.exchange(
                    createURL(id.toString()),
                    HttpMethod.PUT,
                    new HttpEntity<>(request, headers),
                    Object.class
            );

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Error updating ispitni rok: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(Long id) {
        try {
            restTemplate.delete(createURL(id.toString()));
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting ispitni rok: " + e.getMessage());
        }
        return false;
    }

    private IspitniRokDTO mapToDTO(Map<String, Object> data) {
        IspitniRokDTO dto = new IspitniRokDTO();
        
        if (data.get("id") instanceof Number) {
            dto.setId(((Number) data.get("id")).longValue());
        }
        if (data.get("datumPocetka") != null) {
            dto.setDatumPocetka(LocalDate.parse(data.get("datumPocetka").toString()));
        }
        if (data.get("datumZavrsetka") != null) {
            dto.setDatumZavrsetka(LocalDate.parse(data.get("datumZavrsetka").toString()));
        }
        if (data.get("tipRoka") != null) {
            dto.setTipRoka(data.get("tipRoka").toString());
        }
        if (data.get("skolskaGodinaId") instanceof Number) {
            dto.setSkolskaGodinaId(((Number) data.get("skolskaGodinaId")).longValue());
        }
        
        return dto;
    }

    /**
     * Fetches all school years from the server.
     */
    public List<Map<String, Object>> getAllSkolskeGodine() {
        try {
            String url = baseUrl + "/api/skolska-godina/all";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error fetching skolske godine: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
