package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Ispit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumOdrzavanja;
    private LocalTime vremePocetka;
    private LocalTime vremeZavrsetka;

    @ManyToOne
    @JoinColumn(name = "drziPredmet_id")
    private DrziPredmet drziPredmet;

    @ManyToOne
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;

    @ManyToOne
    @JoinColumn(name = "ispitni_rok_id")
    private IspitniRok ispitniRok;

    @ManyToOne
    @JoinColumn(name = "skolska_godina_id")
    private SkolskaGodina skolskaGodina;

    private boolean zakljucen;

}
