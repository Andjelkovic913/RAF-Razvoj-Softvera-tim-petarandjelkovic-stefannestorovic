package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Uplata;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UplataRepository extends JpaRepository<Uplata, Long> {

    /**
     * Dohvata sve uplate za određenog studenta
     */
    @Query("SELECT u FROM Uplata u LEFT JOIN FETCH u.skolskaGodina WHERE u.studentIndeks.id = :studentIndeksId")
    List<Uplata> findByStudentIndeksId(@Param("studentIndeksId") Long studentIndeksId);

    /**
     * Dohvata sve uplate za određenu školsku godinu
     */
    @Query("SELECT u FROM Uplata u WHERE u.skolskaGodina.id = :skolskaGodinaId")
    List<Uplata> findBySkolskaGodinaId(@Param("skolskaGodinaId") Long skolskaGodinaId);

    /**
     * Dohvata sve uplate sa određenim statusom
     */
    @Query("SELECT u FROM Uplata u WHERE u.status = :status")
    List<Uplata> findByStatus(@Param("status") Uplata.StatusUplate status);
}

