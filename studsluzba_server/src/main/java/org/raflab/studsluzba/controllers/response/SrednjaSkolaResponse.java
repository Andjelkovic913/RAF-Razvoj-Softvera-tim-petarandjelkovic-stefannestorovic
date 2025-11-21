package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class SrednjaSkolaResponse {
    private Long id;
    private String naziv;
    private String vrsta;
    private String mesto;
    private String napomena;
    private String sifarnik;
}
