package org.raflab.studsluzba.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PredispitneObaveze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Vrsta vrsta;

    private int maxBodova;
    private int osvojeniBodovi;

    @ManyToOne
    @JoinColumn(name = "slusa_predmet_id")
    private SlusaPredmet slusaPredmet;
    
    @ManyToOne
    @JoinColumn(name = "skolska_godina_id")
    private SkolskaGodina skolskaGodina;

    public enum Vrsta {
        KOL1,
        KOL2,
        POPRAVNI_KOL1,
        POPRAVNI_KOL2,
        DOMACI,
        PROJEKAT;
    }

}
