package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzbadesktopclient.dtos.DrziPredmetDTO;
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
     * Koristi /api/drzipredmet/all i filtrira po predmetId na klijentu.
     */
    public List<DrziPredmetDTO> getDrziPredmetByPredmetId(Long predmetId) {
        try {
            String url = baseUrl + "/api/drzipredmet/all";
            
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<DrziPredmetDTO> result = new ArrayList<>();
                for (Map<String, Object> item : response.getBody()) {
                    // Filter by predmetId
                    Long itemPredmetId = null;
                    if (item.get("predmetId") instanceof Number) {
                        itemPredmetId = ((Number) item.get("predmetId")).longValue();
                    }
                    
                    if (itemPredmetId == null || !itemPredmetId.equals(predmetId)) {
                        continue; // Skip if not matching predmet
                    }
                    
                    DrziPredmetDTO dto = new DrziPredmetDTO();
                    
                    if (item.get("id") instanceof Number) {
                        dto.setId(((Number) item.get("id")).longValue());
                    }
                    dto.setPredmetId(itemPredmetId);
                    dto.setPredmetOznaka(getStringValue(item, "predmetOznaka"));
                    dto.setPredmetNaziv(getStringValue(item, "predmetNaziv"));
                    
                    if (item.get("nastavnikId") instanceof Number) {
                        dto.setNastavnikId(((Number) item.get("nastavnikId")).longValue());
                    }
                    dto.setNastavnikIme(getStringValue(item, "nastavnikIme"));
                    dto.setNastavnikPrezime(getStringValue(item, "nastavnikPrezime"));
                    
                    if (item.get("skolskaGodinaId") instanceof Number) {
                        dto.setSkolskaGodinaId(((Number) item.get("skolskaGodinaId")).longValue());
                    }
                    
                    result.add(dto);
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

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
}
