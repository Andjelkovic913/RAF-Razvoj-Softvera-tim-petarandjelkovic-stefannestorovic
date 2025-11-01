package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.JSONUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Data
public class Ispit {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumOdrzavanja;

    @ManyToOne
    private Predmet predmet;

    @ManyToOne
    private Nastavnik nastavnik;

    private LocalTime vremePocetka;

    boolean ispitZakljucen;
        
}

/*
Za svaki ispit se čuva datum održavanja, predmet na koji
se ispit odnosi, nastavnik koji drži ispit, vreme početka i da li je ispit zaključen (uneti poeni od strane nastavnika).
 */
