package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IzlazakNaIspit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int brojOsvojenihPoena;
    private String napomena;
    private boolean ponistavaIspit;

    @ManyToOne
    private StudentIndeks studentIndeks;
    @ManyToOne
    private Ispit ispit;


}
