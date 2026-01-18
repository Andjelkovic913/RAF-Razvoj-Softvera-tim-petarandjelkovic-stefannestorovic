package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
@Data
public class DrziPredmetAddRequest {

    @NotNull(message = "Predmet ID is required")
    private Long predmetId;

    @NotNull(message = "Nastavnik ID is required")
    private Long nastavnikId;
}
