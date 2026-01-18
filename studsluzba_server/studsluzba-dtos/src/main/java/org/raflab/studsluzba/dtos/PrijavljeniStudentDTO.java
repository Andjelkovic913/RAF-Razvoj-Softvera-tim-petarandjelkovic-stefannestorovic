package org.raflab.studsluzba.dtos;

import lombok.Data;

@Data
public class PrijavljeniStudentDTO {
    private Long prijavaId;
    private Long studentIndeksId;
    private Long slusaPredmetId;
    private String ime;
    private String prezime;
    private Integer broj;
    private Integer godina;
    private String studProgramOznaka;
    private Double predispitneObaveze;
    private String indeks;
    private String predmetNaziv;
    private String ispitniRokNaziv;
    
    public String getFullIndexDisplay() {
        return studProgramOznaka + " " + broj + "/" + godina;
    }
    
    public String getFullName() {
        return ime + " " + prezime;
    }
}
