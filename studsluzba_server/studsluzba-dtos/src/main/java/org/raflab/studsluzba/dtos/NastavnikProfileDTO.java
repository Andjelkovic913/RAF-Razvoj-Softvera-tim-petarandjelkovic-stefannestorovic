package org.raflab.studsluzba.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class NastavnikProfileDTO {
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String pol;
    private String trenutnoZvanje;
    private List<NastavnikZvanjeDTO> zvanja;

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (ime != null) sb.append(ime);
        if (srednjeIme != null && !srednjeIme.isEmpty()) sb.append(" ").append(srednjeIme);
        if (prezime != null) sb.append(" ").append(prezime);
        return sb.toString().trim();
    }
}
