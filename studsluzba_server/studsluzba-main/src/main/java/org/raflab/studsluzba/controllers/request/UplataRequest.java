package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raflab.studsluzba.model.Uplata;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UplataRequest {

    @NotNull(message = "Student indeks ID je obavezan")
    private Long studentIndeksId;

    @NotNull(message = "Skolska godina ID je obavezna")
    private Long skolskaGodinaId;

    @NotNull(message = "Iznos je obavezan")
    @Positive(message = "Iznos mora biti pozitivan")
    private Double iznosRsd;

    private Uplata.TipUplate tip = Uplata.TipUplate.SKOLARINA;

    private String opis;
}
