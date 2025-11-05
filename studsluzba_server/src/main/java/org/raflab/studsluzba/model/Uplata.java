package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Uplata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_indeks_id")
    private StudentIndeks studentIndeks;

    @ManyToOne
    @JoinColumn(name = "skolska_godina_id")
    private SkolskaGodina skolskaGodina;

    private LocalDateTime datumUplate;

    @Column(nullable = false)
    private double iznosRsd;

    @Column(nullable = false)
    private double srednjiKurs;

    @Column(nullable = false)
    private double skolarinaUEur = 3000; // treba da bude fiksna

    @Enumerated(EnumType.STRING)
    private TipUplate tip = TipUplate.SKOLARINA;

    @Enumerated(EnumType.STRING)
    private StatusUplate status = StatusUplate.U_TOKU;

    private String pozivNaBroj; //todo moze da se skloni ako je previse
    private String opis;        // takodje

    public enum TipUplate {
        SKOLARINA,
        PRIJAVA_ISPITA,
        ZAOSTALA_OBAVEZA,
        DRUGO
    }
    public enum StatusUplate {
        U_TOKU,
        POTVRĐENA,
        ODBIJENA,
        OTKAZANA
    }
}

