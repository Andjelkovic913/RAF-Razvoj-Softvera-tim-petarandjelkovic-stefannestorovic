package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.dtos.StudijskiProgramBasicDTO;

import java.time.LocalDate;

@Data
public class StudentIndeksResponse {
    private Long id;
    private int broj;
    private int godina;
    private String studProgramOznaka;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;
    private StudentPodaciResponse student;
    private StudijskiProgramBasicDTO studijskiProgram;
    private Integer ostvarenoEspb;
}
