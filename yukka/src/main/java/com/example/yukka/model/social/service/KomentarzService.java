package com.example.yukka.model.social.service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.BannedUzytkownikException;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzMapper;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

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
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;
    private final PowiadomienieService powiadomienieService;

   // PostMapper postMapper;
    private final KomentarzMapper komentarzMapper;

    // Przysięgam, potem poprawię te funkcje, ale teraz nie mam czasu
                
    @Transactional(readOnly = true)
    public KomentarzResponse findByKomentarzIdWithOdpowiedzi(String komentarzId) {
        return  komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));
    }

    @Transactional(readOnly = true)
    public PageResponse<KomentarzResponse> findKomentarzeOfUzytkownik(int page, int size, String email, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByEmail(email);
        if (targetUzyt.isEmpty() || !uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)) {
            return new PageResponse<>();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("komentarz.dataUtworzenia").descending());

        Page<Komentarz> komentarze = komentarzRepository.findKomentarzeOfUzytkownik(uzyt.getEmail(), pageable);
        return komentarzMapper.komentarzResponsetoPageResponse(komentarze);
    }

    public Komentarz addOcenaToKomentarz(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(request.getOcenialnyId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getOcenialnyId()));

        if(komentarz.getRozmowaPrywatna() != null) {
            throw new IllegalArgumentException("Nie można oceniać wiadomości w rozmowach prywatnych"); 
        }

        if(komentarz.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            throw new IllegalArgumentException("Nie można oceniać własnych komentarzy");
        }

        return komentarzRepository.addOcenaToKomentarz(uzyt.getEmail(), komentarz.getKomentarzId(), request.isLubi());
    }

    public Komentarz addOcenaToKomentarzTest(OcenaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        if(uzyt.isBan()) {
            throw new BannedUzytkownikException("Użytkownik jest zbanowany");
        }
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(request.getOcenialnyId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getOcenialnyId()));

        if(komentarz.getRozmowaPrywatna() != null) {
            throw new IllegalArgumentException("Nie można oceniać wiadomości w rozmowach prywatnych");    
        }

        if(komentarz.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            throw new IllegalArgumentException("Nie można oceniać własnych komentarzy");
        }

        return komentarzRepository.addOcenaToKomentarz(uzyt.getEmail(), komentarz.getKomentarzId(), request.isLubi());
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

        komentarzRepository.removeOcenaFromKomentarz(uzyt.getEmail(), komentarz.getKomentarzId());
     //   komentarzRepository.updateOcenyCountOfKomentarz(komentarz.getKomentarzId());
    }


    public Komentarz addKomentarzToWiadomoscPrywatna(String otherUzytNazwa, @Valid KomentarzRequest request,
        Authentication connectedUser) {
        Uzytkownik nadawca = ((Uzytkownik) connectedUser.getPrincipal());
        if(nadawca.getNazwa().equals(otherUzytNazwa)) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(otherUzytNazwa).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + otherUzytNazwa));
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(odbiorca.getNazwa(), nadawca.getNazwa()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());
        
        Komentarz response = komentarzRepository.addKomentarzToRozmowaPrywatna(nadawca.getNazwa(), odbiorca.getNazwa(), kom);

        PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
        .typ(TypPowiadomienia.WIADOMOSC_PRYWATNA.name())
        .odnosnik(rozmowa.getNadawca())
        .uzytkownikNazwa(nadawca.getNazwa()).avatar(nadawca.getAvatar())
        .build();
        powiadomienieService.addPowiadomienie(powiadomienie, odbiorca);

        return response; 
    }

    public Komentarz addKomentarzToWiadomoscPrywatna(String otherUzytNazwa, @Valid KomentarzRequest request,
        Uzytkownik connectedUser) {
        Uzytkownik nadawca = connectedUser;
        
        if(nadawca.getNazwa().equals(otherUzytNazwa)) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(otherUzytNazwa).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + otherUzytNazwa));
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByNazwa(odbiorca.getNazwa(), nadawca.getNazwa()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());
        
        Komentarz response = komentarzRepository.addKomentarzToRozmowaPrywatna(nadawca.getNazwa(), odbiorca.getNazwa(), kom);

        PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
        .typ(TypPowiadomienia.WIADOMOSC_PRYWATNA.name())
        .odnosnik(rozmowa.getNadawca())
        .uzytkownikNazwa(nadawca.getNazwa()).avatar(nadawca.getAvatar())
        .build();

        powiadomienieService.addPowiadomienie(powiadomienie, odbiorca);

        return response; 
    }

    public Komentarz addKomentarzToWiadomoscPrywatna(String otherUzytNazwa, @Valid KomentarzRequest request,
        MultipartFile file, Authentication connectedUser) throws FileUploadException {
        Uzytkownik nadawca = ((Uzytkownik) connectedUser.getPrincipal());
        
        if(nadawca.getNazwa().equals(otherUzytNazwa)) {
           throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(otherUzytNazwa).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + otherUzytNazwa));
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByNazwa(odbiorca.getNazwa(), nadawca.getNazwa()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, nadawca);

        Komentarz response = komentarzRepository.addKomentarzToRozmowaPrywatna(nadawca.getNazwa(), odbiorca.getNazwa(), kom);

        PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
        .typ(TypPowiadomienia.WIADOMOSC_PRYWATNA.name())
        .odnosnik(rozmowa.getNadawca())
        .uzytkownikNazwa(nadawca.getNazwa()).avatar(nadawca.getAvatar())
        .build();
        powiadomienieService.addPowiadomienie(powiadomienie, odbiorca);

        return response;
    }

    public Komentarz addKomentarzToPost(KomentarzRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByPostId(request.getTargetId()).orElseThrow(
            () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getTargetId()));


        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());

      //  System.out.println("\n\n\n\n\n\n\nKOMENTARZ: " + kom.toString());

        Komentarz response = komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getPostId(), kom);
      //  komentarzRepository.updateKomentarzeCountInPost(post.getPostId());
     //   System.out.println("\n\n\n\n\n\n\nRESPONSE: " + response.toString());

        // W sumie do autorów postów nie powinny lecieć powiadomienia o nowych komentarzach w postach
        /*
        PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
        .typ(TypPowiadomienia.KOMENTARZ_POST.name())
        .tytul(post.getTytul())
        .odnosnik(post.getPostId())
        .uzytkownikNazwa(uzyt.getNazwa())
        .avatar(uzyt.getAvatar())
        .build();
        powiadomienieService.addPowiadomienie(powiadomienie, post.getAutor());
 */

        return response;
    }

    public Komentarz addKomentarzToPost(KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByPostId(request.getTargetId()).orElseThrow();

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());

        Komentarz response = komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getPostId(), kom);

        // W sumie do autorów postów nie powinny lecieć powiadomienia o nowych komentarzach w postach
        /*
        PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
        .typ(TypPowiadomienia.KOMENTARZ_POST.name())
        .tytul(post.getTytul())
        .odnosnik(post.getPostId())
        .uzytkownikNazwa(uzyt.getNazwa())
        .avatar(uzyt.getAvatar())
        .build();
        powiadomienieService.addPowiadomienie(powiadomienie, post.getAutor());
 */
        return response;
    }

    public Komentarz addKomentarzToPost(KomentarzRequest request,  MultipartFile file, Authentication connectedUser) throws FileUploadException {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByPostId(request.getTargetId()).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getTargetId()));

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        Komentarz response = komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getPostId(), kom);

                // W sumie do autorów postów nie powinny lecieć powiadomienia o nowych komentarzach w postach
        /*
        PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
        .typ(TypPowiadomienia.KOMENTARZ_POST.name())
        .tytul(post.getTytul())
        .odnosnik(post.getPostId())
        .uzytkownikNazwa(uzyt.getNazwa())
        .avatar(uzyt.getAvatar())
        .build();
        powiadomienieService.addPowiadomienie(powiadomienie, post.getAutor());
 */

        return response;
    }

    public Komentarz addOdpowiedzToKomentarz(@Valid KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Komentarz komentarzDoOdpowiedzi = komentarzRepository.findKomentarzByKomentarzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));
        Post post = postRepository.findPostByKomentarzOdpowiedzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta dla odpowiedzi o podanym ID: " + request.getTargetId()));

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());

        Komentarz response = komentarzRepository.addOdpowiedzToKomentarzInPost(uzyt.getEmail(), kom, request.getTargetId());

        if(!komentarzDoOdpowiedzi.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
            .typ(TypPowiadomienia.KOMENTARZ_POST.name())
            .tytul(post.getTytul()).odnosnik(post.getPostId())
            .uzytkownikNazwa(uzyt.getNazwa()).avatar(uzyt.getAvatar())
            .build();
            powiadomienieService.addPowiadomienie(powiadomienie, komentarzDoOdpowiedzi.getUzytkownik());
        }
        
        return response;
    }

    public Komentarz addOdpowiedzToKomentarz(@Valid KomentarzRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Komentarz komentarzDoOdpowiedzi = komentarzRepository.findKomentarzByKomentarzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));
        Post post = postRepository.findPostByKomentarzOdpowiedzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta dla odpowiedzi o podanym ID: " + request.getTargetId()));

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());

        Komentarz response = komentarzRepository.addOdpowiedzToKomentarzInPost(uzyt.getEmail(), kom, request.getTargetId());

        if(!komentarzDoOdpowiedzi.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
            .typ(TypPowiadomienia.KOMENTARZ_POST.name())
            .tytul(post.getTytul()).odnosnik(post.getPostId())
            .uzytkownikNazwa(uzyt.getNazwa()).avatar(uzyt.getAvatar())
            .build();
            powiadomienieService.addPowiadomienie(powiadomienie, komentarzDoOdpowiedzi.getUzytkownik());
        }
        
        return response;
        
    }

    public Komentarz addOdpowiedzToKomentarz(@Valid KomentarzRequest request, MultipartFile file, Authentication connectedUser) throws FileUploadException {
        
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByKomentarzOdpowiedzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta dla odpowiedzi o podanym ID: " + request.getTargetId()));
        Komentarz komentarzDoOdpowiedzi = komentarzRepository.findKomentarzByKomentarzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        Komentarz response = komentarzRepository.addOdpowiedzToKomentarzInPost(uzyt.getEmail(), kom, request.getTargetId());

        if(!komentarzDoOdpowiedzi.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
            .typ(TypPowiadomienia.KOMENTARZ_POST.name()).tytul(post.getTytul())
            .odnosnik(post.getPostId()).uzytkownikNazwa(uzyt.getNazwa())
            .avatar(uzyt.getAvatar())
            .build();
            powiadomienieService.addPowiadomienie(powiadomienie, komentarzDoOdpowiedzi.getUzytkownik());
        }

        return response;
    }

    public Komentarz addOdpowiedzToKomentarz(@Valid KomentarzRequest request, MultipartFile file, Uzytkownik connectedUser) throws FileUploadException {
        
        Uzytkownik uzyt = connectedUser;
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByKomentarzOdpowiedzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta dla odpowiedzi o podanym ID: " + request.getTargetId()));
        Komentarz komentarzDoOdpowiedzi = komentarzRepository.findKomentarzByKomentarzId(request.getTargetId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + request.getTargetId()));

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        Komentarz response = komentarzRepository.addOdpowiedzToKomentarzInPost(uzyt.getEmail(), kom, request.getTargetId());

        if(!komentarzDoOdpowiedzi.getUzytkownik().getUzytId().equals(uzyt.getUzytId())) {
            PowiadomienieResponse powiadomienie = PowiadomienieResponse.builder()
            .typ(TypPowiadomienia.KOMENTARZ_POST.name()).tytul(post.getTytul())
            .odnosnik(post.getPostId()).uzytkownikNazwa(uzyt.getNazwa())
            .avatar(uzyt.getAvatar())
            .build();
            powiadomienieService.addPowiadomienie(powiadomienie, komentarzDoOdpowiedzi.getUzytkownik());
        }

        return response;
    }

    public Komentarz updateKomentarz(String komentarzId, @Valid KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Komentarz kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .filter(k -> uzyt.hasAuthenticationRights(k.getUzytkownik(), connectedUser))
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));
        return komentarzRepository.updateKomentarz(uzyt.getEmail(), komentarzId, kom);
    }


    public void deleteKomentarz(String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());        
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
            .orElseThrow();
        
        if (uzyt.hasAuthenticationRights(komentarz.getUzytkownik(), connectedUser)) {
            throw new AccessDeniedException("Nie masz uprawnień do usunięcia komentarza");
        }

        fileUtils.deleteObraz(komentarz.getObraz());
        komentarzRepository.removeKomentarz(komentarz.getKomentarzId());
    }

    public void deleteKomentarzFromPost(String postId, String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(postId).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        Komentarz kom = komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));
        
        if (uzyt.hasAuthenticationRights(kom.getUzytkownik(), connectedUser)) {
            throw new AccessDeniedException("Nie masz uprawnień do usunięcia komentarza");
        }

        List<Uzytkownik> uzytkownicyInPost = uzytkownikRepository.getConnectedUzytkownicyFromPostButBetter(post.getPostId());

        fileUtils.deleteObraz(kom.getObraz());
        for (Komentarz odp : kom.getOdpowiedzi()) {
            fileUtils.deleteObraz(odp.getObraz());
        }
        komentarzRepository.removeKomentarz(kom.getKomentarzId());
        
        komentarzRepository.updateUzytkownikKomentarzeOcenyCount(uzytkownicyInPost);
        komentarzRepository.updateKomentarzeCountInPostButWithFunniNewRelation(post.getPostId());
    }

    public void deleteKomentarzFromPost(String postId, String komentarzId, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Post post = postRepository.findPostByPostId(postId).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        Komentarz kom = komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono komentarza o podanym ID: " + komentarzId));

        System.out.println("Pobieranie użytowników z posta");
      //  List<Uzytkownik> uzytkownicyInPost = postRepository.getConnectedUzytkownicyFromPostButBetter(post.getPostId());
        List<Uzytkownik> uzytkownicyInPost = uzytkownikRepository.getConnectedUzytkownicyFromPostButBetter(post.getPostId());

        fileUtils.deleteObraz(kom.getObraz());
        for (Komentarz odp : kom.getOdpowiedzi()) {
            fileUtils.deleteObraz(odp.getObraz());
        }
        komentarzRepository.removeKomentarz(kom.getKomentarzId());

        System.out.println("Aktualizacja użytowników");
        for (Uzytkownik u : uzytkownicyInPost) {

            System.out.println("Aktualizacja użytownika: " + u.getNazwa());
            komentarzRepository.updateUzytkownikKomentarzeOcenyCount(u.getUzytId());
        }

        System.out.println("Flex");
      //  komentarzRepository.updateUzytkownikKomentarzeOcenyCount(uzytkownicyInPost);

        System.out.println("Aktualizacja posta");
        komentarzRepository.updateKomentarzeCountInPostButWithFunniNewRelation(post.getPostId());
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

    private void saveKomentarzFile(MultipartFile file, Komentarz kom, Uzytkownik uzyt) throws FileUploadException {
        if (file != null) {
            String leObraz = fileStoreService.saveKomentarz(file, kom.getKomentarzId(), uzyt.getUzytId());
            if (leObraz == null) {
                throw new FileUploadException("Wystąpił błąd podczas wysyłania pliku");
            }
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
