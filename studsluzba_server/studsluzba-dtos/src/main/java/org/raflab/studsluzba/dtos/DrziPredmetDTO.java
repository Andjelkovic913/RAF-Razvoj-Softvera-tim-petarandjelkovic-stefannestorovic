package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrziPredmetDTO {
    private int id;
    private TeacherDTO teacher;
    private SubjectDTO subject;
    private String classType;
    private String sessionCount;
}
