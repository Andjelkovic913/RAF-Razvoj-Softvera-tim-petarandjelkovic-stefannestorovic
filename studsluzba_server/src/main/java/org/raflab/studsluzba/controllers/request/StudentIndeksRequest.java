package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class StudentIndeksRequest { //TODO - add notNull annotations

    @Positive
    private int godina; //ako npr student mastera upisuje neki program, a godina presla u sledecu?
    @NotNull
    private String studProgramOznaka;
    @NotNull
    private String nacinFinansiranja;
    private boolean aktivan;
    @NotNull
    private LocalDate vaziOd;   //TODO - da li da setujm trenutni datum
    @NotNull
    private Long studentId;
}