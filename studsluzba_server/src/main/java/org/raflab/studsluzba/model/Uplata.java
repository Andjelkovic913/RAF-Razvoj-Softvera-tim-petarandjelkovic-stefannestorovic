package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Uplata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate datumUplate;
    private BigDecimal iznos;
    private BigDecimal srednjiKurs;

    @ManyToOne
    private StudentIndeks studentIndeks;

}
