package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.PredispitneObaveze;

import jakarta.validation.constraints.NotNull;

@Data
public class PredispitneObavezeRequest {

    @NotNull(message = "Vrsta obaveze je obavezna")
    private PredispitneObaveze.Vrsta vrsta;

    @NotNull(message = "Maksimalan broj bodova je obavezan")
    private Integer maxBodova;

    private Integer osvojeniBodovi;

    @NotNull(message = "SlusaPredmet ID je obavezan")
    private Long slusaPredmetId;

    @NotNull(message = "Školska godina ID je obavezna")
    private Long skolskaGodinaId;
}
