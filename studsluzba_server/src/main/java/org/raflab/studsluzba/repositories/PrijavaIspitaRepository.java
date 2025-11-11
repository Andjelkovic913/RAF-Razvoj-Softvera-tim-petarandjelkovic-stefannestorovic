package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PrijavaIspita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrijavaIspitaRepository extends JpaRepository<PrijavaIspita, Long> {

    boolean existsByStudentIndeksIdAndIspitId(Long studentIndeksId, Long ispitId);

    @Query("SELECT p FROM PrijavaIspita p " +
           "LEFT JOIN FETCH p.ispit i " +
           "LEFT JOIN FETCH i.predmet " +
           "LEFT JOIN FETCH i.ispitniRok " +
           "LEFT JOIN FETCH p.studentIndeks si " +
           "LEFT JOIN FETCH si.student " +
           "WHERE i.id = :ispitId")
    List<PrijavaIspita> findAllByIspitId(@Param("ispitId") Long ispitId);
}

