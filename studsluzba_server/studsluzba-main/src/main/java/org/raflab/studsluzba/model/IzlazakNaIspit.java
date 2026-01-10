package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IzlazakNaIspit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "prijava_id", nullable = false, unique = true)
    private PrijavaIspita prijava;

    // predmet koji student slusa/polaze nam donosi predispitne poene
    @ManyToOne(optional = false)
    @JoinColumn(name = "slusa_predmet_id", nullable = false)
    private SlusaPredmet slusaPredmet;

    private int osvojeniPredispitniPoeni;  
    private int osvojeniIspitniPoeni;
    private int ukupnoPoena;
    private Integer ocena;                 
    private boolean ponistio;              
    private String napomena;

    // Helper metoda za laksi pristup
    @Transient
    public Ispit getIspit() {
        return prijava != null ? prijava.getIspit() : null;
    }

    @Transient
    public StudentIndeks getStudentIndeks() {
        return prijava != null ? prijava.getStudentIndeks() : null;
    }
    

}