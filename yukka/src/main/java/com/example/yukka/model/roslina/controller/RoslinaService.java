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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;

@Service
public class RoslinaService {
    @Autowired
    RoslinaRepository roslinaRepository;

    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    FileUtils fileUtils;

    @Autowired
    RoslinaMapper roslinaMapper;

    public Collection<Roslina> getSome(int amount) {
        System.out.println("BOOOOOOOOOI " + amount);
        Collection<Roslina> beep = roslinaRepository.getSomePlants(amount);
        Iterable<Roslina> properties = beep;
        for (Roslina property : properties) {
            System.out.println(property.getNazwa());
            System.out.println(property.getGleby());
            
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
        return roslinaRepository.findByNazwaLacinskaWithRelations(nazwaLacinska);
    }

    public Roslina save(RoslinaRequest request) {
        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nBITCH IS PRESENT\n\n\n");
            return null;
        }
        
        if(request.areWlasciwosciEmpty()) {
            Roslina pl = roslinaMapper.toRoslina(request);
            return roslinaRepository.addRoslina(pl);
        }

        System.out.println("\n\n\n Nazwa: " + request.getNazwa() + "\n\n\n");
        System.out.println("\n\n\n Relacje: " + request.getWlasciwosciAsMap() + "\n\n\n");

        return roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

    }

    public Roslina save(RoslinaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nBITCH IS PRESENT\n\n\n");
            return null;
        }
        
        String leObraz = fileStoreService.saveRoslina(file, request.getNazwaLacinska(), uzyt.getUzytId());
        request.setObraz(leObraz);
        if(request.areWlasciwosciEmpty()) {
            Roslina pl = roslinaMapper.toRoslina(request);
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }
        Roslina ros = roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
    }

    public Roslina saveSeededRoslina(RoslinaRequest request, MultipartFile file) {
        Roslina pl = roslinaMapper.toRoslina(request);
        
        String leObraz = fileStoreService.saveSeededRoslina(file, request.getObraz());
        request.setObraz(leObraz);
        if(request.areWlasciwosciEmpty()) {
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }
        Roslina ros = roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
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
            request.getWlasciwosciAsMap());
    }

    // Uwaga: to jest do głównej rośliny, nie customowej
    public void uploadRoslinaObraz(MultipartFile file, Authentication connectedUser, String latinName) {
        Roslina roslina = roslinaRepository.findByNazwaLacinska(latinName).get();
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        
        String pfp = fileStoreService.saveRoslina(file, latinName, uzyt.getUsername());
        if(pfp == null){
            return;
        }
        fileUtils.deleteObraz(roslina.getObraz());
        
        roslina.setObraz(pfp);
        roslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }


    // Usuwanie po ID zajmuje ogromną ilość czasu i wywołuje HeapOverflow, więc lepiej jest użyć UNIQUE atrybutu jak nazwaLacinska
    public void deleteByNazwaLacinska(String nazwaLacinska) {
        roslinaRepository.deleteByNazwaLacinska(nazwaLacinska);
    }


}
