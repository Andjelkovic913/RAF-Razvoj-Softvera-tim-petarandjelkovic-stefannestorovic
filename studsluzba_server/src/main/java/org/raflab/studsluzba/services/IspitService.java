package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IspitService {
    private final IspitRepository ispitRepository;
    private final Converters converters;

    public Ispit save(IspitRequest request) {
        Ispit ispit = converters.toIspit(request);
        return ispitRepository.save(ispit);
    }
}
