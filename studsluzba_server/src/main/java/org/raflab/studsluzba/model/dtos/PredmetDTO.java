package org.raflab.studsluzba.model.dtos;

import lombok.Data;

@Data
public class PredmetDTO {

    private Long id;
    private String sifra;
    private String naziv;
    private Integer semestar;
    private Integer espb;
    private boolean obavezan;
    private Long studProgramId;


}
