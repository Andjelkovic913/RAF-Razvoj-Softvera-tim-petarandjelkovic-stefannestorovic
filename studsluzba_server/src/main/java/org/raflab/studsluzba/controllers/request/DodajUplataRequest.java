package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DodajUplataRequest {

    @NotNull(message = "Student indeks ID je obavezan")
    private Long studentIndeksId;

    @NotNull(message = "Iznos je obavezan")
    @Positive(message = "Iznos mora biti pozitivan")
    private Double iznosDinara;

}

