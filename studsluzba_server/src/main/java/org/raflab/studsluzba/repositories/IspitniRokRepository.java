package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IspitniRok;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IspitniRokRepository extends CrudRepository<IspitniRok, Long> {
    
    // Za kaskadno brisanje SkolskeGodine
    @Query("SELECT ir FROM IspitniRok ir WHERE ir.skolskaGodina.id = :skolskaGodinaId")
    List<IspitniRok> findBySkolskaGodinaId(@Param("skolskaGodinaId") Long skolskaGodinaId);
}

