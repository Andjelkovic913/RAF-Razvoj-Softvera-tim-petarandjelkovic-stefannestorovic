package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentPodaciResponse {
    @NotNull
    private String ime;	  // not null
    @NotNull
    private String prezime;  // not null
    @NotNull
    private String srednjeIme;   // not null
    private String jmbg;
    @NotNull
    private LocalDate datumRodjenja;  // not null
    private String mestoRodjenja;
    @NotNull
    private String mestoPrebivalista;  // not null

    private String drzavaRodjenja;
    @NotNull
    private String drzavljanstvo;   // not null
    private String nacionalnost;   // samoizjasnjavanje, moze bilo sta
    @NotNull
    private Character pol;    // not null
    @NotNull
    private String adresa;  // not null
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    @NotNull
    private String emailPrivatni;  // not null
    @NotNull
    private String emailFakultetski;  // not null
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja;   // u toku studija

}
