package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PrijavaIspita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;

    @ManyToOne
    @JoinColumn(name = "ispit_id", nullable = false)
    private Ispit ispit;

    private LocalDate datumPrijave;

    @OneToOne(mappedBy = "prijava", cascade = CascadeType.ALL, orphanRemoval = true)
    private IzlazakNaIspit izlazakNaIspit;

}
