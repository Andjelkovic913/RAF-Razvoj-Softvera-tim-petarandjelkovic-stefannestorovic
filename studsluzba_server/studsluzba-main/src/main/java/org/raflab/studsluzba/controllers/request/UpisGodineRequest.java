package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpisGodineRequest {

    @NotNull(message = "Datum upisa is required")
    private LocalDate datumUpisa;

    @NotNull(message = "Godina is required")
    private Integer godina;

    private Integer prenetiESPB;

    @NotNull(message = "Student indeks ID is required")
    private Long studentIndeksId;

    @NotNull(message = "Skolska godina ID is required")
    private Long skolskaGodinaId;

}

