package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class IspitniRok {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @ManyToMany
    private Set<Ispit> ispiti;





}
/*
Za ispitni rok se pamte datumi početka i završetka i ispiti koji se u njemu održavaju.

 */