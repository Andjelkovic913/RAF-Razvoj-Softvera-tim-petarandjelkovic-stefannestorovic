package org.raflab.studsluzba.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class IspitniRokDTO {
    private Long id;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;
    private String tipRoka;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    
    @Override
    public String toString() {
        return tipRoka + " (" + datumPocetka + " - " + datumZavrsetka + ")";
    }
}
