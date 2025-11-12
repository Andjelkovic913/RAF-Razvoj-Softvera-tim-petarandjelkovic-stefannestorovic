package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpisGodineSimpleResponse {
    private Integer godina;
    private LocalDate datumUpisa;
    private String skolskaGodinaNaziv;
    private Integer prenetiESPB;
}

