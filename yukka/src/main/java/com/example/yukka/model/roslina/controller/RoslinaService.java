package com.example.yukka.model.roslina.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;

@Service
public class RoslinaService {
    @Autowired
    RoslinaRepository roslinaRepository;

    @Autowired
    FileStoreService fileStoreService;

    @Autowired
    RoslinaMapper roslinaMapper;

    public Collection<Roslina> getSome(int amount) {
        System.out.println("BOOOOOOOOOI " + amount);
        Collection<Roslina> beep = roslinaRepository.getSomePlants(amount);
        Iterable<Roslina> properties = beep;
        for (Roslina property : properties) {
            System.out.println(property.getNazwa());
            System.out.println(property.getGleby());
            
            // TODO Edit: Lol?
        }

        return roslinaRepository.getSomePlants(2);
    }

    public PageResponse<RoslinaResponse> findAllRosliny(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());
        Page<Roslina> rosliny = roslinaRepository.findAllRosliny(pageable);
        List<RoslinaResponse> roslinyResponse = rosliny.stream()
                .map(roslinaMapper::toRoslinaResponse)
                .toList();
        return new PageResponse<>(
                roslinyResponse,
                rosliny.getNumber(),
                rosliny.getSize(),
                rosliny.getTotalElements(),
                rosliny.getTotalPages(),
                rosliny.isFirst(),
                rosliny.isLast()
        );
    }

    public Optional<Roslina> findById(Long id) {
        return roslinaRepository.findById(id);
    }

    public Optional<Roslina> findByNazwaLacinska(String nazwaLacinska) {
        return roslinaRepository.findByNazwaLacinska(nazwaLacinska);
    }

    public Roslina save(RoslinaRequest request) {

        Roslina pl = roslinaMapper.toRoslina(request);
        //return Roslina roslinaRepository.save(request.getNazwa(), request.getNazwaLacinska(), 
       // request.getOpis(), request.getWysokoscMin(), 
       // request.getWysokoscMax(), request.getWlasciwosci());
        

        if(request.areWlasciwosciEmpty()) {
            return roslinaRepository.addRoslina(pl);
        }

        return roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosci());

    }

    public Roslina update(RoslinaRequest request) {
        if(request.areWlasciwosciEmpty()) {
            return roslinaRepository.updateRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax());
        }
        
        return roslinaRepository.updateRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosci());
    }

    // Usuwanie po ID zajmuje ogromną ilość czasu i wywołuje HeapOverflow, więc lepiej jest użyć UNIQUE atrybutu jak nazwaLacinska
    public void deleteByNazwaLacinska(String nazwaLacinska) {
        roslinaRepository.deleteByNazwaLacinska(nazwaLacinska);
    }

    // TODO: popraw dodawanie obrazu jak już się zrobi
    public void uploadRoslinaObraz(MultipartFile file, Authentication connectedUser, String latinName) {
        Roslina roslina = roslinaRepository.findByNazwaLacinska(latinName).get();
        User uzyt = ((User) connectedUser.getPrincipal());

        var pfp = fileStoreService.saveRoslina(file, latinName, uzyt.getUsername());
        
        roslina.setObraz(pfp);
        roslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }
}
