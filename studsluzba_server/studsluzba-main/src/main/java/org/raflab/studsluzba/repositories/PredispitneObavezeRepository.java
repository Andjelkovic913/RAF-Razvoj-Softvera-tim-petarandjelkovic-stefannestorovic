package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitneObaveze;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredispitneObavezeRepository extends CrudRepository<PredispitneObaveze, Long> {
    
    @Query("SELECT po FROM PredispitneObaveze po WHERE po.slusaPredmet.id = :slusaPredmetId")
    List<PredispitneObaveze> findBySlusaPredmetId(@Param("slusaPredmetId") Long slusaPredmetId);
}

