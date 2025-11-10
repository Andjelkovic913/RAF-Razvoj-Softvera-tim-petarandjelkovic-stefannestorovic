package org.raflab.studsluzba.model.dtos;

import lombok.Data;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.dtos.StudijskiProgramBasicDTO;

import java.time.LocalDate;

@Data
public class StudentIndeksDTO {
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

