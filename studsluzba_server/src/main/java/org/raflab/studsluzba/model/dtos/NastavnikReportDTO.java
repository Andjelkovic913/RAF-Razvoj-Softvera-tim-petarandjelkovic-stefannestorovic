package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NastavnikReportDTO {
    private String ime;
    private String prezime;
    private String zvanje;
}
