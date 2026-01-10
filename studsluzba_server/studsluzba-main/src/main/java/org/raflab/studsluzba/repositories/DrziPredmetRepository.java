package org.raflab.studsluzba.repositories;

import java.util.List;


import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DrziPredmetRepository extends CrudRepository<DrziPredmet, Long> {
	
	@Query("select dp.predmet from DrziPredmet dp where dp.nastavnik.id = :idNastavnika")
	List<Predmet> getPredmetiZaNastavnikaUAktivnojSkolskojGodini(Long idNastavnika);
	
	@Query("select dp from DrziPredmet dp where dp.nastavnik.id = :idNastavnik "
			+ "and dp.predmet.id = :idPredmet")
	DrziPredmet getDrziPredmetNastavnikPredmet(Long idPredmet, Long idNastavnik);

	@Query("select dp from DrziPredmet dp where dp.predmet.id = :predmetId and dp.skolskaGodina.id = :skolskaGodinaId")
	List<DrziPredmet> findByPredmetIdAndSkolskaGodinaId(Long predmetId, Long skolskaGodinaId);

	// Za kaskadno brisanje nastavnika
	@Query("select dp from DrziPredmet dp where dp.nastavnik.id = :nastavnikId")
	List<DrziPredmet> findByNastavnikId(@Param("nastavnikId") Long nastavnikId);

	// Za kaskadno brisanje predmeta
	@Query("select dp from DrziPredmet dp where dp.predmet.id = :predmetId")
	List<DrziPredmet> findByPredmetId(@Param("predmetId") Long predmetId);

}
