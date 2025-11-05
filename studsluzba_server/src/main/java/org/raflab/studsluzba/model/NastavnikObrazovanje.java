package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class NastavnikObrazovanje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Nastavnik nastavnik;

    private String ustanova;
    private String stepenStudija; // osnovne, master, doktorske
    private Integer godinaZavrsetka;
    private String zvanje;
}
