package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class StudentSrednjaSkola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentPodaci student;
    @ManyToOne
    private SrednjaSkola srednjaSkola;

    private LocalDate datumUpisa;
    private LocalDate datumZavrsetka;
    private double uspehSaPrijemnog;
    private double uspehIzSrednjeSkole;

}
