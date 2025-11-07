package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.model.SkolskaGodina;

@Data
public class StudijskiProgramResponse {
    private Long id;
    private String oznaka;
    private String naziv;
    private Integer godinaAkreditacije;
    private String zvanje;
    private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    private String vrstaStudija;
    private Integer ukupnoEspb;
    private SkolskaGodina skolskaGodina; // referenca na skolsku godinu
}

