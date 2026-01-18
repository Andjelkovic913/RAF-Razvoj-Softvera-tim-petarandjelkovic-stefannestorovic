package org.raflab.studsluzba.dtos;

import lombok.Data;

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
    private Long studentId;
    private String studentIme;
    private String studentPrezime;
    private StudijskiProgramBasicDTO studijskiProgram;
    private Integer ostvarenoEspb;
    private StudentPodaciDTO student;
}
