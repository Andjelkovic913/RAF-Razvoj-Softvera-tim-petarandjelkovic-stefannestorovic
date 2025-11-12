package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.model.dtos.SkolskaGodinaDTO;
import org.raflab.studsluzba.model.dtos.StudentIndeksDTO;

import java.time.LocalDate;

@Data
public class UpisGodineResponse {
    private Long id;
    private LocalDate datumUpisa;
    private Integer godina;
    private Integer prenetiESPB;
    private StudentIndeksDTO studentIndeks;
    private SkolskaGodinaDTO skolskaGodina;
}

