package org.raflab.studsluzba.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PredispitnaObaveza {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private int maksimalanBrojPoena; // mozda budemo morali kasnije da menjamo u Integer
    private String vrsta;
}

/*
Svaka predispitna obaveza za predmet sadži vrstu (test, kolokvijum) i maksimalan broj poena koje nosi ta obaveza.

 */