package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailedSubjectsResponse {
    private Long studentIndeksId;
    private String studentName;
    private List<FailedSubjectInfo> failedSubjects;
    private int totalFailedESPB;
    private int availableESPBForNextYear; // 60 - totalFailedESPB
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FailedSubjectInfo {
        private Long drziPredmetId;
        private Long predmetId;
        private String predmetSifra;
        private String predmetNaziv;
        private Integer espb;
        private Integer godina; // Which year this subject belongs to
        private Integer semestar;
    }
}
