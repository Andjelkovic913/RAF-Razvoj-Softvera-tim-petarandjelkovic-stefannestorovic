package org.raflab.studsluzba.model.dtos;

import lombok.Data;

@Data
public class PrijavljeniStudentDTO {

    private Long prijavaId;  // ID of PrijavaIspita - needed for creating exam result
    private Long studentIndeksId;
    private Long slusaPredmetId;  // ID of SlusaPredmet - needed for creating exam result
    private String ime;
    private String prezime;
    private String studProgramOznaka;
    private Integer godina;
    private Integer broj;
    private String indeks;
    private String predmetNaziv;
    private String ispitniRokNaziv;
    private Double predispitneObaveze;  // Pre-exam points from SlusaPredmet
}


