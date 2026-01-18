package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredmetDTO {
    private Long id;
    private String sifra;
    private String naziv;
    private Integer espb;
    private Integer semestar;
    private Integer fondCasovaVezbe;
    private Integer fondCasovaPredavanja;
    private boolean obavezan;
    private String opis;
    private Long studProgramId;
    private String studProgramNaziv;
}
