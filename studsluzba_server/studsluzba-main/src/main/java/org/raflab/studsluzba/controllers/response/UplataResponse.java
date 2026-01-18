package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raflab.studsluzba.model.Uplata;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UplataResponse {

    private Long id;
    private Long studentIndeksId;
    private Long skolskaGodinaId;
    private LocalDateTime datumUplate;
    private Double iznosRsd;
    private Double srednjiKurs;
    private Double skolarinaUEur;
    private Uplata.TipUplate tip;
    private Uplata.StatusUplate status;
    private String pozivNaBroj;
    private String opis;
}
