package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PredispitnaObaveza {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private int maksimalanBrojPoena; // mozda budemo morali kasnije da menjamo u Integer
    private String vrsta;

    @ManyToOne
    private Predmet predmet;


}

/*
Svaka predispitna obaveza za predmet sadži vrstu (test, kolokvijum) i maksimalan broj poena koje nosi ta obaveza.

 */