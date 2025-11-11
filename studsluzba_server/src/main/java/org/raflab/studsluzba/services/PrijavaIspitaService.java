package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.dtos.PrijavljeniStudentDTO;
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

        Ispit ispit = ispitRepository.findByIdWithRelations(ispitId)
                .orElseThrow(() -> new EntityNotFoundException("Ispit sa ID " + ispitId + " ne postoji"));

        // Validacija: proveri da li student slusa predmet
        // Prvo proveri preko DrziPredmet (preciznije), pa ako ne postoji, proveri preko Predmet
        Long drziPredmetId = ispit.getDrziPredmet() != null ? ispit.getDrziPredmet().getId() : null;
        Long predmetId = ispit.getPredmet() != null ? ispit.getPredmet().getId() : null;
        
        boolean studentSlusaPredmet = false;
        if (drziPredmetId != null) {
            studentSlusaPredmet = slusaPredmetRepository.existsByStudentIndeksIdAndDrziPredmetId(studentIndeksId, drziPredmetId);
        }
        
        if (!studentSlusaPredmet && predmetId != null) {
            studentSlusaPredmet = slusaPredmetRepository.existsByStudentIndeksIdAndPredmetId(studentIndeksId, predmetId);
        }
        
        if (!studentSlusaPredmet) {
            throw new IllegalStateException("Student ne slusa predmet koji je vezan za ovaj ispit.");
        }

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


