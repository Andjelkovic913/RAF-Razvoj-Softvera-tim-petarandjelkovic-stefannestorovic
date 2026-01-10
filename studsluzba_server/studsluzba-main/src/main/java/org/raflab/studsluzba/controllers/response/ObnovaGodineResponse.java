package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ObnovaGodineResponse {
    private Long id;
    private LocalDate datumUpisa;
    private Integer godina;
    private Integer prenetiESPB;
    private String razlogObnove;
    
    // StudentIndeks info
    private Long studentIndeksId;
    private int broj;
    private int godinaStudija;
    private String studProgramOznaka;
    
    // SkolskaGodina info
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    
    // Prethodni upis info
    private Long prethodniUpisId;
}
