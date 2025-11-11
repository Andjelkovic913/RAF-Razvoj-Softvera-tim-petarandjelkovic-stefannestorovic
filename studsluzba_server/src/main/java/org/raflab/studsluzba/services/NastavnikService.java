package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class NastavnikService {

    private final NastavnikRepository nastavnikRepository;
    private final NastavnikZvanjeRepository nastavnikZvanjeRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public Long save(NastavnikRequest request) {
        Nastavnik nastavnik = converters.toNastavnik(request);
        if (request.getZvanjeIds() != null && !request.getZvanjeIds().isEmpty()) {
            List<NastavnikZvanje> zvanja = nastavnikZvanjeRepository.findAllById(request.getZvanjeIds());
            nastavnik.setZvanja(new HashSet<>(zvanja));
        }
        Nastavnik saved = nastavnikRepository.save(nastavnik);
        return saved.getId();
    }

    public List<NastavnikResponse> findAll() {
        return StreamSupport.stream(nastavnikRepository.findAll().spliterator(), false)
                .map(entityMappers::fromNastavnikToResponse)
                .collect(Collectors.toList());
    }

    public NastavnikResponse findById(Long id) {
        Nastavnik nastavnik = nastavnikRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + id + " ne postoji"));
        return entityMappers.fromNastavnikToResponse(nastavnik);
    }

    public List<NastavnikResponse> findByImeAndPrezime(String ime, String prezime) {
        return nastavnikRepository.findByImeAndPrezime(ime, prezime).stream()
                .map(entityMappers::fromNastavnikToResponse)
                .collect(Collectors.toList());
    }

    public NastavnikResponse updateNastavnik(Long id, NastavnikRequest request) {
        Nastavnik nastavnik = nastavnikRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik sa ID " + id + " ne postoji"));
        converters.updateNastavnik(nastavnik, request);
        if (request.getZvanjeIds() != null) {
            List<NastavnikZvanje> zvanja = nastavnikZvanjeRepository.findAllById(request.getZvanjeIds());
            nastavnik.setZvanja(new HashSet<>(zvanja));
        }
        Nastavnik updated = nastavnikRepository.save(nastavnik);
        return entityMappers.fromNastavnikToResponse(updated);
    }

    public void deleteNastavnik(Long id) {
        if (!nastavnikRepository.existsById(id)) {
            throw new EntityNotFoundException("Nastavnik sa ID " + id + " ne postoji");
        }
        nastavnikRepository.deleteById(id);
    }
}
