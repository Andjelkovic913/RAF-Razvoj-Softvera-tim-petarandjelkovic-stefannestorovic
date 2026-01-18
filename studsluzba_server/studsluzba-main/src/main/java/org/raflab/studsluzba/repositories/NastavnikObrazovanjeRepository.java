package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.NastavnikObrazovanje;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NastavnikObrazovanjeRepository extends CrudRepository<NastavnikObrazovanje, Long> {
    
    @Query("SELECT no FROM NastavnikObrazovanje no WHERE no.nastavnik.id = :nastavnikId")
    List<NastavnikObrazovanje> findByNastavnikId(@Param("nastavnikId") Long nastavnikId);
}

