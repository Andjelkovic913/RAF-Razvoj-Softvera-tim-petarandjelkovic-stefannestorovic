package org.raflab.studsluzba.dtos;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class NastavnikWebProfileDTO {
    private List<PredmetDTO> predmeti;
    private Map<Long, List<StudentIndeksDTO>> slusajuPredmete;
}
