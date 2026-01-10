package org.raflab.studsluzba.dtos;

import lombok.Data;

@Data
public class NastavnikDTO {
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;
    private String jmbg;
    private String zvanje;
}
