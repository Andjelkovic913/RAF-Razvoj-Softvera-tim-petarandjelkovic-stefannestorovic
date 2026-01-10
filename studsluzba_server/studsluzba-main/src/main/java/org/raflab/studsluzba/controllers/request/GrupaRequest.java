package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GrupaRequest {
    
    @NotNull(message = "Studijski program ID je obavezan")
    private Long studijskiProgramId;
    
    private List<Long> predmetIds;
    
    @NotNull(message = "Školska godina ID je obavezna")
    private Long skolskaGodinaId;
}
