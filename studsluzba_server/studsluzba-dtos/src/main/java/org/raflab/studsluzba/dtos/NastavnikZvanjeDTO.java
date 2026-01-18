package org.raflab.studsluzba.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NastavnikZvanjeDTO {
    private Long id;
    private String zvanje;
    private LocalDate datumIzbora;
    private LocalDate datumPrestanka;
    private boolean aktivno;
}
