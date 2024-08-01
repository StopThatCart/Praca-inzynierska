package com.example.yukka.model.roslina;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RoslinaService {

    @Autowired
    RoslinaRepository roslinaRepository;

    public Collection<Roslina> getSome(int amount) {
        System.out.println("BOOOOOOOOOI");
        System.out.println(amount);
        Collection<Roslina> beep = roslinaRepository.getSomePlants(amount);
        Iterable<Roslina> properties = beep;
        for (Roslina property : properties) {
            System.out.println(property.getNazwa());
            System.out.println(property.getGleby());
            
            // TODO
        }

        //return plantRepository.getSomePlants(amount);



        return roslinaRepository.getSomePlants(2);
    }

    public Optional<Roslina> getById(Long id) {
        return roslinaRepository.findById(id);
    }

    public Integer save(RoslinaRequest request, Authentication connectedUser) {
       // var user = ((User) connectedUser.getPrincipal());
       // Roslina roslina = bookMapper.toBook(request);
       // book.setOwner(user);
        //return roslinaRepository.save(roslina).getId();
        return 0;
    }
}
