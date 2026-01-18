package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentSrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.StudentSrednjaSkolaResponse;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudentSrednjaSkola;
import org.raflab.studsluzba.model.SrednjaSkola;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudentSrednjaSkolaRepository;
import org.raflab.studsluzba.repositories.SrednjaSkolaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StudentSrednjaSkolaManagementService {

    private final StudentSrednjaSkolaRepository studentSrednjaSkolaRepository;
    private final StudentPodaciRepository studentPodaciRepository;
    private final SrednjaSkolaRepository srednjaSkolaRepository;
    private final EntityMappers entityMappers;

    /**
     * Dodeljuje srednju školu studentu.
     */
    @Transactional
    public Long addStudentToSrednjaSkola(StudentSrednjaSkolaRequest request) {
        StudentPodaci student = studentPodaciRepository.findById(request.getStudentPodaciId())
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + request.getStudentPodaciId() + " ne postoji"));
        
        SrednjaSkola ss = srednjaSkolaRepository.findById(request.getSrednjaSkolaId())
                .orElseThrow(() -> new EntityNotFoundException("Srednja škola sa ID " + request.getSrednjaSkolaId() + " ne postoji"));
        
        StudentSrednjaSkola sss = new StudentSrednjaSkola();
        sss.setStudent(student);
        sss.setSrednjaSkola(ss);
        sss.setDatumUpisa(request.getDatumUpisa());
        sss.setDatumZavrsetka(request.getDatumZavrsetka());
        sss.setUspehSaPrijemnog(request.getUspehSaPrijemnog());
        sss.setUspehIzSrednjeSkole(request.getUspehIzSrednjeSkole());
        
        StudentSrednjaSkola saved = studentSrednjaSkolaRepository.save(sss);
        return saved.getId();
    }

    /**
     * Pronalazi vezu između studenta i srednje škole po ID-u.
     */
    @Transactional(readOnly = true)
    public StudentSrednjaSkolaResponse getStudentSrednjaSkolaById(Long id) {
        StudentSrednjaSkola sss = studentSrednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veza student-srednja škola sa ID " + id + " ne postoji"));
        return entityMappers.fromStudentSrednjaSkolaToDetailResponse(sss);
    }

    /**
     * Pronalazi sve srednje škole za određenog studenta.
     */
    @Transactional(readOnly = true)
    public List<StudentSrednjaSkolaResponse> getStudentSrednjeSkolaByStudentId(Long studentPodaciId) {
        if (!studentPodaciRepository.existsById(studentPodaciId)) {
            throw new EntityNotFoundException("Student sa ID " + studentPodaciId + " ne postoji");
        }
        
        List<StudentSrednjaSkola> studentSrednjaSkolas = StreamSupport.stream(
                studentSrednjaSkolaRepository.findAll().spliterator(), false)
                .filter(sss -> sss.getStudent().getId().equals(studentPodaciId))
                .collect(Collectors.toList());
        
        return entityMappers.fromStudentSrednjaSkolaListToDetailResponse(studentSrednjaSkolas);
    }

    /**
     * Ažurira vezu između studenta i srednje škole.
     */
    @Transactional
    public StudentSrednjaSkolaResponse updateStudentSrednjaSkola(Long id, StudentSrednjaSkolaRequest request) {
        StudentSrednjaSkola sss = studentSrednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veza student-srednja škola sa ID " + id + " ne postoji"));
        
        sss.setDatumUpisa(request.getDatumUpisa());
        sss.setDatumZavrsetka(request.getDatumZavrsetka());
        sss.setUspehSaPrijemnog(request.getUspehSaPrijemnog());
        sss.setUspehIzSrednjeSkole(request.getUspehIzSrednjeSkole());
        
        StudentSrednjaSkola updated = studentSrednjaSkolaRepository.save(sss);
        return entityMappers.fromStudentSrednjaSkolaToDetailResponse(updated);
    }

    /**
     * Briše vezu između studenta i srednje škole.
     */
    @Transactional
    public void deleteStudentSrednjaSkola(Long id) {
        if (!studentSrednjaSkolaRepository.existsById(id)) {
            throw new EntityNotFoundException("Veza student-srednja škola sa ID " + id + " ne postoji");
        }
        studentSrednjaSkolaRepository.deleteById(id);
    }
}
