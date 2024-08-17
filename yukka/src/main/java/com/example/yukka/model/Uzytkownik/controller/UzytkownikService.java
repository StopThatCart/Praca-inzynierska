package com.example.yukka.model.uzytkownik.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UzytkownikService implements  UserDetailsService {

    @Autowired
    private final UzytkownikRepository uzytkownikRepository;

    @Override
   // @Transactional
    public UserDetails loadUserByUsername(String nazwa) throws UsernameNotFoundException {
        return uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownika nie znaleziono"));
    }

    public Collection<Uzytkownik> findAllUzytkownicy(){
        return uzytkownikRepository.findAllUzytkownicy();
    }

    public Uzytkownik findByEmail(String userEmail){
        return uzytkownikRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));
    }

    public Uzytkownik setBanUzytkownik(String email, Authentication currentUser, boolean ban){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Optional<Uzytkownik> uzytOpt = uzytkownikRepository.findByEmail(email);
        if(uzytOpt.isEmpty()) {
            return null;
        }
        Uzytkownik targetUzyt = uzytOpt.get();

        if(uzyt.isNormalUzytkownik()) {
            throw new IllegalArgumentException("Zwykli użytkownicy nie mogą banować nikogo");
        }

        if(targetUzyt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można banować samego siebie");
        }

        if(targetUzyt.isBan() == ban) {
            throw new IllegalArgumentException("Użytkownik jest już zbanowany/odbanowany");
        }

        if(targetUzyt.isAdmin() || targetUzyt.isPracownik()) {
            throw new IllegalArgumentException("Admini i pracownicy nie mogą być banowani ani odbanowywani");
        }
        return uzytkownikRepository.banUzytkownik(email, ban);
    }

    // Bez zabezpieczeń bo to tylko do seedowania
    public void addUzytkownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        uzytkownikRepository.addUzytkownik(uzytkownik, ust);
    }

    // Bez zabezpieczeń bo to tylko do seedowania
    public void addPracownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        uzytkownikRepository.addUzytkownik(uzytkownik, uzytkownik.getLabels(), ust);
    }

    public void remove(String email, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik uzytOpt = uzytkownikRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + email));
        
        if(uzytOpt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można usuwać samego siebie, przynajmniej na razie.");
        }

        if(uzytOpt.isAdmin() || uzytOpt.isPracownik()) {
            throw new IllegalArgumentException("Nie można usuwać samego admina lub pracownika, przynajmniej na razie.");
        }
    }

}
