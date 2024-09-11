package com.example.yukka.model.uzytkownik.controller;

import static java.io.File.separator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

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
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.social.CommonMapperService;
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
    public UserDetails loadUserByUsername(String nazwa) throws UsernameNotFoundException {
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
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie istnieje"));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    public Uzytkownik updateUzytkownikAvatar(MultipartFile file, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        String leObraz = fileStoreService.saveAvatar(file, uzyt.getUzytId());
        Uzytkownik uzytkownik = uzytkownikRepository.updateAvatar(uzyt.getEmail(), leObraz);
        return uzytkownik;
    }

    public Uzytkownik updateUzytkownikAvatar(MultipartFile file, Uzytkownik currentUser) {
        Uzytkownik uzyt = currentUser;

        String leObraz = fileStoreService.saveAvatar(file, uzyt.getUzytId());
        System.out.println("Zapisano avatar: " + leObraz);
        Uzytkownik uzytkownik = uzytkownikRepository.updateAvatar(uzyt.getEmail(), leObraz);
        return uzytkownik;
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
