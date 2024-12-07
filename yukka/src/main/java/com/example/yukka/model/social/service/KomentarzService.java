package com.example.yukka.model.social.service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzMapper;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
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

   // PostMapper postMapper;
    private final KomentarzMapper komentarzMapper;

    
    /** 
     * @param komentarzId
     * @return KomentarzResponse
     */
    // Przysięgam, potem poprawię te funkcje, ale teraz nie mam czasu
    // No poprawiłem delikatnie
                
    @Transactional(readOnly = true)
    public KomentarzResponse findByKomentarzIdWithOdpowiedzi(String komentarzId) {
        return  komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));
    }

    @Transactional(readOnly = true)
    public PageResponse<KomentarzResponse> findKomentarzeOfUzytkownik(int page, int size, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByNazwa(nazwa);
        if (targetUzyt.isEmpty() || !uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)) {
           throw new ForbiddenException("Nie masz uprawnień do przeglądania komentarzy tego użytkownika");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("komentarz.dataUtworzenia").descending());

        Page<Komentarz> komentarze = komentarzRepository.findKomentarzeOfUzytkownik(targetUzyt.get().getNazwa(), pageable);
        return komentarzMapper.komentarzResponsetoPageResponse(komentarze);
    }

    public KomentarzResponse addOcenaToKomentarz(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        if(uzyt.isBan()) {
            throw new BannedUzytkownikException("Użytkownik jest zbanowany");
        }

        return addOcenaToKomentarz(request, uzyt);
    }

    public KomentarzResponse addOcenaToKomentarz(OcenaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;

        Komentarz komentarz = komentarzRepository
            .findKomentarzByKomentarzId(request.getOcenialnyId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getOcenialnyId()));

        if(komentarz.getRozmowaPrywatna() != null) {
            throw new IllegalArgumentException("Nie można oceniać wiadomości w rozmowach prywatnych");    
        }

        if(komentarz.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            throw new IllegalArgumentException("Nie można oceniać własnych komentarzy");
        }

        uzytkownikService.sprawdzBlokowanie(komentarz.getUzytkownik().getNazwa(), connectedUser);

        return komentarzMapper.toKomentarzResponse(
            komentarzRepository.addOcenaToKomentarz(uzyt.getEmail(), 
            komentarz.getKomentarzId(), 
            request.isLubi()));
    }


    public void removeOcenaFromKomentarz(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(request.getOcenialnyId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getOcenialnyId()));

        if(komentarz.getRozmowaPrywatna() != null) {
            throw new IllegalArgumentException("Nie można oceniać wiadomości w rozmowach prywatnych"); 
        }

        if(komentarz.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            throw new IllegalArgumentException("Nie można oceniać własnych komentarzy");
        }

        uzytkownikService.sprawdzBlokowanie(komentarz.getUzytkownik().getNazwa(), uzyt);

        komentarzRepository.removeOcenaFromKomentarz(uzyt.getEmail(), komentarz.getKomentarzId());
     //   komentarzRepository.updateOcenyCountOfKomentarz(komentarz.getKomentarzId());
    }


    public KomentarzResponse addKomentarzToWiadomoscPrywatna(KomentarzRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik nadawca = ((Uzytkownik) connectedUser.getPrincipal());
        return addKomentarzToWiadomoscPrywatna(request, file, nadawca);
    }

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

    public KomentarzResponse addKomentarzToPost(KomentarzRequest request,  MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        return komentarzMapper.toKomentarzResponse(addKomentarzToPost(request, file, uzyt));
    }

    public Komentarz addKomentarzToPost(KomentarzRequest request,  MultipartFile file, Uzytkownik connectedUser)  {
        Uzytkownik uzyt = connectedUser;

        Post post = postRepository.findPostByPostId(request.getTargetId())
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getTargetId()));

        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), connectedUser);

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        Komentarz response = komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getPostId(), 
        kom, LocalDateTime.now());

        return response;
    }

    public KomentarzResponse addOdpowiedzToKomentarz(@Valid KomentarzRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);
        Komentarz kom = null;
        kom = addOdpowiedzToKomentarz(request, file, uzyt);
        

        return komentarzMapper.toKomentarzResponse(kom);

    }

    public Komentarz addOdpowiedzToKomentarz(@Valid KomentarzRequest request, MultipartFile file, Uzytkownik connectedUser) {
        Uzytkownik nadawca = connectedUser;
        Komentarz komentarzDoOdpowiedzi = komentarzRepository.findKomentarzByKomentarzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));

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

        powiadomienieService.sendPowiadomienieOfKomentarz(connectedUser, komentarzDoOdpowiedzi.getUzytkownik(), komentarzDoOdpowiedzi);

        return response;
    }

    public KomentarzResponse updateKomentarz(String komentarzId, KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Komentarz kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .filter(k -> uzyt.hasAuthenticationRights(k.getUzytkownik(), connectedUser))
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));

        kom = komentarzRepository.updateKomentarz(komentarzId, request.getOpis())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza do aktualizacji o podanym ID: " + komentarzId));

        return komentarzMapper.toKomentarzResponse(kom);
    }


    public void deleteKomentarz(String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());        
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
            .orElseThrow();
        
        if (!uzyt.hasAuthenticationRights(komentarz.getUzytkownik(), connectedUser)) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia komentarza");
        }

        if(komentarz.getWPoscie() != null) {
            System.out.println("Usuwanie komentarza z posta BO TAKI WYKRYTO");
            deleteKomentarzFromPost(komentarz.getWPoscie().getPostId(), komentarzId, uzyt);
        } else if(komentarz.getRozmowaPrywatna() != null) {
            komentarzRepository.removeKomentarz(komentarzId);
        } else {
            throw new IllegalArgumentException("Nie można usunąć komentarza, bo nie komentuje ani posta, ani komentarza, ani rozmowy prywatnej.");
        }
    }

    public void deleteKomentarzFromPost(String postId, String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        postRepository.findPostByPostId(postId).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        Komentarz kom = komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));
        
        if (!uzyt.hasAuthenticationRights(kom.getUzytkownik(), connectedUser)) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia komentarza");
        }

        fileUtils.deleteObraz(kom.getObraz());
        for (Komentarz odp : kom.getOdpowiedzi()) {
            fileUtils.deleteObraz(odp.getObraz());
        }
        komentarzRepository.removeKomentarz(kom.getKomentarzId());
    }

    public void deleteKomentarzFromPost(String postId, String komentarzId, Uzytkownik connectedUser) {
        postRepository.findPostByPostId(postId).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        Komentarz kom = komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));

        System.out.println("Pobieranie użytowników z posta");

        fileUtils.deleteObraz(kom.getObraz());
        for (Komentarz odp : kom.getOdpowiedzi()) {
            fileUtils.deleteObraz(odp.getObraz());
        }
        komentarzRepository.removeKomentarz(kom.getKomentarzId());
    }

    // Seedowane bo trzeba usuwać obrazy
    public void seedRemoveKomentarzeObrazy() {
        List<Komentarz> komentarze = komentarzRepository.findAll();
        System.out.println("Usuwanie obrazów komentarzy");
        for (Komentarz kom : komentarze) {
            System.out.println("Usuwanie obrazu komentarza: " + kom.getKomentarzId());
            fileUtils.deleteObraz(kom.getObraz());
        }
    }

    // Pomocnicze
   
    private Komentarz createKomentarz(KomentarzRequest request) {
        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());
        return kom;
    }

    String createKomentarzId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<Komentarz> kom = komentarzRepository.findKomentarzByKomentarzId(resultId);
            if(kom.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }

    private void saveKomentarzFile(MultipartFile file, Komentarz kom, Uzytkownik uzyt) {
        if (file != null) {
            String leObraz = fileStoreService.saveKomentarz(file, kom.getKomentarzId(), uzyt.getUzytId());
            // if (leObraz == null) {
            //     throw new FileUploadException("Wystąpił błąd podczas wysyłania pliku");
            // }
            kom.setObraz(leObraz);
        }
    }

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
