package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.UpisGodine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpisGodineRepository extends CrudRepository<UpisGodine, Long> {
}

