package com.example.yukka.model.uzytkownik.controller;

import static java.io.File.separator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.auth.email.EmailService;
import com.example.yukka.auth.email.EmailTemplateName;
import com.example.yukka.auth.requests.EmailRequest;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.common.FileResponse;
import com.example.yukka.common.PageResponse;
import com.example.yukka.file.DefaultImage;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.BlockedUzytkownikException;
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.mappers.CommonMapperService;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;
import com.example.yukka.model.uzytkownik.requests.ProfilRequest;
import com.example.yukka.model.uzytkownik.requests.StatystykiDTO;
import com.example.yukka.model.uzytkownik.requests.UstawieniaRequest;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serwis zarządzający użytkownikami w aplikacji.
 * 
 * Metody:
 * <ul>
 * <li><strong>loadUserByUsername</strong>: Ładuje szczegóły użytkownika na podstawie nazwy użytkownika lub emaila.</li>
 * <li><strong>findAll</strong>: Zwraca listę wszystkich użytkowników.</li>
 * <li><strong>findByEmail</strong>: Znajduje użytkownika na podstawie emaila i zwraca jego odpowiedź.</li>
 * <li><strong>findByNazwa</strong>: Znajduje użytkownika na podstawie nazwy i zwraca jego odpowiedź.</li>
 * <li><strong>getLoggedInAvatar</strong>: Pobiera avatar zalogowanego użytkownika.</li>
 * <li><strong>getBlokowaniAndBlokujacy</strong>: Pobiera listę blokowanych i blokujących użytkowników.</li>
 * <li><strong>getUstawienia</strong>: Pobiera ustawienia zalogowanego użytkownika.</li>
 * <li><strong>updateUstawienia</strong>: Aktualizuje ustawienia zalogowanego użytkownika.</li>
 * <li><strong>getStatystykiOfUzytkownik</strong>: Pobiera statystyki użytkownika na podstawie nazwy.</li>
 * <li><strong>updateProfil</strong>: Aktualizuje profil zalogowanego użytkownika.</li>
 * <li><strong>sendChangeEmail</strong>: Wysyła email z prośbą o zmianę adresu email użytkownika.</li>
 * <li><strong>updateUzytkownikAvatar</strong>: Aktualizuje avatar zalogowanego użytkownika.</li>
 * <li><strong>setBlokUzytkownik</strong>: Blokuje lub odblokowuje użytkownika na podstawie nazwy.</li>
 * <li><strong>addUzytkownik</strong>: Dodaje nowego użytkownika (do seedowania).</li>
 * <li><strong>addPracownik</strong>: Dodaje nowego pracownika (do seedowania).</li>
 * <li><strong>removeSelf</strong>: Usuwa konto zalogowanego użytkownika.</li>
 * <li><strong>sprawdzBlokowanie</strong>: Sprawdza, czy użytkownik jest zablokowany przez innego użytkownika.</li>
 * <li><strong>seedRemoveUzytkownicyObrazy</strong>: Usuwa obrazy użytkowników (do seedowania).</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UzytkownikService implements  UserDetailsService {
    @Value("${application.file.uploads.photos-output-path}")
    String fileUploadPath;

    private final UzytkownikRepository uzytkownikRepository;
    private final FileUtils fileUtils;
    private final FileStoreService fileStoreService;
    private final CommonMapperService commonMapperService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    

    /**
     * Ładuje szczegóły użytkownika na podstawie nazwy użytkownika lub adresu e-mail.
     *
     * @param nazwa nazwa użytkownika lub adres e-mail
     * @return szczegóły użytkownika
     * @throws UsernameNotFoundException jeśli użytkownik o podanej nazwie nie zostanie znaleziony
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nazwa) {
        return uzytkownikRepository.findByNameOrEmail(nazwa)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));
    }


    /**
     * Pobiera stronę użytkowników na podstawie podanych parametrów.
     *
     * @param page numer strony do pobrania.
     * @param size liczba elementów na stronie.
     * @param szukaj fraza do wyszukiwania użytkowników.
     * @param connectedUser aktualnie zalogowany użytkownik.
     * @return PageResponse<UzytkownikResponse> zawierający stronę użytkowników.
     */
    @Transactional(readOnly = true)
    public PageResponse<UzytkownikResponse> findAllUzytkownicy(int page, int size, String szukaj, Authentication connectedUser) {
        log.info("Pobieranie wszystkich użytkowników z nazwą: " + szukaj);
        Boolean aktywowany = false;

        if(connectedUser != null) {
            Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
            aktywowany = uzyt.isAdmin() || uzyt.isPracownik();
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("uzyt.dataUtworzenia").descending());
        Page<Uzytkownik> uzytkownicy = uzytkownikRepository.findAllUzytkownicy(szukaj, aktywowany, pageable);
        return commonMapperService.uzytkownikPagetoPageUzytkownikResponse(uzytkownicy);
    }

    /**
     * Znajduje użytkownika na podstawie adresu email.
     *
     * @param userEmail adres email użytkownika, którego chcemy znaleźć
     * @return UzytkownikResponse obiekt zawierający dane znalezionego użytkownika
     * @throws EntityNotFoundException jeśli użytkownik o podanym adresie email nie zostanie znaleziony
     */
    @Transactional(readOnly = true)
    public UzytkownikResponse findByEmail(String userEmail){
        log.info("Pobieranie użytkownika o emailu: " + userEmail);
        Uzytkownik uzyt = uzytkownikRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + userEmail));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    /**
     * Znajduje użytkownika na podstawie nazwy.
     *
     * @param nazwa nazwa użytkownika do znalezienia
     * @return odpowiedź zawierająca dane użytkownika
     * @throws EntityNotFoundException jeśli użytkownik o podanej nazwie nie zostanie znaleziony
     */
    @Transactional(readOnly = true)
    public UzytkownikResponse findByNazwa(String nazwa){
        log.info("Pobieranie użytkownika o nazwie: " + nazwa);
        Uzytkownik uzyt = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    /**
     * Pobiera awatar zalogowanego użytkownika.
     *
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return FileResponse zawierający dane awatara użytkownika.
     * @throws EntityNotFoundException jeśli użytkownik o podanym emailu nie zostanie znaleziony.
     */
    @Transactional(readOnly = true)
    public FileResponse getLoggedInAvatar(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik uzyt2 = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzyt.getEmail()));
                
        return FileResponse.builder().content(fileUtils.readFile(uzyt2.getAvatar(), DefaultImage.AVATAR)).build();
    }

    /**
     * Metoda pobiera listę użytkowników blokowanych oraz blokujących aktualnie zalogowanego użytkownika.
     *
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return Obiekt UzytkownikResponse zawierający informacje o blokowanych i blokujących użytkownikach.
     */
    @Transactional(readOnly = true)
    public UzytkownikResponse getBlokowaniAndBlokujacy(Authentication currentUser) {
        log.info("Pobieranie blokowanych i blokujących użytkowników");
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik uzyt2 = uzytkownikRepository.getBlokowaniAndBlokujacy(uzyt.getNazwa()).orElse(null);

        return commonMapperService.toUzytkownikResponse(uzyt2);
    }

    /**
     * Pobiera ustawienia użytkownika na podstawie bieżącego uwierzytelnionego użytkownika.
     *
     * @param currentUser bieżący uwierzytelniony użytkownik
     * @return odpowiedź zawierająca ustawienia użytkownika
     * @throws EntityNotFoundException jeśli użytkownik o podanej nazwie nie zostanie znaleziony
     */
    @Transactional(readOnly = true)
    public UzytkownikResponse getUstawienia(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Pobieranie ustawień użytkownika " + uzyt.getNazwa());

        Uzytkownik uzytus = uzytkownikRepository.findByNazwa(uzyt.getNazwa())
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + uzyt.getNazwa()));

        return  commonMapperService.toUzytkownikResponse(uzytus);
    }

    /**
     * Aktualizuje ustawienia użytkownika.
     *
     * @param ustawienia Obiekt zawierający nowe ustawienia użytkownika.
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return Obiekt UzytkownikResponse zawierający zaktualizowane dane użytkownika.
     */
    public UzytkownikResponse updateUstawienia(UstawieniaRequest ustawienia, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Zmiana ustawień użytkownika: " + uzyt.getNazwa());
        
        Ustawienia ust = commonMapperService.toUstawienia(ustawienia);
        Uzytkownik uzytkownik = uzytkownikRepository.updateUstawienia(ust, uzyt.getEmail());

        return commonMapperService.toUzytkownikResponse(uzytkownik);
    }

    /**
     * Pobiera statystyki użytkownika na podstawie jego nazwy.
     *
     * @param nazwa Nazwa użytkownika, którego statystyki mają zostać pobrane.
     * @param currentUser Aktualnie zalogowany użytkownik, używany do sprawdzenia uprawnień.
     * @return Statystyki użytkownika w postaci obiektu StatystykiDTO lub null, jeśli użytkownik nie ma uprawnień do przeglądania statystyk.
     * @throws EntityNotFoundException Jeśli użytkownik o podanej nazwie nie istnieje.
     */
    public StatystykiDTO getStatystykiOfUzytkownik(String nazwa, Authentication currentUser) {
        log.info("Pobieranie statystyk użytkownika: " + nazwa);

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if(!targetUzyt.getUstawienia().isStatystykiProfilu() ) {
            if (currentUser == null) {
                return null;
            }
            Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
            if(!uzyt.hasAuthenticationRights(targetUzyt)) {
                return null;
            }
        }

        StatystykiDTO statystyki = StatystykiDTO.builder()
                .posty(uzytkownikRepository.getPostyCountOfUzytkownik(nazwa))
                .komentarze(uzytkownikRepository.getKomentarzeCountOfUzytkownik(nazwa))
                .rosliny(uzytkownikRepository.getRoslinyCountOfUzytkownik(nazwa))
                //.komentarzeOceny(uzytkownikRepository.getKomentarzeOcenyOfUzytkownik(nazwa))
                .komentarzeOcenyPozytywne(uzytkownikRepository.getKomentarzeOcenyPozytywneOfUzytkownik(nazwa))
                .komentarzeOcenyNegatywne(uzytkownikRepository.getKomentarzeOcenyNegatywneOfUzytkownik(nazwa))
                //.postyOceny(uzytkownikRepository.getPostyOcenyOfUzytkownik(nazwa))
                .postyOcenyPozytywne(uzytkownikRepository.getPostyOcenyPozytywneOfUzytkownik(nazwa))
                .postyOcenyNegatywne(uzytkownikRepository.getPostyOcenyNegatywneOfUzytkownik(nazwa))
                .build();
                
        return statystyki;
    }

    /**
     * Aktualizuje profil użytkownika na podstawie przekazanego żądania.
     *
     * @param request       Obiekt ProfilRequest zawierający dane do aktualizacji profilu.
     * @param currentUser   Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return              Obiekt UzytkownikResponse zawierający zaktualizowane dane użytkownika.
     */
    public UzytkownikResponse updateProfil(ProfilRequest request, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Zmiana prfilu użytkownika: " + uzyt.getNazwa());
        
        Uzytkownik uzytkownik = uzytkownikRepository.updateProfil(uzyt.getEmail(), request.getImie(), request.getMiasto(), 
        request.getMiejsceZamieszkania(), request.getOpis());
        return commonMapperService.toUzytkownikResponse(uzytkownik);
    }

    /**
     * Wysyła email weryfikacyjny w celu zmiany adresu email użytkownika.
     *
     * @param request       obiekt zawierający nowy adres email oraz hasło użytkownika
     * @param currentUser   aktualnie zalogowany użytkownik
     * @throws MessagingException           jeśli wystąpi problem z wysłaniem emaila
     * @throws IllegalArgumentException     jeśli nowy adres email jest taki sam jak obecny,
     *                                      lub jeśli podane hasło jest nieprawidłowe
     * @throws EntityNotFoundException      jeśli użytkownik z podanym adresem email nie zostanie znaleziony
     * @throws EntityAlreadyExistsException jeśli użytkownik z nowym adresem email już istnieje
     */
    public void sendChangeEmail(EmailRequest request, Authentication currentUser) throws MessagingException {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Zmiana emaila użytkownika: " + uzyt.getEmail() + " na " + request.getNowyEmail());

        if(uzyt.getEmail().equals(request.getNowyEmail())) {
            throw new IllegalArgumentException("Nie można zmienić emaila na obecny email");
        }
        Uzytkownik uzyt2 = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika z podanym adresem email"));
        
        if(!passwordEncoder.matches(request.getHaslo(), uzyt2.getHaslo())) {
            throw new IllegalArgumentException("Podane hasło jest nieprawidłowe");
        }

        if(uzytkownikRepository.findByEmail(request.getNowyEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("Użytkownik o podanym emailu już istnieje");
        }

        emailService.sendValidationEmail(uzyt, request.getNowyEmail(), EmailTemplateName.ZMIANA_EMAIL);
    }

    /**
     * Aktualizuje awatar użytkownika.
     *
     * @param file        Plik zawierający nowy awatar użytkownika.
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return Obiekt UzytkownikResponse zawierający zaktualizowane dane użytkownika.
     */
    public UzytkownikResponse updateUzytkownikAvatar(MultipartFile file, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        return commonMapperService.toUzytkownikResponse(updateUzytkownikAvatar(file, uzyt));
    }

    /**
     * Aktualizuje avatar użytkownika.
     *
     * @param file Plik zawierający nowy avatar użytkownika.
     * @param currentUser Aktualnie zalogowany użytkownik.
     * @return Zaktualizowany obiekt użytkownika z nowym avatarem.
     * @throws EntityNotFoundException Jeśli użytkownik o podanym emailu nie zostanie znaleziony.
     * @throws IllegalArgumentException Jeśli plik nie zostanie podany.
     */
    public Uzytkownik updateUzytkownikAvatar(MultipartFile file, Uzytkownik currentUser) {
        Uzytkownik uzyt = currentUser;
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzyt.getEmail()));

        if(file == null) {
            throw new IllegalArgumentException("Nie podano pliku");
        }
        fileUtils.deleteObraz(uzytkownik.getAvatar());

        String leObraz = fileStoreService.saveAvatar(file, uzyt.getUuid());
        log.info("Zapisano avatar: " + leObraz);

        uzytkownik = uzytkownikRepository.updateAvatar(uzyt.getEmail(), leObraz);
        return uzytkownik;
    }


    /**
     * Ustawia blokadę na użytkownika o podanej nazwie.
     *
     * @param nazwa          Nazwa użytkownika, który ma być zablokowany lub odblokowany.
     * @param currentUser    Aktualnie zalogowany użytkownik wykonujący operację.
     * @param blok           Flaga określająca, czy użytkownik ma być zablokowany (true) czy odblokowany (false).
     * @return               Zwraca true, jeśli operacja zakończyła się sukcesem, w przeciwnym razie false.
     * @throws EntityNotFoundException  Jeśli użytkownik o podanej nazwie nie istnieje.
     * @throws IllegalArgumentException Jeśli użytkownik próbuje zablokować samego siebie, 
     *                                  jeśli użytkownik jest administratorem lub pracownikiem,
     *                                  jeśli użytkownik jest już zablokowany,
     *                                  lub jeśli nie znaleziono blokowanych użytkowników podczas próby odblokowania.
     */
    public Boolean setBlokUzytkownik(String nazwa, Authentication currentUser, boolean blok){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik blokowany = uzytkownikRepository.findByNazwa(nazwa)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));
        log.info("Użytkownik {} próbuje zablokować użytkownika {}", uzyt.getNazwa(), blokowany.getNazwa());

        if(blokowany.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można blokować samego siebie");
        }

        if(blokowany.isAdmin() || blokowany.isPracownik()) {
            throw new IllegalArgumentException("Admini i pracownicy nie mogą być blokowani.");
        }

        log.info("Pobieranie blokowanych użytkowników");
        Optional<Uzytkownik> uzyt2 = uzytkownikRepository.getBlokowaniUzytkownicyOfUzytkownik(uzyt.getEmail());
        
        if(blok) {
            if(uzyt2.isPresent()) {
                Set<Uzytkownik> blokowani = uzyt2.get().getBlokowaniUzytkownicy();

                if(blokowani.stream().anyMatch(b -> b.getEmail().equals(blokowany.getEmail()))) {
                    throw new IllegalArgumentException("Użytkownik jest już zablokowany");
                }
            }
            return uzytkownikRepository.zablokujUzyt(blokowany.getEmail(), uzyt.getEmail());
        } else if(uzyt2.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono blokowanych użytkowników");
        }

        log.info("Odblokowywanie użytkownika");
        return uzytkownikRepository.odblokujUzyt(blokowany.getEmail(), uzyt.getEmail());
    }

    // Bez zabezpieczeń bo to tylko do seedowania
    /**
     * Dodaje nowego użytkownika do repozytorium.
     * Jest to metoda pomocnicza, która służy do seedowania danych.
     *
     * @param uzytkownik obiekt użytkownika, który ma zostać dodany
     */
    public void addUzytkownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        uzytkownikRepository.addUzytkownik(uzytkownik, ust);
    }

    // Bez zabezpieczeń bo to tylko do seedowania
    /**
     * Dodaje nowego pracownika do repozytorium.
     * Jest to metoda pomocnicza, która służy do seedowania danych.
     *
     * @param uzytkownik obiekt użytkownika, który ma zostać dodany
     */
    public void addPracownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        
        List<String> labelsSet = uzytkownik.getLabels();
        if (!labelsSet.contains("Pracownik")) {
            labelsSet.add("Pracownik");
        }

        String labels = labelsSet.stream()
                     .map(role -> "`" + role + "`")
                     .collect(Collectors.joining(":"));

        uzytkownikRepository.addUzytkownik(uzytkownik, labels, ust);
    }

    /**
     * Usuwa konto użytkownika, który wykonuje operację.
     * 
     * @param request Obiekt zawierający dane potrzebne do usunięcia konta, w tym hasło użytkownika.
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @throws IllegalArgumentException Jeśli użytkownik jest administratorem lub pracownikiem, lub jeśli podane hasło jest nieprawidłowe.
     * 
     * Operacja wykonuje następujące kroki:
     * 1. Sprawdza, czy użytkownik jest administratorem lub pracownikiem i rzuca wyjątek, jeśli tak.
     * 2. Weryfikuje, czy podane hasło jest poprawne.
     * 3. Wylogowuje użytkownika.
     * 4. Usuwa konto użytkownika z bazy danych.
     * 5. Usuwa folder użytkownika z systemu plików.
     */
    public void removeSelf(UsunKontoRequest request, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Użytkownika o emailu: " + uzyt.getEmail() + " usuwa się sam");
        
        if(uzyt.isAdmin() || uzyt.isPracownik()) {
            throw new IllegalArgumentException("Nie można usuwać konta admina lub pracownika");
        }
        if(!passwordEncoder.matches(request.getHaslo(), uzyt.getHaslo())) {
            throw new IllegalArgumentException("Podane hasło jest nieprawidłowe");
        }

        // Wylogowanie
        SecurityContextHolder.clearContext();

        removeUzytkownikQueries(uzyt.getEmail());

        Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + uzyt.getUuid());
        fileUtils.deleteDirectory(path);
    }

    // Pomocnicze
    /**
     * Seria zapytań do usunięcia użytkownika.
     *
     * @param email Email użytkownika, który ma zostać usunięty.
     */
    private void removeUzytkownikQueries(String email) {
        log.info("Usuwanie użytkownika o emailu: " + email);
        uzytkownikRepository.removePostyOfUzytkownik(email);
        uzytkownikRepository.removeKomentarzeOfUzytkownik(email);
        uzytkownikRepository.removeRoslinyOfUzytkownik(email);
        uzytkownikRepository.removePowiadomieniaOfUzytkownik(email);
        uzytkownikRepository.removeUzytkownik(email);

        
    }
    

    /**
     * Sprawdza, czy użytkownik jest zablokowany przez innego użytkownika lub czy sam zablokował innego użytkownika.
     *
     * @param nazwaUzytkownika Nazwa użytkownika, który ma być sprawdzony.
     * @param connectedUser Użytkownik, który wykonuje sprawdzenie.
     * @return Użytkownik, który został sprawdzony.
     * @throws EntityNotFoundException Jeśli użytkownik o podanej nazwie nie istnieje.
     * @throws BlockedUzytkownikException Jeśli użytkownik jest zablokowany przez innego użytkownika lub sam zablokował innego użytkownika.
     */
    public Uzytkownik sprawdzBlokowanie(String nazwaUzytkownika, Uzytkownik connectedUser) {
        Uzytkownik odbiorca = uzytkownikRepository.getBlokowaniAndBlokujacy(nazwaUzytkownika)
            .orElse(null);
        if(odbiorca == null) {
            return uzytkownikRepository.findByNazwa(nazwaUzytkownika)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o podanej nazwie: " + nazwaUzytkownika));
        }
        
        for(Uzytkownik blokujacy : odbiorca.getBlokujacyUzytkownicy()) {
            if(blokujacy.getUuid().equals(connectedUser.getUuid())) {
                throw new BlockedUzytkownikException("Nie można komentować ani oceniać treści blokujących użytkowników");
            }
        }

        for(Uzytkownik blokowany : odbiorca.getBlokowaniUzytkownicy()) {
            if(blokowany.getUuid().equals(connectedUser.getUuid())) {
                throw new BlockedUzytkownikException("Nie można komentować ani oceniać treści zablokowanych użytkowników");
            }
        }
        
        return odbiorca;
    }


        /**
     * Tworzy unikalny identyfikator użytkownika.
     *
     * @return unikalny identyfikator użytkownika
     */
    public String createUzytkownikId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<Uzytkownik> uzyt = uzytkownikRepository.findByUUID(resultId);
            if(uzyt.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }


    /**
     * Metoda seedRemoveUzytkownicyObrazy usuwa obrazy użytkowników z systemu.
     * 
     * <p>Metoda pobiera wszystkich użytkowników z repozytorium, a następnie dla każdego użytkownika
     * usuwa odpowiedni folder z obrazami znajdujący się w ścieżce określonej przez zmienną 
     * fileUploadPath. Ścieżka do folderu jest tworzona na podstawie identyfikatora użytkownika.
     * 
     * <p>Przykładowa ścieżka do folderu: fileUploadPath/uzytkownicy/{uuid}
     * 
     * <p>Metoda wykorzystuje klasę fileUtils do usunięcia folderu.
     */
    public void seedRemoveUzytkownicyObrazy() {
        Path usersDirectory = Paths.get(fileUploadPath + separator + "uzytkownicy");
        try {
            Files.list(usersDirectory).forEach(fileUtils::deleteDirectory);
            log.info("Usunięto wszystkie foldery w katalogu uzytkownicy");
        } catch (IOException e) {
            log.error("Wystąpił błąd podczas usuwania folderów w katalogu uzytkownicy", e);
        }
    }

}
