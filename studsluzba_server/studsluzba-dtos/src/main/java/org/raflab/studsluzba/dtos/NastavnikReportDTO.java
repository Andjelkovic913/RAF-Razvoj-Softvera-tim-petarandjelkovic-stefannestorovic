package org.raflab.studsluzba.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NastavnikReportDTO {
    private String ime;
    private String prezime;
    private String zvanje;
}
