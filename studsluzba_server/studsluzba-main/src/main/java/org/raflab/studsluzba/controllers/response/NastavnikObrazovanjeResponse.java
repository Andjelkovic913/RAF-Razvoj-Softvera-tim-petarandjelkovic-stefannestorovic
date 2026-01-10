package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class NastavnikObrazovanjeResponse {
    private Long id;
    private Long nastavnikId;
    private String nastavnikIme;
    private String nastavnikPrezime;
    private String ustanova;
    private String stepenStudija;
    private Integer godinaZavrsetka;
    private String zvanje;
}
