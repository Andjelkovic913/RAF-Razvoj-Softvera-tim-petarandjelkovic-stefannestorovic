package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    // Za kaskadno brisanje Predmeta
    @Query("SELECT i FROM Ispit i WHERE i.predmet.id = :predmetId")
    List<Ispit> findByPredmetId(@Param("predmetId") Long predmetId);

    // Za kaskadno brisanje DrziPredmet
    @Query("SELECT i FROM Ispit i WHERE i.drziPredmet.id = :drziPredmetId")
    List<Ispit> findByDrziPredmetId(@Param("drziPredmetId") Long drziPredmetId);
}

