package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.PredmetReportDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PredmetRepository extends CrudRepository<Predmet, Long> {

    @Query("select p from Predmet p where p.studProgram.godinaAkreditacije = :godinaAkreditacije")
	List<Predmet> getPredmetForGodinaAkreditacije(Integer godinaAkreditacije);

	List<Predmet> getPredmetsByStudProgramAndObavezan(StudijskiProgram studProgram, boolean obavezan);

	List<Predmet> findByStudProgram(StudijskiProgram studProgram);

	@Query("select p from Predmet p where p.studProgram = :studProgram and p.semestar in :semestri")
	List<Predmet> findByStudProgramAndSemestarIn(StudijskiProgram studProgram, List<Integer> semestri);

	List<Predmet> findByIdIn(List<Long> ids);
	List<Predmet> findByNazivIn(List<String> nazivi);
    boolean existsBySifra(String sifra);

	// Za kaskadno brisanje StudijskogPrograma
	@Query("select p from Predmet p where p.studProgram.id = :studProgramId")
	List<Predmet> findByStudProgramId(@Param("studProgramId") Long studProgramId);

	// Optimized query for report - returns only necessary fields for specific study program
	@Query("SELECT new org.raflab.studsluzba.model.dtos.PredmetReportDTO(p.naziv, p.espb) " +
			"FROM Predmet p WHERE p.studProgram.oznaka = :oznaka ORDER BY p.semestar, p.naziv")
	List<PredmetReportDTO> findAllForReportByStudProgram(@Param("oznaka") String oznaka);
}
