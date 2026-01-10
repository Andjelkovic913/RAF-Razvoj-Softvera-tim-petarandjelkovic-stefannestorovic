package org.raflab.studsluzba.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PrijavaIspitaDTO {
    private Long id;
    private Long studentIndeksId;
    private Long ispitId;
    private LocalDate datumPrijave;
    
    // Student info for display
    private String studentIme;
    private String studentPrezime;
    private Integer brojIndeksa;
    private Integer godinaIndeksa;
    private String studProgramOznaka;
    
    // Exam info
    private String predmetNaziv;
}
