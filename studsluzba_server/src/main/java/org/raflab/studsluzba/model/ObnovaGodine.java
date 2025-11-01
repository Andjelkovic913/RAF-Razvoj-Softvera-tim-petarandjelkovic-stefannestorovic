package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class ObnovaGodine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer godinaObnove;
    private LocalDate datumObnove;
    private String napomena;

    @ManyToOne
    private StudentIndeks studentIndeks;
    @ManyToOne
    private SkolskaGodina skolskaGodina;
    @ManyToMany
    List<Predmet> predmeti;

}
