package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class DrziPredmetNewRequest {

    private Long predmetId;
    @NotEmpty
    private String predmetNaziv;
    @NotEmpty
    private String emailNastavnik;
}
