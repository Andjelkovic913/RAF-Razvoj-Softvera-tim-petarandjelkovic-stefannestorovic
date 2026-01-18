package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisokoskolskaUstanovaDTO {
    private Long id;
    private String naziv;
    private String tipSkole;
    
    @Override
    public String toString() {
        return naziv + (tipSkole != null ? " (" + tipSkole + ")" : "");
    }
}
