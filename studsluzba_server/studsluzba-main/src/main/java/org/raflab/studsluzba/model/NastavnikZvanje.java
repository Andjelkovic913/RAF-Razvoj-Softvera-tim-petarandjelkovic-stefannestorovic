package org.raflab.studsluzba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
@Data
public class NastavnikZvanje {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private LocalDate datumIzbora;  // ili reizbora
	private String naucnaOblast;  // sifarnik na klijentu - tabela u bazi bez veze
	private String uzaNaucnaOblast;  // sifarnik na klijentu - tabela u bazi bez veze
	private String zvanje;   // sifarnik na klijentu - tabela u bazi bez veze
	private boolean aktivno;

	@ManyToOne
	@JsonIgnore
	private Nastavnik nastavnik;
}
