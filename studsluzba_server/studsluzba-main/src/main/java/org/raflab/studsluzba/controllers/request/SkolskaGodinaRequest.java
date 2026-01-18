package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
@Data
public class SkolskaGodinaRequest {

    @NotNull(message = "Naziv školske godine je obavezan")
    private String naziv; // npr. "2023/2024"

    private boolean aktivna;
}
