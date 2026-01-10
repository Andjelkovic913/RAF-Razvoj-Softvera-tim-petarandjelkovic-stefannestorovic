package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SkolskaGodina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv; // 2022/2023

    @OneToMany(mappedBy = "skolskaGodina")
    private List<IspitniRok> ispitniRokovi = new ArrayList<>();

    @OneToMany(mappedBy = "skolskaGodina")
    private List<StudijskiProgram> studijskiProgrami = new ArrayList<>();

    @OneToMany(mappedBy = "skolskaGodina")
    private List<DrziPredmet> drziPredmeti = new ArrayList<>();

    @OneToMany(mappedBy = "skolskaGodina")
    private List<PredispitneObaveze> predispitneObaveze;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<Uplata> uplate;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<Grupa> grupe = new ArrayList<>();

    private boolean aktivna;

}
