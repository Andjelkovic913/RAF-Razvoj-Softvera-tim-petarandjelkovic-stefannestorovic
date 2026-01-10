package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.SkolskaGodinaDTO;
import org.raflab.studsluzba.dtos.StudijskiProgramBasicDTO;

import java.util.List;

@Data
public class GrupaResponse {
    private Long id;
    private StudijskiProgramBasicDTO studijskiProgram;
    private List<PredmetDTO> predmeti;
    private SkolskaGodinaDTO skolskaGodina;
}
