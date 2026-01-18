package org.raflab.studsluzba.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
public class ObnovaGodine extends UpisGodine{

    @ManyToOne
    @JoinColumn(name = "prethodni_upis_id")
    private UpisGodine prethodniUpis;

    private String razlogObnove;
}
