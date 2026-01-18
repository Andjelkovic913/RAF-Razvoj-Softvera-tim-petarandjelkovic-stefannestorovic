package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentSrednjaSkolaRequest {
    
    @NotNull(message = "ID studenta je obavezan")
    private Long studentPodaciId;
    
    @NotNull(message = "ID srednje škole je obavezan")
    private Long srednjaSkolaId;
    
    private LocalDate datumUpisa;
    
    private LocalDate datumZavrsetka;
    
    private double uspehSaPrijemnog;
    
    private double uspehIzSrednjeSkole;
}
