package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitResponse {
    private Long id;
    private LocalDate datumOdrzavanja;
    private LocalTime vremePocetka;
    private LocalTime vremeZavrsetka;
    private DrziPredmetResponse drziPredmet;
    private PredmetResponse predmet;
    private Long ispitniRokId;
    private Long skolskaGodinaId;
    private boolean zakljucen;
}
