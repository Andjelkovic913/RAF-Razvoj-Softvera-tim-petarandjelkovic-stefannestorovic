package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SrednjaSkolaRequest {
    
    @NotBlank(message = "Naziv srednje škole je obavezan")
    private String naziv;
    
    @NotBlank(message = "Vrsta škole je obavezna")
    private String vrsta;
    
    @NotBlank(message = "Mesto škole je obavezno")
    private String mesto;
    
    private String napomena;
}
