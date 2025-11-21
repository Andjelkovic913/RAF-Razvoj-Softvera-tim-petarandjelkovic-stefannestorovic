package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<IzlazakNaIspit, Long> {
    
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.prijava.studentIndeks.id = :studentIndeksId AND i.ocena >= 6")
    Page<IzlazakNaIspit> findPassedExamsByStudentIndeks(@Param("studentIndeksId") Long studentIndeksId, Pageable pageable);
    
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.prijava.studentIndeks.id = :studentIndeksId AND (i.ocena < 6 OR i.ocena IS NULL)")
    Page<IzlazakNaIspit> findFailedExamsByStudentIndeks(@Param("studentIndeksId") Long studentIndeksId, Pageable pageable);
}
