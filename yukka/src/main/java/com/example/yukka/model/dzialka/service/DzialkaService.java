package com.example.yukka.model.dzialka.service;

import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.repository.DzialkaRepository;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.UzytkownikRoslina;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaRepository;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DzialkaService {
    private final DzialkaRepository dzialkaRepository;
    private final RoslinaRepository roslinaRepository;
    private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;
    @SuppressWarnings("unused")
    private final ZasadzonaNaService zasadzonaNaService;
    private final UzytkownikRepository uzytkownikRepository;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;


    // Pobiera działki jakiegoś użytkownika, o ile on na to pozwala
    public Optional<Dzialka> getDzialkaOfUzytkownikByNumer(int numer, String nazwa) {
        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(nazwa).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika " + nazwa));

        if(!wlasciciel.getUstawienia().isOgrodPokaz()) {
            throw new AccessDeniedException("Ogród użytkownika " + nazwa + " jest ukryty");
        }

        return dzialkaRepository.getDzialkaByNumer(wlasciciel.getEmail(), numer);
    }

    // Pobiera WŁASNĄ działkę
    public Optional<Dzialka> getDzialkaByNumer(int numer, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), numer);
    }
/* 
    public Optional<Dzialka> getDzialkaById(Long id) {
        return dzialkaRepository.findById(id);
    }
*/
    /* 
    public Dzialka saveDzialka(Dzialka dzialka) {
        return dzialkaRepository.save(dzialka);
    }
*/
    // Do zapisywania z obrazem lepiej na razie poczekać
    public Dzialka saveRoslinaToDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), request.getNumerDzialki())
        .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono działki " + request.getNumerDzialki() + " dla użytkownika " + uzyt.getEmail()));

        if(dzialkaRepository.checkIfCoordinatesAreOccupied(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY())) {
            System.out.println("Koordynaty zajęte. Anyway!");
        };

        if(request.getUzytkownikRoslinaId() != null) {
            Dzialka dzialkaZRoslina = dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), 
            request.getObraz(), request.getUzytkownikRoslinaId());
            return dzialkaZRoslina;
        }

        if(request.getNazwaLacinskaRosliny() == null) {
            return null;
        }

        Dzialka dzialkaZRoslina = dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
        request.getX(), request.getY(), 
        request.getObraz(), request.getNazwaLacinskaRosliny());
        return dzialkaZRoslina;
    }

    public Dzialka saveRoslinaToDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), request.getNumerDzialki())
        .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono działki " + request.getNumerDzialki() + " dla użytkownika " + uzyt.getEmail()));

        if(dzialkaRepository.checkIfCoordinatesAreOccupied(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY())) {
            System.out.println("Koordynaty zajęte. Anyway!");
        };

        if(request.getUzytkownikRoslinaId() != null) {
            UzytkownikRoslina roslina = uzytkownikRoslinaRepository.findByRoslinaId(request.getUzytkownikRoslinaId())
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rośliny użytkownika o roslinaId: " + request.getNazwaLacinskaRosliny()));

            Dzialka dzialkaZRoslina = dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), 
            request.getObraz(), request.getUzytkownikRoslinaId());
            return dzialkaZRoslina;
        }

        Roslina roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinskaRosliny())
        .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rośliny " + request.getNazwaLacinskaRosliny()));

        Dzialka dzialkaZRoslina = dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
        request.getX(), request.getY(), 
        request.getObraz(), roslina.getNazwaLacinska());
        return dzialkaZRoslina;
    }

    public Dzialka updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), request.getNumerDzialki())
        .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono działki " + request.getNumerDzialki() + " dla użytkownika " + uzyt.getEmail()));

        if(dzialkaRepository.checkIfCoordinatesAreOccupied(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY())) {
            if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz() != null) {
                fileUtils.deleteObraz(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz());
            }
            String pfp = fileStoreService.saveRoslinaObrazInDzialka(file, uzyt.getUsername());
            if(pfp == null){
                return null;
            }
        
            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaObrazInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), pfp);

            return dzialkaZRoslina;
        } else {
            return null;
        }
    }


    public Dzialka updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile file, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), request.getNumerDzialki())
        .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono działki " + request.getNumerDzialki() + " dla użytkownika " + uzyt.getEmail()));

        System.out.println("Le aktualizacja obrazu rośliny na działce");
        if(dzialkaRepository.checkIfCoordinatesAreOccupied(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY())) {
            if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz() != null) {
                fileUtils.deleteObraz(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz());
            }
            String pfp = fileStoreService.saveRoslinaObrazInDzialka(file, uzyt.getUsername());
            if(pfp == null){
                return null;
            }
        
            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaObrazInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), pfp);

            return dzialkaZRoslina;
        } else {
            return null;
        }
    }

    public Dzialka saveUzytkownikRoslinaToDzialka(String nazwaLacinska, int numer, Authentication connectedUser) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteRoslinaFromDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Dzialka dzialka = dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), request.getNumerDzialki()).orElseThrow(
            () -> new IllegalArgumentException("Nie znaleziono działki " + request.getNumerDzialki() + " dla użytkownika " + uzyt.getEmail()));
        
        Uzytkownik wlasciciel = dzialka.getOgrod().getUzytkownik();
        
        if(!uzyt.hasAuthenticationRights(wlasciciel, connectedUser)) {
            throw new AccessDeniedException("Nie masz uprawnień do usunięcia rośliny z działki użytkownika " + dzialka.getOgrod().getUzytkownik().getEmail());
        }
        ZasadzonaNaReverse pozycja = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());

        if(pozycja == null) {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }

        fileUtils.deleteObraz(pozycja.getObraz());

        dzialkaRepository.removeRoslinaFromDzialka(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY());
    }
    

    // Lol nie
    /*
 public void deleteDzialka(Long id) {
        dzialkaRepository.deleteById(id);
    }

     */
   
}
