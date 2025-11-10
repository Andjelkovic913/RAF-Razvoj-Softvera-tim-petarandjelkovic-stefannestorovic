package org.raflab.studsluzba.model.dtos;

import lombok.Data;

@Data
public class StudijskiProgramBasicDTO {
    private Long id;
    private String oznaka;
    private String naziv;
    private Integer godinaAkreditacije;
    private String zvanje;
    private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    private String vrstaStudija;
    private Integer ukupnoEspb;
}

