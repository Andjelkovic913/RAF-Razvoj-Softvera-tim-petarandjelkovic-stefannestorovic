package org.raflab.studsluzba.dtos;

import lombok.Data;

@Data
public class SkolskaGodinaDTO {
    private Long id;
    private String naziv;
    private boolean aktivna;
}
