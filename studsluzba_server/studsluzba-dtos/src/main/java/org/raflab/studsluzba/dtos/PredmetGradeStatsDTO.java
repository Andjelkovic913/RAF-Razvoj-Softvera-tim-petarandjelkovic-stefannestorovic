package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO za statistiku ocena na pojedinačnom predmetu za raspon godina.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredmetGradeStatsDTO {
    private Long predmetId;
    private String predmetSifra;
    private String predmetNaziv;
    private Integer brStudenata;
    private Integer polozilo;
    private Double prosecnaOcena;
    private Double procenatProlaz;
}
