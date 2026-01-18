package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentIndeksRepository extends JpaRepository<StudentIndeks, Long> {
	
	
	@Query("select indeks from StudentIndeks indeks where indeks.studProgramOznaka like :studProgramOznaka and indeks.godina = :godina " + "and indeks.broj = :broj ")
    StudentIndeks findStudentIndeks(@Param("studProgramOznaka") String studProgramOznaka, @Param("godina") int godina, @Param("broj") int broj);
	
	
	@Query("select indeks from StudentIndeks indeks where "
			+ "indeks.aktivan = true and "
			+ "(:ime is null or lower(indeks.student.ime) like concat('%', lower(:ime), '%')) and "
			+ "(:prezime is null or lower(indeks.student.prezime) like concat('%', lower(:prezime), '%')) and "
			+ "(:studProgramOznaka is null or lower(indeks.studProgramOznaka) like concat('%', lower(:studProgramOznaka), '%')) and "
			+ "(:godina is null or indeks.godina = :godina) and "
			+ "(:broj is null or indeks.broj = :broj)")
	Page<StudentIndeks> findStudentIndeks(@Param("ime") String ime, 
										   @Param("prezime") String prezime, 
										   @Param("studProgramOznaka") String studProgramOznaka, 
										   @Param("godina") Integer godina, 
										   @Param("broj") Integer broj, 
										   Pageable pageable);
	
	@Query("select si from StudentIndeks si where si.student.id = :idStudentPodaci")
	List<StudentIndeks> findStudentIndeksiForStudentPodaciId(@Param("idStudentPodaci") Long idStudentPodaci);

	@Query("select si from StudentIndeks si where si.student.id = :idStudentPodaci and si.aktivan = true")
	StudentIndeks findAktivanStudentIndeksiByStudentPodaciId(@Param("idStudentPodaci") Long idStudentPodaci);

	@Query("SELECT s.broj FROM StudentIndeks s WHERE s.godina = :godina AND s.studProgramOznaka = :studProgramOznaka AND s.aktivan = true ORDER BY s.broj ASC")
	List<Integer> findBrojeviByGodinaAndStudProgramOznaka(@Param("godina") int godina, @Param("studProgramOznaka") String studProgramOznaka);

    List<StudentIndeks> findByBrojAndAktivanTrue(int broj);

    @Query("SELECT COUNT(si) > 0 FROM StudentIndeks si WHERE si.broj = :broj AND si.godina = :godina AND si.studProgramOznaka = :studProgramOznaka AND si.aktivan = true AND (:id IS NULL OR si.id != :id)")
    boolean existsByBrojGodinaStudProgramOznakaAndAktivanAndIdNot(@Param("broj") int broj, @Param("godina") int godina, @Param("studProgramOznaka") String studProgramOznaka, @Param("id") Long id);

	// Provera da li student vec ima indeks u istoj godini ILI na istom studijskom programu
    @Query("SELECT COUNT(si) > 0 FROM StudentIndeks si WHERE si.student.id = :studentId AND (si.godina = :godina OR si.studProgramOznaka = :studProgramOznaka) AND (:id IS NULL OR si.id != :id)")
    boolean existsByStudentIdAndGodinaOrStudProgramOznakaAndIdNot(
        @Param("studentId") Long studentId, 
        @Param("godina") int godina, 
        @Param("studProgramOznaka") String studProgramOznaka,
        @Param("id") Long id);

    // Dohvati sve indekse za studenta (za deaktivaciju prilikom aktivacije novog)
    @Query("SELECT si FROM StudentIndeks si WHERE si.student.id = :studentId")
    List<StudentIndeks> findAllByStudentId(@Param("studentId") Long studentId);

    // Za kaskadno brisanje StudijskogPrograma
    @Query("SELECT si FROM StudentIndeks si WHERE si.studijskiProgram.id = :studijskiProgramId")
    List<StudentIndeks> findByStudijskiProgramId(@Param("studijskiProgramId") Long studijskiProgramId);

}
