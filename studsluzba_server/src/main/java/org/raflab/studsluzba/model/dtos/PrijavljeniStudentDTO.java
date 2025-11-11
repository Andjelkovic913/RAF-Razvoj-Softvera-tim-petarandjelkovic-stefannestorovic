package org.raflab.studsluzba.model.dtos;

import lombok.Data;

@Data
public class PrijavljeniStudentDTO {

    private Long studentIndeksId;
    private String ime;
    private String prezime;
    private String studProgramOznaka;
    private Integer godina;
    private Integer broj;
    private String indeks;
    private String predmetNaziv;
    private String ispitniRokNaziv;
}


