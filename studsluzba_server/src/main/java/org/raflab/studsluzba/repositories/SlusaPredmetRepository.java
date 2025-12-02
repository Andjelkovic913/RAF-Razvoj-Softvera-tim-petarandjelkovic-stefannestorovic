package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SlusaPredmetRepository extends CrudRepository<SlusaPredmet, Long> {
	
	@Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :indeksId1")
	List<SlusaPredmet> getSlusaPredmetForIndeksAktivnaGodina(@Param("indeksId1") Long indeksId);

	// Proverava da li student slusa predmet
	@Query("select count(sp) > 0 from SlusaPredmet sp " +
	       "where sp.studentIndeks.id = :studentIndeksId " +
	       "and sp.drziPredmet.predmet.id = :predmetId")
	boolean existsByStudentIndeksIdAndPredmetId(Long studentIndeksId, Long predmetId);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.predmet.id = :idPredmeta "
			+ "and sp.drziPredmet.nastavnik.id = :idNastavnika  ")
	List<StudentIndeks> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet")
	List<StudentIndeks> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet);
	
	
	/*
	 * TODO dodati slicne operacije koja vracaju sve studente za stud program/ godinu studija koje ne slusaju predmet
	 */
	@Query("select si from StudentIndeks si where not exists "
			+ "(select sp from SlusaPredmet sp where sp.studentIndeks=si and sp.drziPredmet.id = :idDrziPredmet) ")
	List<StudentIndeks> getStudentiNeSlusajuDrziPredmet(Long idDrziPredmet);
	
	
	@Query("select count(sp) > 0 from SlusaPredmet sp where sp.studentIndeks.id = :studentIndeksId " +
			"and sp.drziPredmet.id = :drziPredmetId")
	boolean existsByStudentIndeksIdAndDrziPredmetId(Long studentIndeksId, Long drziPredmetId);

	@Query("select sp from SlusaPredmet sp where sp.upisGodine.id = :upisGodineId")
	List<SlusaPredmet> findByUpisGodineId(Long upisGodineId);

	@Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :studentIndeksId")
	List<SlusaPredmet> findByStudentIndeksId(@Param("studentIndeksId") Long studentIndeksId);

	// Za kaskadno brisanje DrziPredmet
	@Query("select sp from SlusaPredmet sp where sp.drziPredmet.id = :drziPredmetId")
	List<SlusaPredmet> findByDrziPredmetId(@Param("drziPredmetId") Long drziPredmetId);
}
