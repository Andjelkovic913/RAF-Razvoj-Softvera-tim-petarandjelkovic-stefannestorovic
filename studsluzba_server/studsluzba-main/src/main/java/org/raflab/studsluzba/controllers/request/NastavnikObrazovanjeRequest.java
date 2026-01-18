package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class NastavnikObrazovanjeRequest {
    
    @NotNull(message = "Nastavnik ID je obavezan")
    private Long nastavnikId;
    
    @NotNull(message = "Ustanova je obavezna")
    private String ustanova;
    
    private String stepenStudija; // osnovne, master, doktorske
    
    private Integer godinaZavrsetka;
    
    private String zvanje;
}
