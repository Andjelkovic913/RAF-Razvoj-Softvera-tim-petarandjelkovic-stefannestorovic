package org.raflab.studsluzba.model;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class StudentVisokaUstanova {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentPodaci student;

    @ManyToOne
    private VisokoskolskaUstanova visokaUstanova;

    private LocalDate datumUpisa;
    private LocalDate datumZavrsetka;
    private String napomena;
}
