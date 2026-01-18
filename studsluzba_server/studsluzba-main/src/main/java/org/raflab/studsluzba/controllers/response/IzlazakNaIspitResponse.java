package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IzlazakNaIspitResponse {
    private Long id;
    
    private Long ispitId;
    private String predmetNaziv;
    private String predmetSifra;
    private LocalDate datumOdrzavanja;
    private String ispitniRok;  // Exam period name (e.g., "JANUAR", "FEBRUAR")
    
    // Student
    private Long studentIndeksId;
    private String ime;
    private String prezime;
    private int broj;
    private int godina;
    private String studProgramOznaka;
    
    // rezultati
    private int osvojeniPredispitniPoeni;
    private int osvojeniIspitniPoeni;
    private int ukupnoPoena;
    private Integer ocena;
    private boolean ponistio;
    private String napomena;
}
