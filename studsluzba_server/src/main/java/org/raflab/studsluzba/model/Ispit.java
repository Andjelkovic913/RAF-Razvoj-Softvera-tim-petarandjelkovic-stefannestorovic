package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.JSONUtil;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class Ispit {
    private long id;
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
