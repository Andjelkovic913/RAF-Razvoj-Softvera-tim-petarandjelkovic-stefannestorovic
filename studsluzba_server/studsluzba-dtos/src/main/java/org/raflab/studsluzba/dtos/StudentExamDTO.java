package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamDTO {
    private Long id;
    private String predmetNaziv;
    private String predmetSifra;
    private Integer espb;
    private Integer ocena;
    private LocalDate datumPolaganja;
    private String ispitniRok;
    private Double predispitneObaveze;
    private Double poeniIspit;
    private Double ukupnoPoena;
    private boolean polozen;
    private Integer godinaStudija;
}
