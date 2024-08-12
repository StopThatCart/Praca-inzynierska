package com.example.yukka.model.uzytkownik;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements  UserDetailsService {

    @Autowired
    private final UzytkownikRepository repository;

    @Override
   // @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownika nie znaleziono"));
    }

    public Collection<Uzytkownik> getAllUsers(){
        return repository.getAllUsers();
    }

    public Optional<Uzytkownik> dawajEmailDeklu(String userEmail){
        return repository.findByEmail(userEmail);
    }

    public void banUzytkownik(String userEmail){
        repository.banUzytkownik(userEmail);
    }

    public void addUzytkownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        repository.addUzytkownik(uzytkownik, ust);
    }

    public void addPracownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        repository.addUzytkownik(uzytkownik, uzytkownik.getLabels(), ust);
    }

}
