package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPodaciRepository extends JpaRepository<StudentPodaci, Long> {

    @Query("SELECT sp FROM StudentPodaci sp WHERE sp.jmbg = :jmbg")
    Optional<StudentPodaci> findByJmbg(@Param("jmbg") String jmbg);

    @Query("SELECT sp FROM StudentPodaci sp WHERE sp.brojLicneKarte = :brojLicneKarte")
    Optional<StudentPodaci> findByBrojLicneKarte(@Param("brojLicneKarte") String brojLicneKarte);

    @Query("SELECT sp FROM StudentPodaci sp WHERE sp.emailFakultetski = :emailFakultetski")
    Optional<StudentPodaci> findByEmailFakultetski(@Param("emailFakultetski") String emailFakultetski);

    // da li postoji jos neki student sa ovim id-jem koji ima ovakav JMBG
    @Query("SELECT COUNT(sp) > 0 FROM StudentPodaci sp WHERE sp.jmbg = :jmbg AND (:id IS NULL OR sp.id != :id)")
    boolean existsByJmbgAndIdNot(@Param("jmbg") String jmbg, @Param("id") Long id);

    @Query("SELECT COUNT(sp) > 0 FROM StudentPodaci sp WHERE sp.brojLicneKarte = :brojLicneKarte AND (:id IS NULL OR sp.id != :id)")
    boolean existsByBrojLicneKarteAndIdNot(@Param("brojLicneKarte") String brojLicneKarte, @Param("id") Long id);

    @Query("SELECT COUNT(sp) > 0 FROM StudentPodaci sp WHERE sp.emailFakultetski = :emailFakultetski AND (:id IS NULL OR sp.id != :id)")
    boolean existsByEmailFakultetskiAndIdNot(@Param("emailFakultetski") String emailFakultetski, @Param("id") Long id);

    @Query("select sp from StudentPodaci sp where "
            + "(:ime is null or lower(sp.ime) like concat('%', lower(:ime), '%')) and "
            + "(:prezime is null or lower(sp.prezime) like concat('%', lower(:prezime), '%'))")
    Page<StudentPodaci> findStudent(String ime, String prezime, Pageable pageable);

    @Query("select si from StudentIndeks si where si.aktivan=true and si.student.id = :studPodaciId")
    StudentIndeks getAktivanIndeks(Long studPodaciId);

    @Query("select si from StudentIndeks si where si.aktivan=false and si.student.id = :studPodaciId")
    List<StudentIndeks> getNeaktivniIndeksi(Long studPodaciId);
}
