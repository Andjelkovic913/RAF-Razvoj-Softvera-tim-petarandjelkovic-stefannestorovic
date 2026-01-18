package org.raflab.studsluzba.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"studProgram"})
public class Predmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @Column(unique = true, nullable = false)
	private String sifra;
	private String naziv;
	private String opis;
	private Integer espb;
    private Integer semestar;
    private Integer fondCasovaVezbe;
    private Integer fondCasovaPredavanja;
	@ManyToOne
	private StudijskiProgram studProgram;
	private boolean obavezan;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sifra == null) ? 0 : sifra.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predmet other = (Predmet) obj;
		if (sifra == null) {
			if (other.sifra != null)
				return false;
		} else if (!sifra.equals(other.sifra))
			return false;
		return true;
	}

}
