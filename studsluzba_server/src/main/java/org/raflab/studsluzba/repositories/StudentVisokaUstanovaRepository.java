package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentVisokaUstanova;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentVisokaUstanovaRepository extends CrudRepository<StudentVisokaUstanova, Long> {
}

