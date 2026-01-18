package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO za statistiku ocena po godini na predmetu.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyGradeStatsDTO {
    private Integer godina;
    private Integer brStudenata;
    private Integer polozilo;
    private Double prosecnaOcena;
    private Double procenatProlaz;
}
