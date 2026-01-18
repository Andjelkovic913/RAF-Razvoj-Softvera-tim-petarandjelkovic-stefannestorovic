package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO za statistiku ocena po godini na predmetu.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyGradeStatsResponse {
    private Integer godina;
    private Integer brStudenata;
    private Integer polozilo;
    private Double prosecnaOcena;
    private Double procenatProlaz;
}
