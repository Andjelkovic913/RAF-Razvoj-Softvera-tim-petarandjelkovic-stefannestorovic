package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentVisokaUstanovaResponse {
    private Long id;
    private Long studentPodaciId;
    private String studentIme;
    private String studentPrezime;
    private Long visokaUstanovaId;
    private String visokaUstanovaNaziv;
    private String visokaUstanovaTipSkole;
    private LocalDate datumUpisa;
    private LocalDate datumZavrsetka;
    private String napomena;
}
