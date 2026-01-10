package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.NastavnikZvanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NastavnikZvanjeRepository extends JpaRepository<NastavnikZvanje, Long> {
    
    @Query("SELECT nz FROM NastavnikZvanje nz WHERE nz.nastavnik.id = :nastavnikId")
    List<NastavnikZvanje> findByNastavnikId(@Param("nastavnikId") Long nastavnikId);
    
    @Query("SELECT nz FROM NastavnikZvanje nz WHERE nz.zvanje = :zvanje ORDER BY nz.nastavnik.prezime ASC, nz.nastavnik.ime ASC")
    List<NastavnikZvanje> findByZvanje(@Param("zvanje") String zvanje);
}
