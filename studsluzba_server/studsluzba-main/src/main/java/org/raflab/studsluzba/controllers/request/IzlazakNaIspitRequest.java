package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class IzlazakNaIspitRequest {
    private Long prijavaIspitaId;
    private Long slusaPredmetId;
    private int osvojeniPredispitniPoeni;
    private int osvojeniIspitniPoeni;
    private int ukupnoPoena;
    private Integer ocena;
    private boolean ponistio;
    private String napomena;
}
