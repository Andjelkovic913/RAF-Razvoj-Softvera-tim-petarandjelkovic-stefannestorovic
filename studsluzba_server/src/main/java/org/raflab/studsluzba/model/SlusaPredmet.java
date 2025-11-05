package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

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

	@ManyToOne
	@JoinColumn(name = "student_visoka_ustanova_id")
	private StudentVisokaUstanova studentVisokaUstanova;

}