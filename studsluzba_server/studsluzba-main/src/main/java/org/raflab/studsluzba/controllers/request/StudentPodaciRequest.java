package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentPodaciRequest {

    @NotNull
    private String ime;	  // not null
    @NotNull
    private String prezime;  // not null
    @NotNull
    private String srednjeIme;
    @NotNull// not null
    private String jmbg;
    @NotNull
    private LocalDate datumRodjenja;  // not null
    @NotNull
    private String mestoRodjenja;
    @NotNull
    private String mestoPrebivalista;  // not null
    @NotNull
    private String drzavaRodjenja;
    @NotNull
    private String drzavljanstvo;   // not null
    @NotNull
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
    @NotNull
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja;   // u toku studija
}
