package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
}
