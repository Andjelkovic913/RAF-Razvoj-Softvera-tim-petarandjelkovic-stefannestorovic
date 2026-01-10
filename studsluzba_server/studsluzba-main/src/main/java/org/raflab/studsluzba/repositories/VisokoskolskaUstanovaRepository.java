package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisokoskolskaUstanovaRepository extends CrudRepository<VisokoskolskaUstanova, Long> {
}

