package org.raflab.studsluzba.controllers.response;

import lombok.Data;

@Data
public class VisokoskolskaUstanovaResponse {
    private Long id;
    private String naziv;
    private String tipSkole;
    private String sifarnik;
}
