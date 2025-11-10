package org.raflab.studsluzba.model.dtos;

import java.util.List;

import lombok.Data;

@Data
public class StudentWebProfileDTO {
	
	private StudentIndeksDTO aktivanIndeks;	

	// za aktivnu skolsku godinu
	private List<SlusaPredmetDTO> slusaPredmete;
	
}
