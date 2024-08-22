package com.example.yukka.model.dzialka.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.repository.DzialkaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DzialkaService {
    private final DzialkaRepository dzialkaRepository;

    public Dzialka saveDzialka(Dzialka dzialka) {
        return dzialkaRepository.save(dzialka);
    }

    public Optional<Dzialka> getDzialkaById(Long id) {
        return dzialkaRepository.findById(id);
    }

    public void deleteDzialka(Long id) {
        dzialkaRepository.deleteById(id);
    }
}
