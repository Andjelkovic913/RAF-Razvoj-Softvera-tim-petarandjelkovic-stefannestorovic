package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PrijavaIspita;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrijavaIspitaRepository extends CrudRepository<PrijavaIspita, Long> {
}

