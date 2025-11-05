package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkolskaGodinaRepository extends CrudRepository<SkolskaGodina, Long> {
}

