package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IspitRepository extends JpaRepository<Ispit, Long> {

    @Query("SELECT i FROM Ispit i " +
           "LEFT JOIN FETCH i.drziPredmet dp " +
           "LEFT JOIN FETCH dp.predmet " +
           "LEFT JOIN FETCH i.predmet " +
           "LEFT JOIN FETCH i.ispitniRok " +
           "WHERE i.id = :id")
    Optional<Ispit> findByIdWithRelations(@Param("id") Long id);
}

