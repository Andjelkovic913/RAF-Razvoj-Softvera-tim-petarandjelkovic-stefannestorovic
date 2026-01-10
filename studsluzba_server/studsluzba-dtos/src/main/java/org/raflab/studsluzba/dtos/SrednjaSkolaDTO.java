package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SrednjaSkolaDTO {
    private Long id;
    private String naziv;
    private String vrsta;
    private String mesto;
    private String napomena;
    private String sifarnik;
    
    @Override
    public String toString() {
        return naziv + (mesto != null ? " (" + mesto + ")" : "");
    }
}
