package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Grupa {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private StudijskiProgram studijskiProgram;
	
	@ManyToMany
	private List<Predmet> predmeti;

	@ManyToOne
	@JoinColumn(name = "skolska_godina_id")
	private SkolskaGodina skolskaGodina;
}