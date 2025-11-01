package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Prijava {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate datumPrijave;

    @ManyToOne
    private Ispit ispit;
    @ManyToOne
    private StudentIndeks studentIndeks;

}
