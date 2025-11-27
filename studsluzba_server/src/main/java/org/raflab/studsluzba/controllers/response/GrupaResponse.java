package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.model.dtos.SkolskaGodinaDTO;
import org.raflab.studsluzba.model.dtos.StudijskiProgramBasicDTO;

import java.util.List;

@Data
public class GrupaResponse {
    private Long id;
    private StudijskiProgramBasicDTO studijskiProgram;
    private List<PredmetDTO> predmeti;
    private SkolskaGodinaDTO skolskaGodina;
}
