package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class VisokoskolskaUstanova {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    private String tipSkole;

    @Column(nullable = false, unique = true, updatable = false)
    private String sifarnik;

    @OneToMany(mappedBy = "visokaUstanova")
    private List<StudentVisokaUstanova> prenosi = new ArrayList<>();

    @PrePersist
    private void ensureSifarnik() {
        if (sifarnik == null || sifarnik.isBlank()) {
            sifarnik = UUID.randomUUID().toString();
        }
    }

}
