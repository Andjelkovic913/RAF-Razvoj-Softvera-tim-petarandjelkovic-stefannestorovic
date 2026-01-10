package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

/**
 * Response DTO for listing students who attended a specific high school.
 * Contains only relevant fields without null values.
 */
@Data
public class StudentBySrednjaSkolaResponse {
    
    // Student Info
    private Long studentPodaciId;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    
    // High School Info
    private Long srednjaSkolaId;
    private String srednjaSkolaNaziv;
    private String srednjaSkolaMesto;
    
    // Enrollment Info
    private LocalDate datumZavrsetka;
    private Double uspehIzSrednjeSkole;
}
