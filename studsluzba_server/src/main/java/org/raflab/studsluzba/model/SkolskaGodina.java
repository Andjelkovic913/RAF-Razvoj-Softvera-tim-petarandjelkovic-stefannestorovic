package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class SkolskaGodina {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private int godina;
    private boolean aktivna;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @OneToMany
    private List<Ispit> ispiti;   //moracemo da proverimo ovu vezu kako cemo da gledamo ali verovatno je ovo dobro
    @OneToMany(mappedBy = "skolskaGodina")
    private List<UpisGodine> upisi;



}
