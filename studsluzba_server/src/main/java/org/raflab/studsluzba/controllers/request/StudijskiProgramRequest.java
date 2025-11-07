package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class StudijskiProgramRequest {
    private String oznaka;
    private String naziv;
    private Integer godinaAkreditacije;
    private String zvanje;
    private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    private String vrstaStudija;
    private Integer ukupnoEspb;
    private Long skolskaGodinaId;
}


