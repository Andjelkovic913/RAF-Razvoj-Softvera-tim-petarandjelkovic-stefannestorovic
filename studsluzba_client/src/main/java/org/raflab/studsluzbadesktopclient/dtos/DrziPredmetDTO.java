package org.raflab.studsluzbadesktopclient.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrziPredmetDTO {
    private Long id;
    private Long predmetId;
    private String predmetOznaka;
    private String predmetNaziv;
    private Long nastavnikId;
    private String nastavnikIme;
    private String nastavnikPrezime;
    private Long skolskaGodinaId;
}
