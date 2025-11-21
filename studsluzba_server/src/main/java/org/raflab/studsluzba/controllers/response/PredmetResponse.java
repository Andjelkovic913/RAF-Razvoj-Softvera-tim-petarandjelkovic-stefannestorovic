package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class PredmetResponse {
    private Long id;
    private String sifra;
    private String naziv;
    private Integer espb;
    private Integer semestar;
    private Integer fondCasovaVezbe;
    private Integer fondCasovaPredavanja;
    private boolean obavezan;
    private String opis;
}
