package org.raflab.studsluzba.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.Set;

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