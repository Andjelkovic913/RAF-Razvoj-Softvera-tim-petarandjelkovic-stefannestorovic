package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentSrednjaSkola;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSrednjaSkolaRepository extends CrudRepository<StudentSrednjaSkola, Long> {
}

