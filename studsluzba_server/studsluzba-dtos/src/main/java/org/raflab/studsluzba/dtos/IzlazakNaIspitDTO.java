package org.raflab.studsluzba.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class IzlazakNaIspitDTO {
    private Long id;
    private Long ispitId;
    private Long prijavaIspitaId;
    private Long studentIndeksId;
    private Long slusaPredmetId;
    
    // Student info
    private String studentIme;
    private String studentPrezime;
    private Integer brojIndeksa;
    private Integer godinaIndeksa;
    private String studProgramOznaka;
    
    // Exam/Subject info
    private String predmetNaziv;
    private String predmetSifra;
    private LocalDate datumOdrzavanja;
    
    // Results
    private Integer osvojeniPredispitniPoeni;
    private Integer osvojeniIspitniPoeni;
    private Integer ukupnoPoena;
    private Integer ocena;
    private boolean ponistio;
    private String napomena;
    
    public String getFullStudentInfo() {
        return studentIme + " " + studentPrezime + " (" + studProgramOznaka + " " + brojIndeksa + "/" + godinaIndeksa + ")";
    }
}
