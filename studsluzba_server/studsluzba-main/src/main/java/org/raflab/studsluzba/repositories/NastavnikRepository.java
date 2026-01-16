package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.dtos.NastavnikReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NastavnikRepository extends JpaRepository<Nastavnik, Long> {	//	nasljedjene implementacije poput findById i findByAll

	@Query("SELECT new org.raflab.studsluzba.dtos.NastavnikReportDTO(" +
			"n.ime, n.prezime, nz.zvanje) " +
			"FROM Nastavnik n LEFT JOIN n.zvanja nz " +
			"WHERE nz.aktivno = true OR nz IS NULL " +
			"ORDER BY n.prezime, n.ime")
	List<NastavnikReportDTO> findAllForReport();
	
	@Query("select sp from Nastavnik sp where "
			+ "(:ime is null or lower(sp.ime) like :ime) and "
			+ "(:prezime is null or lower(sp.prezime) like :prezime)")			
	List<Nastavnik> findByImeAndPrezime(@org.springframework.data.repository.query.Param("ime") String ime, @org.springframework.data.repository.query.Param("prezime") String prezime);

	List<Nastavnik> findByEmailIn(List<String> emails);

	@Query("SELECT n FROM Nastavnik n WHERE LOWER(n.email) = LOWER(:email)")
	List<Nastavnik> findByEmail(@org.springframework.data.repository.query.Param("email") String email);

//	@Query("SELECT DISTINCT n FROM Nastavnik n LEFT JOIN FETCH n.zvanja")
//	List<Nastavnik> findAll();
	
}

