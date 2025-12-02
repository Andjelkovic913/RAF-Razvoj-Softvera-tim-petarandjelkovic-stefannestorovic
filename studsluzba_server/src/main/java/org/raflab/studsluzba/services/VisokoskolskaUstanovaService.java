package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.VisokoskolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoskolskaUstanovaResponse;
import org.raflab.studsluzba.model.StudentVisokaUstanova;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.raflab.studsluzba.repositories.StudentVisokaUstanovaRepository;
import org.raflab.studsluzba.repositories.VisokoskolskaUstanovaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class VisokoskolskaUstanovaService {

    private final VisokoskolskaUstanovaRepository visokaUstanovaRepository;
    private final StudentVisokaUstanovaRepository studentVisokaUstanovaRepository;
    private final EntityMappers entityMappers;

    /**
     * Kreira novu visokoškolsku ustanovu.
     */
    @Transactional
    public Long createVisokoskolskaUstanova(VisokoskolskaUstanovaRequest request) {
        VisokoskolskaUstanova vu = new VisokoskolskaUstanova();
        vu.setNaziv(request.getNaziv());
        vu.setTipSkole(request.getTipSkole());
        
        VisokoskolskaUstanova saved = visokaUstanovaRepository.save(vu);
        return saved.getId();
    }

    /**
     * Pronalazi visokoškolsku ustanovu po ID-u.
     */
    @Transactional(readOnly = true)
    public VisokoskolskaUstanovaResponse getVisokoskolskaUstanovaById(Long id) {
        VisokoskolskaUstanova vu = visokaUstanovaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visokoškolska ustanova sa ID " + id + " ne postoji"));
        return entityMappers.fromVisokoskolskaUstanovaToResponse(vu);
    }

    /**
     * Pronalazi sve visokoškolske ustanove.
     */
    @Transactional(readOnly = true)
    public List<VisokoskolskaUstanovaResponse> getAllVisokoskolskeUstanove() {
        List<VisokoskolskaUstanova> ustanove = StreamSupport.stream(
                visokaUstanovaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return entityMappers.fromVisokoskolskaUstanovaListToResponse(ustanove);
    }

    /**
     * Ažurira visokoškolsku ustanovu.
     */
    @Transactional
    public VisokoskolskaUstanovaResponse updateVisokoskolskaUstanova(Long id, VisokoskolskaUstanovaRequest request) {
        VisokoskolskaUstanova vu = visokaUstanovaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visokoškolska ustanova sa ID " + id + " ne postoji"));
        
        vu.setNaziv(request.getNaziv());
        vu.setTipSkole(request.getTipSkole());
        
        VisokoskolskaUstanova updated = visokaUstanovaRepository.save(vu);
        return entityMappers.fromVisokoskolskaUstanovaToResponse(updated);
    }

    /**
     * Briše visokoškolsku ustanovu i sve zavisne entitete (kaskadno brisanje).
     * Redosled: StudentVisokaUstanova -> VisokoskolskaUstanova
     */
    @Transactional
    public void deleteVisokoskolskaUstanova(Long id) {
        if (!visokaUstanovaRepository.existsById(id)) {
            throw new EntityNotFoundException("Visokoškolska ustanova sa ID " + id + " ne postoji");
        }
        
        // 1. Obrisi sve StudentVisokaUstanova za ovu visokoskolsku ustanovu
        List<StudentVisokaUstanova> studentVisokeUstanove = studentVisokaUstanovaRepository.findByVisokoskolskaUstanovaId(id);
        studentVisokaUstanovaRepository.deleteAll(studentVisokeUstanove);
        
        // 2. Na kraju obrisi samu visokoskolsku ustanovu
        visokaUstanovaRepository.deleteById(id);
    }
}
