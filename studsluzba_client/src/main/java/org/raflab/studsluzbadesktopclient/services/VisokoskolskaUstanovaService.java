package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.VisokoskolskaUstanovaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@AllArgsConstructor
public class VisokoskolskaUstanovaService {

    private RestTemplate restTemplate;
    private String baseUrl;

    private final String VISOKOSKOLSKA_USTANOVA_URL_PATH = "/api/visokoskolska-ustanova";

    private String createURL(String pathEnd) {
        return baseUrl + VISOKOSKOLSKA_USTANOVA_URL_PATH + "/" + pathEnd;
    }

    public List<VisokoskolskaUstanovaDTO> getAllVisokoskolskeUstanove() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("all"));
        ResponseEntity<VisokoskolskaUstanovaDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(),
                VisokoskolskaUstanovaDTO[].class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return List.of(response.getBody());
        }
        return List.of();
    }

    public VisokoskolskaUstanovaDTO getVisokoskolskaUstanovaById(Long id) {
        if (id == null) {
            return null;
        }

        String url = createURL(String.valueOf(id));
        ResponseEntity<VisokoskolskaUstanovaDTO> response = restTemplate.getForEntity(
                url,
                VisokoskolskaUstanovaDTO.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public VisokoskolskaUstanovaDTO createVisokoskolskaUstanova(java.util.Map<String, String> request) {
        String url = baseUrl + VISOKOSKOLSKA_USTANOVA_URL_PATH + "/add";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<java.util.Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<VisokoskolskaUstanovaDTO> response = restTemplate.postForEntity(
                url,
                entity,
                VisokoskolskaUstanovaDTO.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        return null;
    }
}
