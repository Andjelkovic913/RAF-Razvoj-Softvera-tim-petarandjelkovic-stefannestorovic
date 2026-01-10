package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class SrednjaSkola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    @Enumerated(EnumType.STRING)
    private VrstaSkole vrsta;
    private String mesto;
    private String napomena;

    @Column(nullable = false, unique = true, updatable = false)
    private String sifarnik;

    public enum VrstaSkole {
        GIMNAZIJA,
        STRUCNA,
        UMETNICKA,
        TEHNICKA,
        MUZICKA
    }

    @PrePersist
    private void ensureSifarnik() {
        if (sifarnik == null || sifarnik.isBlank()) {
            sifarnik = UUID.randomUUID().toString();
        }
    }
}
