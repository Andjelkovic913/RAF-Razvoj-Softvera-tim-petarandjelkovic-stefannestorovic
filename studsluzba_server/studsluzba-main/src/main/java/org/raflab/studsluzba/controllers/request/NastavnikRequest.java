package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NastavnikRequest {

    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;
    private Set<Long> zvanjeIds;

    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;
}
