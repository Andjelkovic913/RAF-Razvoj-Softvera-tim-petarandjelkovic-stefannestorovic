package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.StudentBySrednjaSkolaResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentSrednjaSkola;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentSrednjaSkolaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentSrednjaSkolaService {

    private final StudentSrednjaSkolaRepository studentSrednjaSkolaRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final EntityMappers entityMappers;

    /**
     * Pronalazi sve upisane studente koji su završili određenu srednju školu.
     * Vraća samo aktivne studentske indekse.
     *
     * @param srednjaSkolaId ID srednje škole
     * @return Lista studenata sa njihovim podacima o srednjoj školi
     */
    @Transactional(readOnly = true)
    public List<StudentBySrednjaSkolaResponse> findEnrolledStudentsBySrednjaSkola(Long srednjaSkolaId) {
        List<StudentSrednjaSkola> studentSrednjaSkolas = 
            studentSrednjaSkolaRepository.findEnrolledStudentsBySrednjaSkola(srednjaSkolaId);
        
        // Return empty list if no students found, instead of throwing exception
        if (studentSrednjaSkolas == null || studentSrednjaSkolas.isEmpty()) {
            return List.of();
        }
        
        return studentSrednjaSkolas.stream()
                .map(sss -> {
                    StudentIndeks si = studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(sss.getStudent().getId());
                    return entityMappers.fromStudentSrednjaSkolaToResponse(sss, si);
                })
                .collect(Collectors.toList());
    }
}
