package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.SrednjaSkolaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@AllArgsConstructor
public class SrednjaSkolaService {

    private RestTemplate restTemplate;
    private String baseUrl;

    private final String SREDNJA_SKOLA_URL_PATH = "/api/srednja-skola";

    private String createURL(String pathEnd) {
        return baseUrl + SREDNJA_SKOLA_URL_PATH + "/" + pathEnd;
    }

    public List<SrednjaSkolaDTO> getAllSrednjeSkole() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("all"));
        ResponseEntity<SrednjaSkolaDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(),
                SrednjaSkolaDTO[].class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return List.of(response.getBody());
        }
        return List.of();
    }

    public SrednjaSkolaDTO getSrednjaSkolaById(Long id) {
        if (id == null) {
            return null;
        }

        String url = createURL(String.valueOf(id));
        ResponseEntity<SrednjaSkolaDTO> response = restTemplate.getForEntity(
                url,
                SrednjaSkolaDTO.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }
}
