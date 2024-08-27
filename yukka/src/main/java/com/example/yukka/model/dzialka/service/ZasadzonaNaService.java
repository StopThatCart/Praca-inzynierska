package com.example.yukka.model.dzialka.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.yukka.model.dzialka.ZasadzonaNa;
import com.example.yukka.model.dzialka.repository.ZasadzonaNaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZasadzonaNaService {
    private final ZasadzonaNaRepository zasadzonaNaRepository;

    public ZasadzonaNa saveZasadzonaNa(ZasadzonaNa zasadzonaNa) {
        return zasadzonaNaRepository.save(zasadzonaNa);
    }

    public Optional<ZasadzonaNa> getZasadzonaNaById(Long id) {
        return zasadzonaNaRepository.findById(id);
    }

    public void deleteZasadzonaNa(Long id) {
        zasadzonaNaRepository.deleteById(id);
    }
}
