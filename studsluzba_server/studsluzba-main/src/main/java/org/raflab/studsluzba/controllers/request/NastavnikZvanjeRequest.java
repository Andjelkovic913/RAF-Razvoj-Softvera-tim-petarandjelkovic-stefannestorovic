package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class NastavnikZvanjeRequest {
    
    @NotNull(message = "Datum izbora je obavezan")
    private LocalDate datumIzbora;
    
    private String naucnaOblast;
    
    private String uzaNaucnaOblast;
    
    @NotNull(message = "Zvanje je obavezno")
    private String zvanje;
    
    private boolean aktivno;
    
    @NotNull(message = "Nastavnik ID je obavezan")
    private Long nastavnikId;
}
