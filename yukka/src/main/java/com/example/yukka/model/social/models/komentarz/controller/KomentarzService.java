package com.example.yukka.model.social.models.komentarz.controller;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.BannedUzytkownikException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.social.mappers.KomentarzMapper;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.komentarz.KomentarzResponse;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.post.controller.PostRepository;
import com.example.yukka.model.social.models.powiadomienie.controller.PowiadomienieService;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.models.rozmowaPrywatna.controller.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.requests.KomentarzRequest;
import com.example.yukka.model.social.requests.OcenaRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <strong>KomentarzService</strong> - Serwis odpowiedzialny za operacje na komentarzach.
 * 
 * <strong>Metody:</strong>
 * <ul>
 * <li><strong>findByUUIDWithOdpowiedzi</strong> - Znajduje komentarz po ID wraz z odpowiedziami.</li>
 * <li><strong>findKomentarzeOfUzytkownik</strong> - Znajduje komentarze użytkownika.</li>
 * <li><strong>addOcenaToKomentarz</strong> - Dodaje ocenę do komentarza.</li>
 * <li><strong>removeOcenaFromKomentarz</strong> - Usuwa ocenę z komentarza.</li>
 * <li><strong>addKomentarzToWiadomoscPrywatna</strong> - Dodaje komentarz do wiadomości prywatnej.</li>
 * <li><strong>addKomentarzToPost</strong> - Dodaje komentarz do posta.</li>
 * <li><strong>addOdpowiedzToKomentarz</strong> - Dodaje odpowiedź do komentarza.</li>
 * <li><strong>updateKomentarz</strong> - Aktualizuje komentarz.</li>
 * <li><strong>deleteKomentarz</strong> - Usuwa komentarz.</li>
 * <li><strong>deleteKomentarzFromPost</strong> - Usuwa komentarz z posta.</li>
 * <li><strong>seedRemoveKomentarzeObrazy</strong> - Usuwa obrazy z komentarzy.</li>
 * </ul>
 * 
 * <strong>Metody pomocnicze:</strong>
 * <ul>
 * <li><strong>createKomentarz</strong> - Tworzy nowy komentarz.</li>
 * <li><strong>saveKomentarzFile</strong> - Zapisuje plik komentarza.</li>
 * <li><strong>checkTimeSinceLastKomentarz</strong> - Sprawdza czas od ostatniego komentarza.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KomentarzService {
    @Value("${komentarz.add.cooldown}")
    private Integer komAddCD;

    private final PostRepository postRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;
    private final KomentarzRepository komentarzRepository;
    private final UzytkownikService uzytkownikService;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;
    private final PowiadomienieService powiadomienieService;
    private final KomentarzMapper komentarzMapper;
        
    /**
     * Znajduje komentarz na podstawie jego ID wraz z odpowiedziami.
     *
     * @param uuid ID komentarza do znalezienia.
     * @return KomentarzResponse zawierający znaleziony komentarz oraz jego odpowiedzi.
     * @throws EntityNotFoundException jeśli komentarz o podanym ID nie zostanie znaleziony.
     */
    @Transactional(readOnly = true)
    public KomentarzResponse findByUUIDWithOdpowiedzi(String uuid) {
        log.info("Szukanie komentarza o ID: " + uuid);
        return  komentarzRepository.findKomentarzWithOdpowiedziByUUID(uuid)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + uuid));
    }

    /**
     * Znajduje komentarze użytkownika.
     *
     * @param page numer strony.
     * @param size rozmiar strony.
     * @param nazwa nazwa użytkownika, którego komentarze mają zostać znalezione.
     * @param connectedUser zalogowany użytkownik.
     * @return PageResponse zawierający znalezione komentarze użytkownika.
     * @throws ForbiddenException jeśli zalogowany użytkownik nie ma uprawnień do przeglądania komentarzy użytkownika.
     */
    @Transactional(readOnly = true)
    public PageResponse<KomentarzResponse> findKomentarzeOfUzytkownik(int page, int size, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByNazwa(nazwa);
        log.info("Szukanie komentarzy użytkownika: " + nazwa + " przez użytkownika: " + uzyt.getEmail());

        if (targetUzyt.isEmpty() || !uzyt.hasAuthenticationRights(targetUzyt.get())) {
           throw new ForbiddenException("Nie masz uprawnień do przeglądania komentarzy tego użytkownika");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("komentarz.dataUtworzenia").descending());

        Page<Komentarz> komentarze = komentarzRepository.findKomentarzeOfUzytkownik(targetUzyt.get().getNazwa(), pageable);
        return komentarzMapper.komentarzResponsetoPageResponse(komentarze);
    }

    /**
     * Dodaje ocenę do komentarza.
     *
     * @param request obiekt OcenaRequest zawierający dane oceny.
     * @param connectedUser zalogowany użytkownik.
     * @return OcenaResponse zawierający zawierający oceny komentarza.
     * @throws BannedUzytkownikException jeśli zalogowany użytkownik jest zbanowany.
     */
    public OcenaResponse addOcenaToKomentarz(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Dodawanie oceny do komentarza: " + request.getOcenialnyId() + " przez użytkownika: " + uzyt.getEmail());

        return addOcenaToKomentarz(request, uzyt);
    }

    /**
     * Dodaje ocenę do komentarza.
     *
     * @param request obiekt OcenaRequest zawierający dane oceny.
     * @param connectedUser zalogowany użytkownik.
     * @return OcenaResponse zawierający oceny komentarza.
     * @throws EntityNotFoundException jeśli komentarz o podanym ID nie zostanie znaleziony.
     * @throws IllegalArgumentException jeśli użytkownik próbuje ocenić własny komentarz.
     * @throws IllegalArgumentException jeśli użytkownik próbuje ocenić komentarz w rozmowie prywatnej.
     */
    public OcenaResponse addOcenaToKomentarz(OcenaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        log.info("Dodawanie oceny do komentarza: " + request.getOcenialnyId() + " przez użytkownika: " + uzyt.getEmail());

        Komentarz komentarz = komentarzRepository
            .findKomentarzByUUID(request.getOcenialnyId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getOcenialnyId()));

        if(komentarz.getRozmowaPrywatna() != null) {
            throw new IllegalArgumentException("Nie można oceniać wiadomości w rozmowach prywatnych");    
        }

        // if(komentarz.getUzytkownik().getUuid().equals(uzyt.getUuid())) {
        //     throw new IllegalArgumentException("Nie można oceniać własnych komentarzy");
        // }

        uzytkownikService.sprawdzBlokowanie(komentarz.getUzytkownik().getNazwa(), connectedUser);
        Komentarz kom = komentarzRepository.addOcenaToKomentarz(uzyt.getEmail(), komentarz.getUuid(), request.isLubi());

        return komentarzMapper.toOcenaResponse(kom);
    }

    /**
     * Dodaje komentarz do wiadomości prywatnej.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param file plik do załączenia do komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return KomentarzResponse zawierający dodany komentarz.
     * @throws IllegalArgumentException jeśli użytkownik próbuje rozmawiać sam ze sobą.
     */
    public KomentarzResponse addKomentarzToWiadomoscPrywatna(KomentarzRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();
        log.info("Dodawanie komentarza do wiadomości prywatnej: " + request.getTargetId() + " przez użytkownika: " + nadawca.getEmail());

        return addKomentarzToWiadomoscPrywatna(request, file, nadawca);
    }

    /**
     * Dodaje komentarz do wiadomości prywatnej.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param file plik do załączenia do komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return KomentarzResponse zawierający dodany komentarz.
     * @throws IllegalArgumentException jeśli użytkownik próbuje rozmawiać sam ze sobą.
     */
    public KomentarzResponse addKomentarzToWiadomoscPrywatna(KomentarzRequest request,
        MultipartFile file, Uzytkownik connectedUser) {

        String otherUzytNazwa = request.getTargetId();
        Uzytkownik nadawca = connectedUser;
        
        if(nadawca.getNazwa().equals(otherUzytNazwa)) {
           throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }

        Uzytkownik odbiorca = uzytkownikService.sprawdzBlokowanie(otherUzytNazwa, nadawca);
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByNazwa(odbiorca.getNazwa(), nadawca.getNazwa())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, nadawca);

        Komentarz response = komentarzRepository.addKomentarzToRozmowaPrywatna(nadawca.getNazwa(), odbiorca.getNazwa(), 
        kom, LocalDateTime.now());

        powiadomienieService.sendPowiadomienieOfRozmowa(nadawca, odbiorca, rozmowa);
        return komentarzMapper.toKomentarzResponse(response);
    }

    /**
     * Dodaje komentarz do posta.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param file plik do załączenia do komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return KomentarzResponse zawierający dodany komentarz.
     */
    public KomentarzResponse addKomentarzToPost(KomentarzRequest request,  MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Dodawanie komentarza do posta: " + request.getTargetId() + " przez użytkownika: " + uzyt.getEmail());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        return komentarzMapper.toKomentarzResponse(addKomentarzToPost(request, file, uzyt));
    }

    /**
     * Dodaje komentarz do posta.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param file plik do załączenia do komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return Komentarz zawierający dodany komentarz.
     */
    public Komentarz addKomentarzToPost(KomentarzRequest request,  MultipartFile file, Uzytkownik connectedUser)  {
        Uzytkownik uzyt = connectedUser;

        Post post = postRepository.findPostByUUIDCheck(request.getTargetId())
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getTargetId()));

        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), connectedUser);

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        Komentarz response = komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getUuid(), 
        kom, LocalDateTime.now());

        return response;
    }

    /**
     * Dodaje odpowiedź do komentarza.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param file plik do załączenia do komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return KomentarzResponse zawierający dodaną odpowiedź.
     */
    public KomentarzResponse addOdpowiedzToKomentarz(@Valid KomentarzRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Dodawanie odpowiedzi do komentarza: " + request.getTargetId());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);
        Komentarz kom = addOdpowiedzToKomentarz(request, file, uzyt);
        
        return komentarzMapper.toKomentarzResponse(kom);

    }

    /**
     * Dodaje odpowiedź do komentarza.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param file plik do załączenia do komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return Komentarz zawierający dodaną odpowiedź.
     */
    public Komentarz addOdpowiedzToKomentarz(@Valid KomentarzRequest request, MultipartFile file, Uzytkownik connectedUser) {
        Uzytkownik nadawca = connectedUser;
        Komentarz komentarzDoOdpowiedzi = komentarzRepository.findKomentarzByUUID(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));

        if(komentarzDoOdpowiedzi.getPost() == null && komentarzDoOdpowiedzi.getWPoscie() == null) {
            uzytkownikService.sprawdzBlokowanie(komentarzDoOdpowiedzi.getUzytkownik().getNazwa(), connectedUser);  
        } else {
            Post post = komentarzDoOdpowiedzi.getWPoscie();
            if(post == null) {
                post = komentarzDoOdpowiedzi.getPost();
            }
            uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), connectedUser);
        }

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, nadawca);
        
        Komentarz response = komentarzRepository.addOdpowiedzToKomentarzInPost(nadawca.getEmail(), kom, 
        request.getTargetId(), LocalDateTime.now());

        powiadomienieService.sendPowiadomienieOfKomentarz(connectedUser, komentarzDoOdpowiedzi);

        return response;
    }

    /**
     * Aktualizuje komentarz.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @param connectedUser zalogowany użytkownik.
     * @return KomentarzResponse zawierający zaktualizowany komentarz.
     * @throws EntityNotFoundException jeśli komentarz o podanym ID nie zostanie znaleziony.
     * @throws ForbiddenException jeśli zalogowany użytkownik nie ma uprawnień do aktualizacji komentarza.
     */
    public KomentarzResponse updateKomentarz(KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Aktualizacja komentarza o ID: " + request.getTargetId() + " przez użytkownika: " + uzyt.getEmail());

        komentarzRepository.findKomentarzByUUID(request.getTargetId())
                .filter(k -> uzyt.hasAuthenticationRights(k.getUzytkownik()))
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));

        Komentarz kom = komentarzRepository.updateKomentarz(request.getTargetId(), request.getOpis())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza do aktualizacji o podanym ID: " + request.getTargetId()));

        return komentarzMapper.toKomentarzResponse(kom);
    }


    /**
     * Usuwa komentarz.
     *
     * @param uuid ID komentarza do usunięcia.
     * @param connectedUser zalogowany użytkownik.
     * @throws ForbiddenException jeśli zalogowany użytkownik nie ma uprawnień do usunięcia komentarza.
     */
    public void deleteKomentarz(String uuid, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());        
        log.info("Usuwanie komentarza o ID: " + uuid + " przez użytkownika: " + uzyt.getEmail());

        Komentarz komentarz = komentarzRepository.findKomentarzByUUID(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + uuid));
        
        if (!uzyt.hasAuthenticationRights(komentarz.getUzytkownik())) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia komentarza");
        }

        if(komentarz.getWPoscie() != null) {
            deleteKomentarzFromPost(komentarz.getWPoscie().getUuid(), uuid, uzyt);
        } else if(komentarz.getRozmowaPrywatna() != null) {
            komentarzRepository.removeKomentarz(uuid);
        } else {
            throw new IllegalArgumentException("Nie można usunąć komentarza, bo nie komentuje ani posta, ani komentarza, ani rozmowy prywatnej.");
        }
    }

    /**
     * Usuwa komentarz z posta oraz wszystkie odpowiedzi na ten komentarz.
     *
     * @param postUUID ID posta, z którego ma zostać usunięty komentarz.
     * @param uuid ID komentarza, który ma zostać usunięty.
     * @param connectedUser Użytkownik, który jest aktualnie zalogowany i wykonuje operację.
     * @throws EntityNotFoundException jeśli post o podanym ID nie zostanie znaleziony.
     * @throws EntityNotFoundException jeśli komentarz o podanym ID nie zostanie znaleziony.
     */
    public void deleteKomentarzFromPost(String postUUID, String uuid, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Usuwanie komentarza o ID: " + uuid + " z posta o ID: " + postUUID + " przez użytkownika: " + uzyt.getEmail());

        postRepository.findPostByUUIDCheck(postUUID).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postUUID));
        Komentarz kom = komentarzRepository.findKomentarzWithOdpowiedziByUUID(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + uuid));
        
        if (!uzyt.hasAuthenticationRights(kom.getUzytkownik())) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia komentarza");
        }

        fileUtils.deleteObraz(kom.getObraz());
        for (Komentarz odp : kom.getOdpowiedzi()) {
            fileUtils.deleteObraz(odp.getObraz());
        }
        komentarzRepository.removeKomentarz(kom.getUuid());
    }

    /**
     * Usuwa komentarz z posta oraz wszystkie odpowiedzi na ten komentarz.
     *
     * @param postUUID ID posta, z którego ma zostać usunięty komentarz.
     * @param komUUID ID komentarza, który ma zostać usunięty.
     * @param connectedUser Użytkownik, który jest aktualnie zalogowany i wykonuje operację.
     * @throws EntityNotFoundException jeśli post o podanym ID nie zostanie znaleziony.
     * @throws EntityNotFoundException jeśli komentarz o podanym ID nie zostanie znaleziony.
     */
    public void deleteKomentarzFromPost(String postUUID, String komUUID, Uzytkownik connectedUser) {
        postRepository.findPostByUUIDCheck(postUUID).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postUUID));
        Komentarz kom = komentarzRepository.findKomentarzWithOdpowiedziByUUID(komUUID)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komUUID));

        log.info("Pobieranie użytowników z posta");

        fileUtils.deleteObraz(kom.getObraz());
        for (Komentarz odp : kom.getOdpowiedzi()) {
            fileUtils.deleteObraz(odp.getObraz());
        }
        komentarzRepository.removeKomentarz(kom.getUuid());
    }


    /**
     * Metoda seedRemoveKomentarzeObrazy usuwa obrazy powiązane z komentarzami.
     * Pobiera wszystkie komentarze z repozytorium, a następnie dla każdego komentarza
     * usuwa powiązany obraz, korzystając z metody deleteObraz z klasy fileUtils.
     * 
     * Wypisuje na konsolę informacje o procesie usuwania obrazów.
     */
    public void seedRemoveKomentarzeObrazy() {
        List<Komentarz> komentarze = komentarzRepository.findAll();
        log.info("Usuwanie obrazów komentarzy");
        for (Komentarz kom : komentarze) {
            log.info("Usuwanie obrazu komentarza: " + kom.getUuid());
            fileUtils.deleteObraz(kom.getObraz());
        }
    }

    // Pomocnicze
   
    /**
     * Tworzy nowy komentarz.
     *
     * @param request obiekt KomentarzRequest zawierający dane komentarza.
     * @return Komentarz zawierający nowy komentarz.
     */
    private Komentarz createKomentarz(KomentarzRequest request) {
        Komentarz kom = komentarzMapper.toKomentarz(request);
        return kom;
    }

    /**
     * Zapisuje plik komentarza.
     *
     * @param file plik do zapisania.
     * @param kom komentarz, do którego ma zostać zapisany plik.
     * @param uzyt użytkownik, który dodaje komentarz.
     */
    private void saveKomentarzFile(MultipartFile file, Komentarz kom, Uzytkownik uzyt) {
        if (file != null) {
            String leObraz = fileStoreService.saveKomentarz(file, kom.getUuid(), uzyt.getUuid());
            // if (leObraz == null) {
            //     throw new FileUploadException("Wystąpił błąd podczas wysyłania pliku");
            // }
            kom.setObraz(leObraz);
        }
    }

    /**
     * Sprawdza czas od ostatniego komentarza.
     *
     * @param newestKomentarz najnowszy komentarz.
     */
    private void checkTimeSinceLastKomentarz(Optional<Komentarz> newestKomentarz) {
        if (newestKomentarz.isPresent()) {
            LocalDateTime lastKomentarzTime = newestKomentarz.get().getDataUtworzenia();
            LocalDateTime now = LocalDateTime.now();
    
            Duration timeElapsed = Duration.between(lastKomentarzTime, now);
            if (timeElapsed.getSeconds() < komAddCD) {
                throw new IllegalStateException("Musisz poczekać " + komAddCD + " sekund przed dodaniem kolejnego komentarza.");
            }
        }
    }

}
