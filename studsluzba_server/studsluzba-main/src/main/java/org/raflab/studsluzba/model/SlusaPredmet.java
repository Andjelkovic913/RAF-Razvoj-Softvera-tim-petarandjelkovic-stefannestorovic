package org.raflab.studsluzba.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class SlusaPredmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne	
	private StudentIndeks studentIndeks;
	
	@ManyToOne
	private DrziPredmet drziPredmet;

	@ManyToOne
	@JoinColumn(name = "upis_godine_id")
	private UpisGodine upisGodine;

}