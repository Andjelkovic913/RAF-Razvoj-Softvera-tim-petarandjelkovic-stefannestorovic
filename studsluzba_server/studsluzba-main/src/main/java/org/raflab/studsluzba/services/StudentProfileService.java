package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.Uplata;
import org.raflab.studsluzba.dtos.StudentProfileDTO;
import org.raflab.studsluzba.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.UplataRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileService {
	
	private final StudentIndeksRepository studentIndeksRepository;
	private final StudentPodaciRepository studentPodaciRepository;
	private final SlusaPredmetRepository slusaPredmetRepository;
	private final UplataRepository uplataRepository;
	private final EntityMappers entityMappers;

	public StudentProfileDTO getStudentProfile(Long indeksId) {
		StudentIndeks studIndeks = studentIndeksRepository.findById(indeksId)
				.orElseThrow(() -> new EntityNotFoundException("Student indeks sa ID jem " + indeksId + " ne postoji"));
		
		StudentProfileDTO retVal = new StudentProfileDTO();
		retVal.setIndeks(entityMappers.fromStudentIndeksToIndeksDTO(studIndeks));
		retVal.setSlusaPredmete(entityMappers.fromSlusaPredmetiToDTO(
				slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(indeksId)));
		
		// Load payments
		System.out.println("DEBUG [StudentProfileService]: Loading payments for indeksId: " + indeksId);
		List<Uplata> uplate = uplataRepository.findByStudentIndeksId(indeksId);
		System.out.println("DEBUG [StudentProfileService]: Found " + uplate.size() + " payments");
		
		for (Uplata u : uplate) {
			System.out.println("DEBUG [StudentProfileService]: Uplata ID=" + u.getId() + 
				", skolskaGodina=" + (u.getSkolskaGodina() != null ? u.getSkolskaGodina().getNaziv() : "NULL"));
		}
		
		retVal.setUplate(entityMappers.fromUplateToStudentPaymentDTOs(uplate));
		System.out.println("DEBUG [StudentProfileService]: Mapped " + 
			(retVal.getUplate() != null ? retVal.getUplate().size() : 0) + " payment DTOs");
		
		return retVal;
	}
	
	public StudentWebProfileDTO getStudentWebProfile(Long indeksId) {
		StudentIndeks studIndeks = studentIndeksRepository.findById(indeksId)
				.orElseThrow(() -> new EntityNotFoundException("Student indeks sa ID " + indeksId + " ne postoji"));
		
		Long studPodaciId = studIndeks.getStudent().getId();
		StudentIndeks aktivanIndeks = studentPodaciRepository.getAktivanIndeks(studPodaciId);
		
		StudentWebProfileDTO retVal = new StudentWebProfileDTO();
		retVal.setAktivanIndeks(aktivanIndeks != null ? entityMappers.fromStudentIndeksToIndeksDTO(aktivanIndeks) : null);
		retVal.setSlusaPredmete(entityMappers.fromSlusaPredmetiToDTO(
				slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(indeksId)));
		return retVal;
	}
}
