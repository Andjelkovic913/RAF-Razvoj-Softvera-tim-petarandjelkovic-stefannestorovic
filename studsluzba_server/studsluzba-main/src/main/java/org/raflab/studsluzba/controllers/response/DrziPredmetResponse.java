package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class DrziPredmetResponse {
    private Long id;

    private Long nastavnikId;
    private String nastavnikIme;
    private String nastavnikPrezime;
    private String nastavnikEmail;

    private Long predmetId;
    private String predmetNaziv;
    private String predmetSifra;

    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
}
