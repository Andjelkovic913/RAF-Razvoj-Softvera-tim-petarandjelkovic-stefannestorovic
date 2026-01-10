package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitRequest {
    @NotNull
    private LocalDate datumOdrzavanja;
    @NotNull
    private LocalTime vremePocetka;
    @NotNull
    private LocalTime vremeZavrsetka;

    @NotNull
    private Long drziPredmetId;
    @NotNull
    private Long predmetId;
    @NotNull
    private Long ispitniRokId;
    @NotNull
    private Long skolskaGodinaId;

    private boolean zakljucen;
}
