package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO za prosečnu ocenu studenata na predmetu za raspon godina.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AverageGradeDTO {
    private Long predmetId;
    private String predmetNaziv;
    private String predmetSifra;
    private Integer startYear;
    private Integer endYear;
    private Double averageGrade;
    private Long totalPassedExams;
}
