package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VisokoskolskaUstanovaRequest {
    
    @NotBlank(message = "Naziv visokoškolske ustanove je obavezan")
    private String naziv;
    
    @NotBlank(message = "Tip škole je obavezan")
    private String tipSkole;
}
