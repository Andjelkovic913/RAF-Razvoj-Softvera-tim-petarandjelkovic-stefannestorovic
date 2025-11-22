package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.model.IspitniRok;

import java.time.LocalDate;

@Data
public class IspitniRokResponse {
    private Long id;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;
    private IspitniRok.TipRoka tipRoka;
    private Long skolskaGodinaId;
}
