package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;

@Data
public class SlusaPredmetResponse {
    private Long id;
    private Long studentIndeksId;
    private DrziPredmetResponse drziPredmet;
    private Long upisGodineId;
}

