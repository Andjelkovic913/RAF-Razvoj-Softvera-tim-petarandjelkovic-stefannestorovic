package org.raflab.studsluzba.model.dtos;

import lombok.Data;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;

@Data
public class SlusaPredmetDTO {
    private Long id;
    private Long studentIndeksId;
    private DrziPredmetResponse drziPredmet;
    private Long upisGodineId;
    private Long studentVisokaUstanovaId;
}

