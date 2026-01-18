package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.dtos.DrziPredmetDTO;

@Data
public class SlusaPredmetResponse {
    private Long id;
    private Long studentIndeksId;
    private DrziPredmetDTO drziPredmet;
    private Long upisGodineId;
}

