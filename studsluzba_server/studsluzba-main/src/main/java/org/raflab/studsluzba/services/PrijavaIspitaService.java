package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.dtos.PrijavljeniStudentDTO;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrijavaIspitaService {

    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final IspitRepository ispitRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    @Transactional
    public Long create(PrijavaIspitaRequest request) {
        Long studentIndeksId = request.getStudentIndeksId();
        Long ispitId = request.getIspitId();

        if (prijavaIspitaRepository.existsByStudentIndeksIdAndIspitId(studentIndeksId, ispitId)) {
            throw new IllegalStateException("Prijava za zadati ispit vec postoji za ovog studenta.");
        }

        StudentIndeks studentIndeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new EntityNotFoundException("Student indeks sa ID " + studentIndeksId + " ne postoji"));

        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new EntityNotFoundException("Ispit sa ID " + ispitId + " ne postoji"));

        //TODO  - Provera da li student slusa predmet
        if (!slusaPredmetRepository.existsByStudentIndeksIdAndPredmetId(studentIndeksId, ispit.getPredmet().getId())) {
            throw new IllegalStateException("Student nije upisan na predmet i ne moze se prijaviti za ispit.");
        }

        //TODO - kreiranje i snimanje prijave na ispit
        PrijavaIspita prijava = converters.toPrijavaIspita(request);
        if (prijava.getDatumPrijave() == null) {
            prijava.setDatumPrijave(LocalDate.now());
        }
        prijava.setStudentIndeks(studentIndeks);
        prijava.setIspit(ispit);

        PrijavaIspita saved = prijavaIspitaRepository.save(prijava);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<PrijavljeniStudentDTO> getPrijavljeniStudenti(Long ispitId) {
        if (!ispitRepository.existsById(ispitId)) {
            throw new EntityNotFoundException("Ispit sa ID " + ispitId + " ne postoji");
        }
        List<PrijavaIspita> prijave = prijavaIspitaRepository.findAllByIspitId(ispitId);
        return entityMappers.fromPrijaveIspitaToPrijavljeniStudentiDTO(prijave);
    }
}


