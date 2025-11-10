package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudijskiProgramService {

    private final StudijskiProgramRepository studijskiProgramRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public Long createStudijskiProgram(StudijskiProgramRequest request) {
        StudijskiProgram studijskiProgram = converters.toStudijskiProgram(request);
        if (request.getSkolskaGodinaId() != null) {
            skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                    .ifPresent(studijskiProgram::setSkolskaGodina);
        }
        StudijskiProgram saved = studijskiProgramRepository.save(studijskiProgram);
        return saved.getId();
    }

    public List<StudijskiProgramResponse> getAllSortedDesc() {
        return studijskiProgramRepository.getAllSortedByGodinaDesc().stream()
                .map(entityMappers::fromStudijskiProgramToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<String> getAllOznaka() {
        return studijskiProgramRepository.findAllOznaka();
    }

    public StudijskiProgramResponse updateStudijskiProgram(Long id, StudijskiProgramRequest request) {
        StudijskiProgram program = studijskiProgramRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Ne postoji ovaj studijski program"));
        converters.updateStudijskiProgram(program, request);
        StudijskiProgram updated = studijskiProgramRepository.save(program);
        return entityMappers.fromStudijskiProgramToResponse(updated);
    }

    public void deleteStudijskiProgram(Long id) {
        studijskiProgramRepository.deleteById(id);
    }

    public List<StudijskiProgram> findByOznaka(String oznaka) {
        return studijskiProgramRepository.findByOznaka(oznaka);
    }
}

