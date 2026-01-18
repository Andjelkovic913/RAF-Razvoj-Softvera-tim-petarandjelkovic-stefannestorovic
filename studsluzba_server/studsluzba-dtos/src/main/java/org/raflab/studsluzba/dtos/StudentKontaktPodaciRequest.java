package org.raflab.studsluzba.dtos;

import lombok.Data;

@Data
public class StudentKontaktPodaciRequest {
    private Long studentId;
    private String emailPrivatni;
    private String emailFakultetski;
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    private String adresa;
    private String mestoStanovanja;
}
