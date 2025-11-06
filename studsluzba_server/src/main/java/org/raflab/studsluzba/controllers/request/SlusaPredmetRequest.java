package org.raflab.studsluzba.controllers.request;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SlusaPredmetRequest {

    @NotNull(message = "Student indeks ID is required")
    private Long studentIndeksId;

    @NotNull(message = "DrziPredmet ID is required")
    private Long drziPredmetId;

    private Long upisGodineId;
    private Long studentVisokaUstanovaId;

}
