package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentSrednjaSkolaResponse {
    private Long id;
    private Long studentPodaciId;
    private String studentIme;
    private String studentPrezime;
    private Long srednjaSkolaId;
    private String srednjaSkolaNaziv;
    private String srednjaSkolaMesto;
    private LocalDate datumUpisa;
    private LocalDate datumZavrsetka;
    private double uspehSaPrijemnog;
    private double uspehIzSrednjeSkole;
}
