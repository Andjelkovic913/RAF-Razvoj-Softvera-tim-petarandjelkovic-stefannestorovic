package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentVisokaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.StudentVisokaUstanovaResponse;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudentVisokaUstanova;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudentVisokaUstanovaRepository;
import org.raflab.studsluzba.repositories.VisokoskolskaUstanovaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StudentVisokaUstanovaService {

    private final StudentVisokaUstanovaRepository studentVisokaUstanovaRepository;
    private final StudentPodaciRepository studentPodaciRepository;
    private final VisokoskolskaUstanovaRepository visokaUstanovaRepository;
    private final EntityMappers entityMappers;

    /**
     * Dodeljuje visokoškolsku ustanovu studentu (prenosi predmete).
     */
    @Transactional
    public Long addStudentToVisokaUstanova(StudentVisokaUstanovaRequest request) {
        StudentPodaci student = studentPodaciRepository.findById(request.getStudentPodaciId())
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + request.getStudentPodaciId() + " ne postoji"));
        
        VisokoskolskaUstanova vu = visokaUstanovaRepository.findById(request.getVisokaUstanovaId())
                .orElseThrow(() -> new EntityNotFoundException("Visokoškolska ustanova sa ID " + request.getVisokaUstanovaId() + " ne postoji"));
        
        StudentVisokaUstanova svu = new StudentVisokaUstanova();
        svu.setStudent(student);
        svu.setVisokaUstanova(vu);
        svu.setDatumUpisa(request.getDatumUpisa());
        svu.setDatumZavrsetka(request.getDatumZavrsetka());
        svu.setNapomena(request.getNapomena());
        
        StudentVisokaUstanova saved = studentVisokaUstanovaRepository.save(svu);
        return saved.getId();
    }

    /**
     * Pronalazi vezu između studenta i visokoškolske ustanove po ID-u.
     */
    @Transactional(readOnly = true)
    public StudentVisokaUstanovaResponse getStudentVisokaUstanovaById(Long id) {
        StudentVisokaUstanova svu = studentVisokaUstanovaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veza student-visoka ustanova sa ID " + id + " ne postoji"));
        return entityMappers.fromStudentVisokaUstanovaToResponse(svu);
    }

    /**
     * Pronalazi sve visokoškolske ustanove za određenog studenta.
     */
    @Transactional(readOnly = true)
    public List<StudentVisokaUstanovaResponse> getStudentVisokeUstanoveByStudentId(Long studentPodaciId) {
        if (!studentPodaciRepository.existsById(studentPodaciId)) {
            throw new EntityNotFoundException("Student sa ID " + studentPodaciId + " ne postoji");
        }
        
        List<StudentVisokaUstanova> studentVisokeUstanove = StreamSupport.stream(
                studentVisokaUstanovaRepository.findAll().spliterator(), false)
                .filter(svu -> svu.getStudent().getId().equals(studentPodaciId))
                .collect(Collectors.toList());
        
        return entityMappers.fromStudentVisokaUstanovaListToResponse(studentVisokeUstanove);
    }

    /**
     * Ažurira vezu između studenta i visokoškolske ustanove.
     */
    @Transactional
    public StudentVisokaUstanovaResponse updateStudentVisokaUstanova(Long id, StudentVisokaUstanovaRequest request) {
        StudentVisokaUstanova svu = studentVisokaUstanovaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veza student-visoka ustanova sa ID " + id + " ne postoji"));
        
        svu.setDatumUpisa(request.getDatumUpisa());
        svu.setDatumZavrsetka(request.getDatumZavrsetka());
        svu.setNapomena(request.getNapomena());
        
        StudentVisokaUstanova updated = studentVisokaUstanovaRepository.save(svu);
        return entityMappers.fromStudentVisokaUstanovaToResponse(updated);
    }

    /**
     * Briše vezu između studenta i visokoškolske ustanove.
     */
    @Transactional
    public void deleteStudentVisokaUstanova(Long id) {
        if (!studentVisokaUstanovaRepository.existsById(id)) {
            throw new EntityNotFoundException("Veza student-visoka ustanova sa ID " + id + " ne postoji");
        }
        studentVisokaUstanovaRepository.deleteById(id);
    }
}
