package org.raflab.studsluzba.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class DrziPredmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Nastavnik nastavnik;
	
	@ManyToOne
	private Predmet predmet;

	@ManyToOne
	@JoinColumn(name = "skolska_godina_id")
	private SkolskaGodina skolskaGodina;
}
