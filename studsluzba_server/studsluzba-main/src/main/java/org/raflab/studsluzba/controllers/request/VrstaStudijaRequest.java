package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class VrstaStudijaRequest {
    
    @NotBlank(message = "Skraćeni naziv je obavezan")
    private String skraceniNaziv;
    
    @NotBlank(message = "Pun naziv je obavezan")
    private String punNaziv;
}
