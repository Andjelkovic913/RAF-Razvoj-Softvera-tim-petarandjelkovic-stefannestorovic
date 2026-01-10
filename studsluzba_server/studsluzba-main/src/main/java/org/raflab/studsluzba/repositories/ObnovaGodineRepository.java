package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.ObnovaGodine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObnovaGodineRepository extends JpaRepository<ObnovaGodine, Long> {
    
    @Query("SELECT og FROM ObnovaGodine og WHERE og.studentIndeks.id = :studentIndeksId ORDER BY og.datumUpisa DESC")
    List<ObnovaGodine> findByStudentIndeksId(@Param("studentIndeksId") Long studentIndeksId);
}

