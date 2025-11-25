package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentPodaciRepository studentPodaciRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    // StudentPodaci operations
    public Long createStudentPodaci(StudentPodaciRequest request) {
        StudentPodaci studentPodaci = converters.toStudentPodaci(request);
        StudentPodaci saved = studentPodaciRepository.save(studentPodaci);
        return saved.getId();
    }

    public List<StudentPodaciResponse> getAllStudentPodaci() {
        return studentPodaciRepository.findAll().stream()
                .map(entityMappers::fromStudentPodaciToResponse)
                .collect(Collectors.toList());
    }

    public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(Integer page, Integer size) {
        return studentPodaciRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(entityMappers::fromStudentPodaciToResponse);
    }

    // selekcija studenta (njegovih ličnih podataka) preko ID-a
    public StudentPodaciResponse getStudentPodaciById(Long id) {
        return studentPodaciRepository.findById(id)
                .map(entityMappers::fromStudentPodaciToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + id + " ne postoji"));
    }

    public StudentPodaciResponse updateStudentPodaci(Long id, StudentPodaciRequest request) {
        StudentPodaci student = studentPodaciRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student ne postoji"));
        converters.updateStudentPodaci(student, request);
        StudentPodaci updated = studentPodaciRepository.save(student);
        return entityMappers.fromStudentPodaciToResponse(updated);
    }

    public void deleteStudentPodaci(Long id) {
        studentPodaciRepository.deleteById(id);
    }

    //  selekcija studenta (njegovih ličnih podataka) preko broja indeksa, studProgramOznaka i godine
    public StudentPodaciResponse getStudentPodaciByBroj(int broj, String studProgramOznaka, Integer godina) {
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(studProgramOznaka.toUpperCase(), godina, broj);
        if (si == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Student sa indeksom %s %d/%d ne postoji", studProgramOznaka, broj, godina));
        }
        return entityMappers.fromStudentPodaciToResponse(si.getStudent());
    }

    public StudentPodaciResponse updateStudentPodaciByIndeks(String studProgramOznaka, int godina, int broj, StudentPodaciRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findStudentIndeks(studProgramOznaka.toUpperCase(), godina, broj);
        if (indeks == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Student sa indeksom %s %d/%d ne postoji", studProgramOznaka, broj, godina));
        }

        StudentPodaci studentPodaci = indeks.getStudent();
        converters.updateStudentPodaci(studentPodaci, request);
        StudentPodaci saved = studentPodaciRepository.save(studentPodaci);
        return entityMappers.fromStudentPodaciToResponse(saved);
    }

    // StudentIndeks operations
    @Transactional
    public Long saveIndeks(StudentIndeksRequest request) {
        StudentIndeks studentIndeks = converters.toStudentIndeks(request);

        int nextBroj = findNextBroj(request.getGodina(), request.getStudProgramOznaka());
        studentIndeks.setBroj(nextBroj); // trazi koji sve brojevi indeksa vec postoje i uzima nekorisceni

        StudentPodaci studentPodaci = studentPodaciRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + request.getStudentId() + " ne postoji"));
        studentIndeks.setStudent(studentPodaci);

        List<StudijskiProgram> studijskiProgrami = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka());
        if (studijskiProgrami.isEmpty()) {
            throw new RuntimeException("No StudijskiProgram found with oznaka: " + request.getStudProgramOznaka());
        }
        studentIndeks.setStudijskiProgram(studijskiProgrami.get(0));
        // trazi StudentskiProgram entitet na koji ce da ga poveze na osnovu oznake

        try {
            StudentIndeks savedStudentIndeks = studentIndeksRepository.save(studentIndeks);
            return savedStudentIndeks.getId();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Duplicate entry for broj, godina, and studProgramOznaka", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the StudentIndeks.", e);
        }
    }

    @Transactional(readOnly = true)
    public int findNextBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);
        return findNextAvailableNumber(brojeviList);
    }

    private int findNextAvailableNumber(List<Integer> brojeviList) {
        if (brojeviList == null || brojeviList.isEmpty()) {
            return 1;
        }

        List<Integer> sorted = brojeviList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int expected = 1;
        for (int num : sorted) {
            if (num != expected) {
                return expected;
            }
            expected++;
        }
        return expected;
    }

    public StudentIndeksResponse getStudentIndeksById(Long id) {
        return studentIndeksRepository.findById(id)
                .map(entityMappers::fromStudentIndexToResponse)
                .orElse(null);
    }

    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(Long idStudentPodaci) {
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci).stream()
                .map(entityMappers::fromStudentIndexToResponse)
                .collect(Collectors.toList());
    }

    // 2.1 - pronalaženje studenta preko skraćenog broja indeksa (npr. "RN1923")
    public StudentIndeksResponse fastSearch(String indeksShort) {
        String[] parsedData = ParseUtils.parseIndeks(indeksShort);
        if (parsedData == null) {
            throw new IllegalArgumentException("Neispravan format indeksa: " + indeksShort);
        }
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000 + Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]));
        if (si == null) {
            throw new EntityNotFoundException("Student sa indeksom " + indeksShort + " ne postoji");
        }
        return entityMappers.fromStudentIndexToResponse(si);
    }

    // 2.2 - pronalaženje studenta preko email-a
    public StudentIndeksResponse emailSearch(String studEmail) {
        String[] parsedData = ParseUtils.parseEmail(studEmail);
        if (parsedData == null) {
            throw new IllegalArgumentException("Neispravan format emaila: " + studEmail);
        }
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000 + Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]));
        if (si == null) {
            throw new EntityNotFoundException("Student sa emailom " + studEmail + " ne postoji");
        }
        return entityMappers.fromStudentIndexToResponse(si);
    }

    // 7 - selekcija studenata na osnovu imena i/ili prezimena (može samo ime, ili samo prezime ili oba da se unesu), paginirano
    public Page<StudentDTO> search(String ime, String prezime, String studProgram, Integer godina, Integer broj, Integer page, Integer size) {
        Page<StudentIndeks> siList = studentIndeksRepository.findStudentIndeks(ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()));
        return siList.map(EntityMappers::fromStudentIndeksToDTO);
    }

    public StudentIndeks findByStudentIdAndAktivan(Long studentPodaciId) {
        return studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
    }
}

