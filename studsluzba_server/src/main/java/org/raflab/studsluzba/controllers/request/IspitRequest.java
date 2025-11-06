package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.SkolskaGodina;

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

    private DrziPredmet drziPredmet;
    @NotNull
    private Predmet predmet;

    private IspitniRok ispitniRok;

    private SkolskaGodina skolskaGodina;

    private boolean zakljucen;
}
