package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class ObnovaGodineRequest {
    
    @NotNull(message = "Student indeks ID je obavezan")
    private Long studentIndeksId;
    
    @NotNull(message = "Godina je obavezna")
    @Min(value = 1, message = "Godina mora biti između 1 i 6")
    @Max(value = 6, message = "Godina mora biti između 1 i 6")
    private Integer godina;
    
    @NotNull(message = "Školska godina ID je obavezna")
    private Long skolskaGodinaId;
    
    private Long prethodniUpisId;
    
    private List<Long> nextYearDrziPredmetIds;
    
    @Min(value = 0, message = "Preneti ESPB ne može biti negativan")
    private Integer prenetiESPB;
    
    private String razlogObnove;
}
