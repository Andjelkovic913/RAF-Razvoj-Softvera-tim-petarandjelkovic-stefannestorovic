package org.raflab.studsluzbadesktopclient.services;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.NastavnikDTO;
import org.raflab.studsluzba.dtos.NastavnikProfileDTO;
import org.raflab.studsluzba.dtos.NastavnikReportDTO;
import org.raflab.studsluzba.dtos.NastavnikRequest;
import org.raflab.studsluzba.dtos.NastavnikZvanjeDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
public class NastavnikService {

    private RestTemplate restTemplate;
    private String baseUrl;

    private final String NASTAVNIK_URL_PATH = "/api/nastavnik";

    private String createURL(String pathEnd) {
        return baseUrl + NASTAVNIK_URL_PATH + "/" + pathEnd;
    }

    public List<NastavnikDTO> searchByNameAndSurname(String ime, String prezime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("search"));

        if (ime != null && !ime.trim().isEmpty()) {
            builder.queryParam("ime", ime);
        }
        if (prezime != null && !prezime.trim().isEmpty()) {
            builder.queryParam("prezime", prezime);
        }

        String url = builder.toUriString();

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return List.of();

        List<NastavnikDTO> result = new ArrayList<>();
        for (Map<String, Object> map : response.getBody()) {
            NastavnikDTO dto = new NastavnikDTO();
            if (map.get("id") instanceof Number) dto.setId(((Number) map.get("id")).longValue());
            if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
            if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
            if (map.get("srednjeIme") instanceof String) dto.setSrednjeIme((String) map.get("srednjeIme"));
            if (map.get("email") instanceof String) dto.setEmail((String) map.get("email"));
            if (map.get("brojTelefona") instanceof String) dto.setBrojTelefona((String) map.get("brojTelefona"));
            if (map.get("adresa") instanceof String) dto.setAdresa((String) map.get("adresa"));
            if (map.get("jmbg") instanceof String) dto.setJmbg((String) map.get("jmbg"));

            result.add(dto);
        }
        return result;
    }

    public List<NastavnikDTO> findAll() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("all"));

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return List.of();

        List<NastavnikDTO> result = new ArrayList<>();
        for (Map<String, Object> map : response.getBody()) {
            NastavnikDTO dto = new NastavnikDTO();
            if (map.get("id") instanceof Number) dto.setId(((Number) map.get("id")).longValue());
            if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
            if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
            if (map.get("srednjeIme") instanceof String) dto.setSrednjeIme((String) map.get("srednjeIme"));
            if (map.get("email") instanceof String) dto.setEmail((String) map.get("email"));
            if (map.get("brojTelefona") instanceof String) dto.setBrojTelefona((String) map.get("brojTelefona"));
            if (map.get("adresa") instanceof String) dto.setAdresa((String) map.get("adresa"));
            if (map.get("jmbg") instanceof String) dto.setJmbg((String) map.get("jmbg"));

            result.add(dto);
        }
        return result;
    }

    public List<NastavnikDTO> searchByZvanje(String zvanje) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("search/zvanje"));
        builder.queryParam("zvanje", zvanje);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
                return List.of();

            return mapToNastavnikDTOList(response.getBody());
        } catch (Exception e) {
            System.err.println("API za pretragu po zvanju nije dostupan: " + e.getMessage());
            return List.of();
        }
    }

    public List<NastavnikDTO> searchByEmail(String email) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("search/email"));
        builder.queryParam("email", email);

        try {
            String url = builder.encode(StandardCharsets.UTF_8).toUriString();
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
                return List.of();

            return mapToNastavnikDTOList(response.getBody());
        } catch (Exception e) {
            System.err.println("API za pretragu po emailu nije dostupan: " + e.getMessage());
            return List.of();
        }
    }

    public NastavnikProfileDTO getNastavnikProfile(Long nastavnikId) {
        if (nastavnikId == null) {
            return null;
        }

        try {
            String url = createURL(nastavnikId.toString());
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return null;
            }

            return mapToNastavnikProfileDTO(response.getBody());
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju profila nastavnika: " + e.getMessage());
            return null;
        }
    }

    private NastavnikProfileDTO mapToNastavnikProfileDTO(Map<String, Object> map) {
        NastavnikProfileDTO dto = new NastavnikProfileDTO();
        
        if (map.get("id") instanceof Number) dto.setId(((Number) map.get("id")).longValue());
        if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
        if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
        if (map.get("srednjeIme") instanceof String) dto.setSrednjeIme((String) map.get("srednjeIme"));
        if (map.get("email") instanceof String) dto.setEmail((String) map.get("email"));
        if (map.get("brojTelefona") instanceof String) dto.setBrojTelefona((String) map.get("brojTelefona"));
        if (map.get("adresa") instanceof String) dto.setAdresa((String) map.get("adresa"));
        if (map.get("jmbg") instanceof String) dto.setJmbg((String) map.get("jmbg"));
        if (map.get("trenutnoZvanje") instanceof String) dto.setTrenutnoZvanje((String) map.get("trenutnoZvanje"));
        
        if (map.get("datumRodjenja") != null) {
            try {
                dto.setDatumRodjenja(java.time.LocalDate.parse(map.get("datumRodjenja").toString()));
            } catch (Exception e) { /* ignore */ }
        }
        
        if (map.get("pol") != null) {
            dto.setPol(map.get("pol").toString());
        }
        
        // Map zvanja list
        if (map.get("zvanja") instanceof List<?>) {
            List<NastavnikZvanjeDTO> zvanjaList = new ArrayList<>();
            for (Object item : (List<?>) map.get("zvanja")) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> zvanjeMap = (Map<String, Object>) item;
                    NastavnikZvanjeDTO zvanjeDTO = new NastavnikZvanjeDTO();
                    
                    if (zvanjeMap.get("id") instanceof Number) {
                        zvanjeDTO.setId(((Number) zvanjeMap.get("id")).longValue());
                    }
                    if (zvanjeMap.get("zvanje") instanceof String) {
                        zvanjeDTO.setZvanje((String) zvanjeMap.get("zvanje"));
                    }
                    if (zvanjeMap.get("datumIzbora") != null) {
                        try {
                            zvanjeDTO.setDatumIzbora(java.time.LocalDate.parse(zvanjeMap.get("datumIzbora").toString()));
                        } catch (Exception e) { /* ignore */ }
                    }
                    if (zvanjeMap.get("datumPrestanka") != null) {
                        try {
                            zvanjeDTO.setDatumPrestanka(java.time.LocalDate.parse(zvanjeMap.get("datumPrestanka").toString()));
                        } catch (Exception e) { /* ignore */ }
                    }
                    if (zvanjeMap.get("aktivno") instanceof Boolean) {
                        zvanjeDTO.setAktivno((Boolean) zvanjeMap.get("aktivno"));
                    }
                    
                    zvanjaList.add(zvanjeDTO);
                }
            }
            dto.setZvanja(zvanjaList);
        }
        
        return dto;
    }

    public Long addNastavnik(NastavnikRequest request) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("add"));
            String url = builder.encode(StandardCharsets.UTF_8).toUriString();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<NastavnikRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<Long> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Long.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Greška pri dodavanju nastavnika: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateNastavnik(Long nastavnikId, NastavnikRequest request) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    baseUrl + NASTAVNIK_URL_PATH + "/nastavnik/update/" + nastavnikId);
            String url = builder.encode(StandardCharsets.UTF_8).toUriString();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<NastavnikRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Greška pri ažuriranju nastavnika: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private List<NastavnikDTO> mapToNastavnikDTOList(List<Map<String, Object>> body) {
        List<NastavnikDTO> result = new ArrayList<>();
        for (Map<String, Object> map : body) {
            NastavnikDTO dto = new NastavnikDTO();
            if (map.get("id") instanceof Number) dto.setId(((Number) map.get("id")).longValue());
            if (map.get("ime") instanceof String) dto.setIme((String) map.get("ime"));
            if (map.get("prezime") instanceof String) dto.setPrezime((String) map.get("prezime"));
            if (map.get("srednjeIme") instanceof String) dto.setSrednjeIme((String) map.get("srednjeIme"));
            if (map.get("email") instanceof String) dto.setEmail((String) map.get("email"));
            if (map.get("brojTelefona") instanceof String) dto.setBrojTelefona((String) map.get("brojTelefona"));
            if (map.get("adresa") instanceof String) dto.setAdresa((String) map.get("adresa"));
            if (map.get("jmbg") instanceof String) dto.setJmbg((String) map.get("jmbg"));
            if (map.get("trenutnoZvanje") instanceof String) dto.setZvanje((String) map.get("trenutnoZvanje"));
            result.add(dto);
        }
        return result;
    }

    /**
     * Fetches optimized data for Nastavnici report generation.
     */
    public List<NastavnikReportDTO> getNastavniciForReport() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("report"));
        ResponseEntity<NastavnikReportDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(), NastavnikReportDTO[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return List.of(response.getBody());
        }
        return List.of();
    }
}
