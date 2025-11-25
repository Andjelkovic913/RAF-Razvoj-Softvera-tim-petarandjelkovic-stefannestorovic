package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.response.IzlazakNaIspitResponse;
import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class IzlazakNaIspitService {

    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final EntityMappers entityMappers;

    @Transactional(readOnly = true)
    public Page<IzlazakNaIspitResponse> getPassedExams(Long studentIndeksId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return izlazakNaIspitRepository.findPassedExamsByStudentIndeks(studentIndeksId, pageable)//uzme polozene
                .map(entityMappers::fromIzlazakNaIspitToResponse);//namapira
    }

    @Transactional(readOnly = true)
    public Page<IzlazakNaIspitResponse> getFailedExams(Long studentIndeksId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return izlazakNaIspitRepository.findFailedExamsByStudentIndeks(studentIndeksId, pageable)
                .map(entityMappers::fromIzlazakNaIspitToResponse);
    }

    @Transactional(readOnly = true)
    public Page<IzlazakNaIspitResponse> getAllExamResults(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return izlazakNaIspitRepository.findAll(pageable)
                .map(entityMappers::fromIzlazakNaIspitToResponse);
    }

    @Transactional
    public Long createExamResult(IzlazakNaIspitRequest request) {
        PrijavaIspita prijava = prijavaIspitaRepository.findById(request.getPrijavaIspitaId())
                .orElseThrow(() -> new EntityNotFoundException("PrijavaIspita sa ID " + request.getPrijavaIspitaId() + " ne postoji"));
        
        SlusaPredmet slusaPredmet = slusaPredmetRepository.findById(request.getSlusaPredmetId())
                .orElseThrow(() -> new EntityNotFoundException("SlusaPredmet sa ID " + request.getSlusaPredmetId() + " ne postoji"));
        
        // Provera da li vec postoji IzlazakNaIspit za ovu PrijavaIspita
        IzlazakNaIspit izlazak = prijava.getIzlazakNaIspit();
        
        if (izlazak == null) {
            izlazak = new IzlazakNaIspit();
            izlazak.setPrijava(prijava);
        }
        
        izlazak.setSlusaPredmet(slusaPredmet);
        izlazak.setOsvojeniPredispitniPoeni(request.getOsvojeniPredispitniPoeni());
        izlazak.setOsvojeniIspitniPoeni(request.getOsvojeniIspitniPoeni());
        izlazak.setUkupnoPoena(request.getUkupnoPoena());
        izlazak.setOcena(request.getOcena()); // pomocna metoda potrebna
        izlazak.setPonistio(request.isPonistio());
        izlazak.setNapomena(request.getNapomena());
        
        // if (total >= 51 && !request.isPonistio()) 
        // u SlusaPredmet dodati polozen - boolean

        IzlazakNaIspit saved = izlazakNaIspitRepository.save(izlazak);
        return saved.getId();
    }
    
    @Transactional(readOnly = true)
    public Long getExamAttemptCount(Long studentIndeksId, Long predmetId) {
        return izlazakNaIspitRepository.countExamAttemptsByStudentAndPredmet(studentIndeksId, predmetId);
    }
}

