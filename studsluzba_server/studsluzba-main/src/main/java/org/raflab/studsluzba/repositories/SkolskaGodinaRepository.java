package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkolskaGodinaRepository extends CrudRepository<SkolskaGodina, Long> {
    
    /**
     * Pronalazi aktivnu skolsku godinu
     */
    Optional<SkolskaGodina> findByAktivnaTrue();
}

