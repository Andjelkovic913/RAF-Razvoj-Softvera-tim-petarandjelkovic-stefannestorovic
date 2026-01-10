package org.raflab.studsluzba.dtos;

import lombok.Data;

@Data
public class SlusaPredmetDTO {
    private Long id;
    private Long studentIndeksId;
    private Long drziPredmetId;
    private String predmetNaziv;
    private String predmetSifra;
    private Long upisGodineId;
    private DrziPredmetDTO drziPredmet;
}
