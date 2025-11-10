package org.raflab.studsluzba.model.dtos;

import java.util.List;

import lombok.Data;
import org.raflab.studsluzba.model.dtos.PredmetDTO;

@Data
public class StudentProfileDTO {
	
	private StudentIndeksDTO indeks;	

	// za aktivnu skolsku godinu
	// zasto List<SlusaPredmetDTO> umesto SlusaPredmetDTO?
	private List<SlusaPredmetDTO> slusaPredmete;
	
	private List<PredmetDTO> nepolozeniPredmeti;
	
}
