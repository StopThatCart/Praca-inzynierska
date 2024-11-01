package com.example.yukka.model.uzytkownik.controller;

import static java.io.File.separator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.BlockedUzytkownikException;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.social.request.UstawieniaRequest;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UzytkownikService implements  UserDetailsService {
    @Value("${application.file.uploads.photos-output-path}")
    String fileUploadPath;

    @Autowired
    private final UzytkownikRepository uzytkownikRepository;

    private final FileUtils fileUtils;
    private final FileStoreService fileStoreService;
    private final CommonMapperService commonMapperService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nazwa) {
        return uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownika nie znaleziono"));
    }

    @Transactional(readOnly = true)
    public List<Uzytkownik> findAll(){
        return uzytkownikRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse findByEmail(String userEmail){
        Uzytkownik uzyt = uzytkownikRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + userEmail));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse findByNazwa(String nazwa){
        Uzytkownik uzyt = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse getLoggedInAvatar(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik uzyt2 = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzyt.getEmail()));

        return commonMapperService.toSimpleAvatar(uzyt2);
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse getBlokowaniAndBlokujacy(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        System.out.println("Pobieranie blokowanych i blokujących użytkowników");
        Uzytkownik uzyt2 = uzytkownikRepository.getBlokowaniAndBlokujacy(uzyt.getNazwa()).orElse(null);
      //  System.out.println("Użyt: " + uzyt2.getNazwa());
    //.orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + uzyt.getNazwa()));

        return commonMapperService.toUzytkownikResponse(uzyt2);
    }


    public Ustawienia getUstawienia(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik uzytus = uzytkownikRepository.findByNazwa(uzyt.getNazwa())
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + uzyt.getNazwa()));

        return uzytus.getUstawienia();
    }

    public UzytkownikResponse updateUstawienia(UstawieniaRequest ustawienia, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Ustawienia ust = commonMapperService.toUstawienia(ustawienia);

        System.out.println("\n\n\nUstawienia przed: " + uzyt.getUstawienia());
        System.out.println("\nUstawienia zmiany: " + ust);

        System.out.println("\nto samo ale request: " + ustawienia);

        Uzytkownik uzytkownik = uzytkownikRepository.updateUstawienia(ust, uzyt.getEmail());

        System.out.println("\nUstawienia po: " + uzytkownik.getUstawienia());

        return commonMapperService.toUzytkownikResponse(uzytkownik);
    }
    

    public UzytkownikResponse updateUzytkownikAvatar(MultipartFile file, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        return commonMapperService.toUzytkownikResponse(updateUzytkownikAvatar(file, uzyt));
    }

    public Uzytkownik updateUzytkownikAvatar(MultipartFile file, Uzytkownik currentUser) {
        Uzytkownik uzyt = currentUser;
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzyt.getEmail()));

        if(file == null) {
            throw new IllegalArgumentException("Nie podano pliku");
        }
        fileUtils.deleteObraz(uzytkownik.getAvatar());

        String leObraz = fileStoreService.saveAvatar(file, uzyt.getUzytId());
        System.out.println("Zapisano avatar: " + leObraz);

        uzytkownik = uzytkownikRepository.updateAvatar(uzyt.getEmail(), leObraz);
        return uzytkownik;
    }


    public Boolean setBlokUzytkownik(String nazwa, Authentication currentUser, boolean blok){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik blokowany = uzytkownikRepository.findByNazwa(nazwa)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if(blokowany.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można blokować samego siebie");
        }

        if(blokowany.isAdmin() || blokowany.isPracownik()) {
            throw new IllegalArgumentException("Admini i pracownicy nie mogą być blokowani.");
        }

        System.out.println("Pobieranie blokowanych użytkowników");
        Optional<Uzytkownik> uzyt2 = uzytkownikRepository.getBlokowaniUzytkownicyOfUzytkownik(uzyt.getEmail());
        
        System.out.println("Sprawdzanie czy użytkownik jest już zablokowany");
        if(blok) {
            if(uzyt2.isPresent()) {
                System.out.println("SET BLOKUJE");
                Set<Uzytkownik> blokowani = uzyt2.get().getBlokowaniUzytkownicy();

                if(blokowani.stream().anyMatch(b -> b.getEmail().equals(blokowany.getEmail()))) {
                    throw new IllegalArgumentException("Użytkownik jest już zablokowany");
                }
            }
            return uzytkownikRepository.zablokujUzyt(blokowany.getEmail(), uzyt.getEmail());
        } else if(uzyt2.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono blokowanych użytkowników");
        }

        System.out.println("Odblokowywanie użytkownika");
        return uzytkownikRepository.odblokujUzyt(blokowany.getEmail(), uzyt.getEmail());
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

        Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + uzytOpt.getUzytId());
        System.out.println("Usuwanie folderu: " + path);
        uzytkownikRepository.removeUzytkownik(uzytOpt.getEmail());
        fileUtils.deleteDirectory(path);
    }

    public void removeSelf(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        
        if(uzyt.isAdmin() || uzyt.isPracownik()) {
            throw new IllegalArgumentException("Nie można usuwać samego admina lub pracownika, przynajmniej na razie.");
        }

        // Wylogowanie
        SecurityContextHolder.clearContext();

        Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + uzyt.getUzytId());
        System.out.println("Usuwanie folderu: " + path);
        uzytkownikRepository.removeUzytkownik(uzyt.getEmail());
        fileUtils.deleteDirectory(path);
    }


    // Pomocnicze
    
    public Uzytkownik sprawdzBlokowanie(String nazwaUzytkownika, Uzytkownik connectedUser) {
        Uzytkownik odbiorca = uzytkownikRepository.getBlokowaniAndBlokujacy(nazwaUzytkownika)
            .orElse(null);
        if(odbiorca == null) {
            return uzytkownikRepository.findByNazwa(nazwaUzytkownika)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o podanej nazwie: " + nazwaUzytkownika));
        }
        
        for(Uzytkownik blokujacy : odbiorca.getBlokujacyUzytkownicy()) {
            if(blokujacy.getUzytId().equals(connectedUser.getUzytId())) {
                throw new BlockedUzytkownikException("Nie można komentować ani oceniać treści blokujących użytkowników");
            }
        }

        for(Uzytkownik blokowany : odbiorca.getBlokowaniUzytkownicy()) {
            if(blokowany.getUzytId().equals(connectedUser.getUzytId())) {
                throw new BlockedUzytkownikException("Nie można komentować ani oceniać treści zablokowanych użytkowników");
            }
        }
        
        return odbiorca;
    }

    public void seedRemoveUzytkownicyObrazy() {
        
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.findAll();
        for(Uzytkownik uzyt : uzytkownicy) {

           // uzytkownikRepository.delete(uzyt);
            Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + uzyt.getUzytId());
            System.out.println("Usuwanie folderu: " + path);
            fileUtils.deleteDirectory(path);
        }
    }

}
