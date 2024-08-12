package com.example.yukka.model.roslina.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;

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

    public void uploadRoslinaObraz(MultipartFile file, Authentication connectedUser, String latinName) {
        // TODO: Lepsza obsługa w przypadku nieznalezienia niczego
        Roslina roslina = roslinaRepository.findByNazwaLacinska(latinName).get();
        User user = ((User) connectedUser.getPrincipal());

        var pfp = fileStoreService.saveFile(file, latinName, user.getUsername());
        
        roslina.setObraz(pfp);
        roslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }
}
