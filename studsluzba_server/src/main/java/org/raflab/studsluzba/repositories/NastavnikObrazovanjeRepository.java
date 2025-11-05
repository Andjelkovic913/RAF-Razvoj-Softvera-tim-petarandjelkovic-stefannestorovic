package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.NastavnikObrazovanje;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NastavnikObrazovanjeRepository extends CrudRepository<NastavnikObrazovanje, Long> {
}

