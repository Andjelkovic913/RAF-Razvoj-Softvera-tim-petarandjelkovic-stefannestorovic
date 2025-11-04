package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Uspeh {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    private Integer ocena;
    private LocalDate datumPolaganja;

    @ManyToOne
    private StudentIndeks studentIndeks;
    @ManyToOne
    private Predmet predmet;
    @ManyToOne
    private Ispit ispit;

}
