package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class ObnovaGodine extends UpisGodine{

    @ManyToOne
    @JoinColumn(name = "prethodni_upis_id")
    private UpisGodine prethodniUpis;

    private String razlogObnove;
}
