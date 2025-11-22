package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.IspitniRok;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class IspitniRokRequest {

    @NotNull(message = "Datum početka je obavezan")
    private LocalDate datumPocetka;

    @NotNull(message = "Datum završetka je obavezan")
    private LocalDate datumZavrsetka;

    @NotNull(message = "Tip roka je obavezan")
    private IspitniRok.TipRoka tipRoka;

    @NotNull(message = "Školska godina ID je obavezna")
    private Long skolskaGodinaId;
}
