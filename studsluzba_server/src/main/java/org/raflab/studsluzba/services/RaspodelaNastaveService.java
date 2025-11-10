package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.dtos.StudentIndeksDTO;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RaspodelaNastaveService {

    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final EntityMappers entityMappers;

    public List<PredmetResponse> getPredmetiZaNastavnikaUAktivnojSkolskojGodini(Long idNastavnika) {
        return drziPredmetRepository.getPredmetiZaNastavnikaUAktivnojSkolskojGodini(idNastavnika).stream()
                .map(entityMappers::fromPredmetToResponse)
                .collect(Collectors.toList());
    }

    public List<StudentIndeksDTO> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika) {
        return slusaPredmetRepository.getStudentiSlusaPredmetAktivnaGodina(idPredmeta, idNastavnika).stream()
                .map(entityMappers::fromStudentIndeksToIndeksDTO)
                .collect(Collectors.toList());
    }

    public List<StudentIndeksDTO> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet) {
        return slusaPredmetRepository.getStudentiSlusaPredmetZaDrziPredmet(idDrziPredmet).stream()
                .map(entityMappers::fromStudentIndeksToIndeksDTO)
                .collect(Collectors.toList());
    }

    public List<StudentIndeksDTO> getStudentiNeSlusajuDrziPredmet(Long idDrziPredmet) {
        return slusaPredmetRepository.getStudentiNeSlusajuDrziPredmet(idDrziPredmet).stream()
                .map(entityMappers::fromStudentIndeksToIndeksDTO)
                .collect(Collectors.toList());
    }

    public Long addDrziPredmet(DrziPredmet drziPredmet) {
        return drziPredmetRepository.save(drziPredmet).getId();
    }

    public Long addSlusaPredmet(SlusaPredmet slusaPredmet) {
        return slusaPredmetRepository.save(slusaPredmet).getId();
    }

    public void deleteDrziPredmet(Long id) {
        drziPredmetRepository.deleteById(id);
    }
}

