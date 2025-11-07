package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NonNull;
import org.raflab.studsluzba.model.StudijskiProgram;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
public class PredmetRequest {
    @NotNull
    private String sifra;
    @NotNull
    private String naziv;
    @NotNull
    private String opis;
    @NotNull
    private Integer espb;
    private Integer semestar;
    private Integer fondCasovaVezbe;
    private Integer fondCasovaPredavanja;

    @NotNull
    private Long studProgramId;

    private boolean obavezan;
}
