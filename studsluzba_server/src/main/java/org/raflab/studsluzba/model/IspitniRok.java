package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class IspitniRok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;
    @Enumerated(EnumType.STRING)
    private TipRoka tipRoka;

    @ManyToOne
    @JoinColumn(name = "skolska_godina_id")
    private SkolskaGodina skolskaGodina;

    @OneToMany(mappedBy = "ispitniRok", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ispit> ispiti = new ArrayList<>();

    public enum TipRoka {
        JANUAR,
        FEBRUAR,
        JUN,
        JUL,
        AVGUST,
        SEPTEMBAR
    }
}
