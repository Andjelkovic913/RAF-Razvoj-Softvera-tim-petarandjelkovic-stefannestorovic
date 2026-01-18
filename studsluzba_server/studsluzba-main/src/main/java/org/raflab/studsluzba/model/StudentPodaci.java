package org.raflab.studsluzba.model;

import lombok.Data;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Data
public class StudentPodaci {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    private String mestoPrebivalista;
    private String drzavaRodjenja;
    private String drzavljanstvo;
    private String nacionalnost;
    private Character pol;
    private String adresa;
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    private String emailPrivatni;
    private String emailFakultetski;
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja;
}
