package org.raflab.studsluzba.dtos;

import lombok.Data;

@Data
public class PredmetRequest {
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private Integer semestar;
    private Integer fondCasovaVezbe;
    private Integer fondCasovaPredavanja;
    private Long studProgramId;
    private boolean obavezan;
}
