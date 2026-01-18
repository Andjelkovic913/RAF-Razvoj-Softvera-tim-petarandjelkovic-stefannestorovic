package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<IzlazakNaIspit, Long> {
    
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.prijava.studentIndeks.id = :studentIndeksId AND i.ocena >= 6")
    Page<IzlazakNaIspit> findPassedExamsByStudentIndeks(@Param("studentIndeksId") Long studentIndeksId, Pageable pageable);
    
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.prijava.studentIndeks.id = :studentIndeksId AND (i.ocena < 6 OR i.ocena IS NULL)")
    Page<IzlazakNaIspit> findFailedExamsByStudentIndeks(@Param("studentIndeksId") Long studentIndeksId, Pageable pageable);
    
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.slusaPredmet.id = :slusaPredmetId")
    List<IzlazakNaIspit> findBySlusaPredmetId(@Param("slusaPredmetId") Long slusaPredmetId);
    
    @Query("SELECT COUNT(i) FROM IzlazakNaIspit i WHERE i.prijava.studentIndeks.id = :studentIndeksId AND i.slusaPredmet.drziPredmet.predmet.id = :predmetId")
    Long countExamAttemptsByStudentAndPredmet(@Param("studentIndeksId") Long studentIndeksId, @Param("predmetId") Long predmetId);

    // Za kaskadno brisanje Ispita
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.prijava.ispit.id = :ispitId")
    List<IzlazakNaIspit> findByIspitId(@Param("ispitId") Long ispitId);


    @Query("SELECT AVG(i.ocena * 1.0) FROM IzlazakNaIspit i " +
           "JOIN i.prijava p " +
           "JOIN p.ispit isp " +
           "WHERE isp.predmet.id = :predmetId " +
           "AND i.ocena >= 6 " +
           "AND isp.datumOdrzavanja >= :startDate " +
           "AND isp.datumOdrzavanja <= :endDate")
    Double calculateAverageGradeByPredmetAndDateRange(
            @Param("predmetId") Long predmetId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query("SELECT COUNT(i) FROM IzlazakNaIspit i " +
           "JOIN i.prijava p " +
           "JOIN p.ispit isp " +
           "WHERE isp.predmet.id = :predmetId " +
           "AND i.ocena >= 6 " +
           "AND isp.datumOdrzavanja >= :startDate " +
           "AND isp.datumOdrzavanja <= :endDate")
    Long countPassedExamsByPredmetAndDateRange(
            @Param("predmetId") Long predmetId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query("SELECT AVG(i.ocena * 1.0) FROM IzlazakNaIspit i " +
           "JOIN i.prijava p " +
           "JOIN p.ispit isp " +
           "WHERE isp.predmet.id = :predmetId " +
           "AND i.ocena >= 6 " +
           "AND YEAR(isp.datumOdrzavanja) = :year")
    Double calculateAverageGradeByPredmetAndYear(
            @Param("predmetId") Long predmetId,
            @Param("year") Integer year);


    @Query("SELECT COUNT(i) FROM IzlazakNaIspit i " +
           "JOIN i.prijava p " +
           "JOIN p.ispit isp " +
           "WHERE isp.predmet.id = :predmetId " +
           "AND i.ocena >= 6 " +
           "AND YEAR(isp.datumOdrzavanja) = :year")
    Long countPassedExamsByPredmetAndYear(
            @Param("predmetId") Long predmetId,
            @Param("year") Integer year);


    @Query("SELECT COUNT(i) FROM IzlazakNaIspit i " +
           "JOIN i.prijava p " +
           "JOIN p.ispit isp " +
           "WHERE isp.predmet.id = :predmetId " +
           "AND YEAR(isp.datumOdrzavanja) = :year")
    Long countTotalExamAttemptsByPredmetAndYear(
            @Param("predmetId") Long predmetId,
            @Param("year") Integer year);

}
