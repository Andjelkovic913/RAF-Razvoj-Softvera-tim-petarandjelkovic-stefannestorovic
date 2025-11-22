package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.dtos.SlusaPredmetDTO;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SlusaPredmetService {

    private final SlusaPredmetRepository slusaPredmetRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;

    public Long createSlusaPredmet(SlusaPredmetRequest request) {
        SlusaPredmet slusaPredmet = converters.toSlusaPredmet(request);
        SlusaPredmet saved = slusaPredmetRepository.save(slusaPredmet);
        return saved.getId();
    }

    public SlusaPredmetResponse getSlusaPredmetById(Long id) {
        SlusaPredmet slusaPredmet = slusaPredmetRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Ne postoji ovaj SlusaPredmet"));
        return fromSlusaPredmetToResponse(slusaPredmet);
    }

    public List<SlusaPredmetResponse> getAllSlusaPredmet() {
        return StreamSupport.stream(slusaPredmetRepository.findAll().spliterator(), false)
                .map(this::fromSlusaPredmetToResponse)
                .collect(Collectors.toList());
    }

    public SlusaPredmetResponse updateSlusaPredmet(Long id, SlusaPredmetRequest request) {
        SlusaPredmet existing = slusaPredmetRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("SlusaPredmet sa ID " + id + " ne postoji"));
        
        // Update polja
        SlusaPredmet updatedEntity = converters.toSlusaPredmet(request);
        existing.setStudentIndeks(updatedEntity.getStudentIndeks());
        existing.setDrziPredmet(updatedEntity.getDrziPredmet());
        existing.setUpisGodine(updatedEntity.getUpisGodine());
        
        SlusaPredmet updated = slusaPredmetRepository.save(existing);
        return fromSlusaPredmetToResponse(updated);
    }

    public void deleteSlusaPredmet(Long id) {
        slusaPredmetRepository.deleteById(id);
    }

    private SlusaPredmetResponse fromSlusaPredmetToResponse(SlusaPredmet sp) {
        if (sp == null) {
            throw new IllegalArgumentException("SlusaPredmet cannot be null");
        }
        SlusaPredmetDTO dto = entityMappers.fromSlusaPredmetToDTO(sp);
        SlusaPredmetResponse response = new SlusaPredmetResponse();
        response.setId(dto.getId());
        response.setStudentIndeksId(dto.getStudentIndeksId());
        response.setDrziPredmet(dto.getDrziPredmet());
        response.setUpisGodineId(dto.getUpisGodineId());
        return response;
    }
}

