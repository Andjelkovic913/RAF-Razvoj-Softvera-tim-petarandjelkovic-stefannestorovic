package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NonNull;
import org.raflab.studsluzba.model.StudijskiProgram;

import javax.persistence.ManyToOne;

@Data
public class PredmetRequest {
    @NonNull
    private String sifra;
    @NonNull
    private String naziv;
    @NonNull
    private String opis;
    @NonNull
    private Integer espb;
    private Integer semestar;
    private Integer fondCasovaVezbe;
    private Integer fondCasovaPredavanja;

    private StudijskiProgram studProgram;
    private boolean obavezan;
}
