package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentBySrednjaSkolaDTO {
    private Long studentPodaciId;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    
    private Long srednjaSkolaId;
    private String srednjaSkolaNaziv;
    private String srednjaSkolaMesto;
    
    private LocalDate datumZavrsetka;
    private Double uspehIzSrednjeSkole;
    
    private Long indeksId;
    
    public Long getIdStudentPodaci() {
        return studentPodaciId;
    }
    
    public Long getIdIndeks() {
        return indeksId;
    }
}
