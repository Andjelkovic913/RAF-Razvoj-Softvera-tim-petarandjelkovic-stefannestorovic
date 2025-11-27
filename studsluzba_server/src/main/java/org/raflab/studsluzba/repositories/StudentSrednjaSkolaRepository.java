package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentSrednjaSkola;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSrednjaSkolaRepository extends CrudRepository<StudentSrednjaSkola, Long> {
    
    @Query("SELECT sss FROM StudentSrednjaSkola sss " +
           "WHERE sss.srednjaSkola.id = :srednjaSkolaId " +
           "ORDER BY sss.student.prezime ASC, sss.student.ime ASC")
    List<StudentSrednjaSkola> findEnrolledStudentsBySrednjaSkola(@Param("srednjaSkolaId") Long srednjaSkolaId);

    @Query("SELECT sss FROM StudentSrednjaSkola sss WHERE sss.student.id = :studentId")
    List<StudentSrednjaSkola> findByStudentId(@Param("studentId") Long studentId);
}

