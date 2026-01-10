package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private Long idIndeks;
    private Long idStudentPodaci;
    private String ime;
    private String prezime;
    private int godinaUpisa;
    private String studProgramOznaka;
    private int broj;
    private boolean aktivanIndeks;
    
    private String pol;
    private String srednjeIme;
    private String jmbg;
    private Date datumRodjenja;
    private String mestoRodjenja;
    private String drzavaRodjenja;
    private String drzavljanstvo;
    private String nacionalnost;
    private String adresa;
    private String mestoStanovanja;
    private String brojTelefona;
    private String emailFakultet;
    private String emailPrivatni;
    private String licnaKarta;
    
    public String getBrojIndeksa() {
        if (studProgramOznaka != null && broj > 0) {
            return studProgramOznaka + " " + broj + "/" + godinaUpisa;
        }
        return "";
    }
    
    public String getStudijskiProgram() {
        return studProgramOznaka != null ? studProgramOznaka : "";
    }
}
