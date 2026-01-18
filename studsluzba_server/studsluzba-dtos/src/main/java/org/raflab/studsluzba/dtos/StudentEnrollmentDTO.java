package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrollmentDTO {
    private Long id;
    private String skolskaGodina;
    private Integer godinaStudija;
    private String studijskiProgram;
    private String tipUpisa;
    private LocalDate datumUpisa;
    private String nacinFinansiranja;
    private Integer ostvareniEspb;
    private boolean aktivan;
}
