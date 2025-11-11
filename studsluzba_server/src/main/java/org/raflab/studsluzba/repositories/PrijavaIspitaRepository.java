package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PrijavaIspita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrijavaIspitaRepository extends JpaRepository<PrijavaIspita, Long> {

    boolean existsByStudentIndeksIdAndIspitId(Long studentIndeksId, Long ispitId);

    List<PrijavaIspita> findAllByIspitId(Long ispitId);
}

