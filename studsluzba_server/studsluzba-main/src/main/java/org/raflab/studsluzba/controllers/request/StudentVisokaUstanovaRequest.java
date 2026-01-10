package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentVisokaUstanovaRequest {
    
    @NotNull(message = "ID studenta je obavezan")
    private Long studentPodaciId;
    
    @NotNull(message = "ID visokoškolske ustanove je obavezan")
    private Long visokaUstanovaId;
    
    private LocalDate datumUpisa;
    
    private LocalDate datumZavrsetka;
    
    private String napomena;
}
