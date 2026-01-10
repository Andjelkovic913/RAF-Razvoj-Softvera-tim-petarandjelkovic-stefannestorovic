package org.raflab.studsluzba.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitDTO {
    private Long id;
    private LocalDate datumOdrzavanja;
    private LocalTime vremePocetka;
    private LocalTime vremeZavrsetka;
    private Long drziPredmetId;
    private Long predmetId;
    private String predmetNaziv;
    private String predmetSifra;
    private String nastavnikIme;
    private Long ispitniRokId;
    private Long skolskaGodinaId;
    private boolean zakljucen;
    
    @Override
    public String toString() {
        return predmetNaziv + " - " + datumOdrzavanja;
    }
}
