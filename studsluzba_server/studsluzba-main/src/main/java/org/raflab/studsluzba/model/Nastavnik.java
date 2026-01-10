package org.raflab.studsluzba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = "zvanja")
@EqualsAndHashCode(exclude = "zvanja")
public class Nastavnik {
	 
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;
	 private String ime;
	 private String prezime;
	 private String srednjeIme;
	 private String email;
	 private String brojTelefona;
	 private String adresa;

	 @OneToMany(mappedBy = "nastavnik", fetch = FetchType.EAGER)
	 private Set<NastavnikZvanje> zvanja;
	 
	 private LocalDate datumRodjenja;
	 private Character pol;
	 private String jmbg;

}


/*
Za nastavnika se čuva ime, prezime, srednje ime, email, podaci o obrazovanju
(visokoškolske ustanove na kojoj je završio sve nivoe studija) i svaki nastavnik u toku svog rada može
imati različita saradnička i nastavnička zvanja (prvo asistent, zatim docent, vanredni profesor, ...).
Za nastavnika treba upamtiti sva zvanja u kojima je bio i to za svako zvanje se čuva datum izbora, uža
naučna oblast i sam naziv zvanja (asistent, docent,...).

 */
