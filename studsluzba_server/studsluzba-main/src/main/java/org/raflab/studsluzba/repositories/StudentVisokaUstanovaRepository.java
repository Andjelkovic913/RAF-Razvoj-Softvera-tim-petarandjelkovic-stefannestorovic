package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentVisokaUstanova;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentVisokaUstanovaRepository extends CrudRepository<StudentVisokaUstanova, Long> {
    
    @Query("SELECT svu FROM StudentVisokaUstanova svu WHERE svu.student.id = :studentId")
    List<StudentVisokaUstanova> findByStudentId(@Param("studentId") Long studentId);
    
    // Za kaskadno brisanje VisokoskolskeUstanove
    @Query("SELECT svu FROM StudentVisokaUstanova svu WHERE svu.visokaUstanova.id = :ustanovaId")
    List<StudentVisokaUstanova> findByVisokoskolskaUstanovaId(@Param("ustanovaId") Long ustanovaId);
}

