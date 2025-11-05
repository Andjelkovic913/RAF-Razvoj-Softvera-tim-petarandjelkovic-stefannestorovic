package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitneObaveze;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredispitneObavezeRepository extends CrudRepository<PredispitneObaveze, Long> {
}

