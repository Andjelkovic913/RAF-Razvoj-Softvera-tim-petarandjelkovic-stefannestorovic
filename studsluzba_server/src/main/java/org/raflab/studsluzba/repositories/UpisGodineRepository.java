package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.UpisGodine;
import org.raflab.studsluzba.model.dtos.StudentReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpisGodineRepository extends JpaRepository<UpisGodine, Long> {
    
    @Query("SELECT ug FROM UpisGodine ug WHERE ug.studentIndeks.id = :studentIndeksId ORDER BY ug.datumUpisa DESC")
    List<UpisGodine> findByStudentIndeksId(@Param("studentIndeksId") Long studentIndeksId);
    
    @Query("SELECT ug FROM UpisGodine ug WHERE ug.studentIndeks.broj = :broj ORDER BY ug.datumUpisa DESC")
    List<UpisGodine> findByBrojIndeksa(@Param("broj") int broj);
    
    @Query("SELECT ug FROM UpisGodine ug WHERE ug.studentIndeks.id = :studentIndeksId AND ug.skolskaGodina.id = :skolskaGodinaId AND ug.godina = :godina")
    List<UpisGodine> findByStudentIndeksAndSkolskaGodinaAndGodina(
            @Param("studentIndeksId") Long studentIndeksId,
            @Param("skolskaGodinaId") Long skolskaGodinaId,
            @Param("godina") Integer godina);
    
    // Za kaskadno brisanje SkolskeGodine
    @Query("SELECT ug FROM UpisGodine ug WHERE ug.skolskaGodina.id = :skolskaGodinaId")
    List<UpisGodine> findBySkolskaGodinaId(@Param("skolskaGodinaId") Long skolskaGodinaId);
    
    // Optimized query for report - gets first enrollment year for each student in single query
    @Query("SELECT new org.raflab.studsluzba.model.dtos.StudentReportDTO(" +
           "sp.ime, sp.prezime, YEAR(MIN(ug.datumUpisa))) " +
           "FROM UpisGodine ug " +
           "JOIN ug.studentIndeks si " +
           "JOIN si.student sp " +
           "GROUP BY si.id, sp.ime, sp.prezime")
    List<StudentReportDTO> findAllStudentsForReport();
}

