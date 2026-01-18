package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.DrziPredmetDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DrziPredmetService {

    private RestTemplate restTemplate;
    private String baseUrl;

    /**
     * Vraća sve DrziPredmet zapise za dati predmet (svi nastavnici koji drže taj predmet).
     * Koristi /api/drzipredmet/all i filtrira po subjectId na klijentu.
     */
    public List<DrziPredmetDTO> getDrziPredmetByPredmetId(Long predmetId) {
        try {
            String url = baseUrl + "/api/drzipredmet/all";
            
            ResponseEntity<List<DrziPredmetDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DrziPredmetDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<DrziPredmetDTO> result = new ArrayList<>();
                for (DrziPredmetDTO dto : response.getBody()) {
                    // Filter by subject id
                    if (dto.getSubject() != null && dto.getSubject().getId() != null && 
                        dto.getSubject().getId().equals(predmetId.intValue())) {
                        result.add(dto);
                    }
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Error fetching DrziPredmet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Greška pri učitavanju nastavnika za predmet: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
