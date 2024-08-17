package com.example.yukka.model.social.service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.handler.BannedUzytkownikException;
import com.example.yukka.model.social.RozmowaPrywatna;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzMapper;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostMapper;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KomentarzService {
    @Value("${komentarz.add.cooldown}")
    private Integer komAddCD;

    private final PostRepository postRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final KomentarzRepository komentarzRepository;
    private final FileStoreService fileStoreService;

    PostMapper postMapper;
    private final KomentarzMapper komentarzMapper;

    
    public KomentarzResponse findByKomentarzId(String komentarzId) {
        return  komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow();
    }

    public KomentarzResponse findByKomentarzIdWithOdpowiedzi(String komentarzId) {
        return  komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow();
    }

    public PageResponse<KomentarzResponse> findKomentarzeOfUzytkownik(int page, int size, String email, Authentication connectedUser) {
        /* 
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByEmail(email);
        if (targetUzyt.isEmpty() || !uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)) {
            return new PageResponse<>();
        }*/
        System.out.println("\n\n\n EMAIL: " + email + "\n\n\n");
        Pageable pageable = PageRequest.of(page, size, Sort.by("komentarz.dataUtworzenia").descending());
        System.out.println("KOMENTARZE:  AAA ");

        Page<Komentarz> komentarze = komentarzRepository.findKomentarzeOfUzytkownik(email, pageable);
       // for(Komentarz k : komentarze) {
      //      System.out.println(k.toString());
      //  }
        //return new PageResponse<>();
        
        List<KomentarzResponse> komentarzeResponse = komentarze.stream()
                .map(komentarzMapper::toKomentarzResponse)
                .toList();
         
        return komentarzMapper.komentarzResponsetoPageResponse(komentarzeResponse, komentarze);
      /*
                return new PageResponse<>(
                komentarzeResponse,
                komentarze.getNumber(),
                komentarze.getSize(),
                komentarze.getTotalElements(),
                komentarze.getTotalPages(),
                komentarze.isFirst(),
                komentarze.isLast()
        );
        */
    }

    public Komentarz addOcenaToKomentarz(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        if(uzyt.isBan()) {
            throw new BannedUzytkownikException("Użytkownik jest zbanowany");
        }
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(request.getOcenialnyId())
                .orElseThrow();
        return komentarzRepository.addOcenaToKomentarz(uzyt.getEmail(), komentarz.getKomentarzId(), request.isLubi());
    }

    public Komentarz addKomentarzToWiadomoscPrywatna(String otherUzytNazwa, @Valid KomentarzRequest request,
        Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        if(uzyt.getNazwa().equals(otherUzytNazwa)) {
            return null;
        }
        Uzytkownik uzyt2 = uzytkownikRepository.findByNazwa(otherUzytNazwa).orElseThrow();

        RozmowaPrywatna uwrp = uzytkownikRepository.findRozmowaPrywatna(uzyt2.getNazwa(), uzyt.getNazwa()).orElseThrow()
            .getRozmowyPrywatne()
            .stream()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Nie znaleziono rozmowy prywatnej"));

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());

        return komentarzRepository.addKomentarzToRozmowaPrywatna(uzyt.getNazwa(), uzyt2.getNazwa(), kom);
    }

    public Komentarz addKomentarzToWiadomoscPrywatna(String otherUzytNazwa, @Valid KomentarzRequest request,
        MultipartFile file, Authentication connectedUser) throws FileUploadException {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        if(uzyt.getNazwa().equals(otherUzytNazwa)) {
            return null;
        }
        Uzytkownik uzyt2 = uzytkownikRepository.findByNazwa(otherUzytNazwa).orElseThrow();

        RozmowaPrywatna uwrp = getRozmowaPrywatna(uzyt2, uzyt);

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        return komentarzRepository.addKomentarzToRozmowaPrywatna(uzyt.getNazwa(), uzyt2.getNazwa(), kom);
    }

    public Komentarz addKomentarzToPost(String postId, KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByPostId(postId).orElseThrow();

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());
        
        return komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getPostId(), kom);
    }

    public Komentarz addKomentarzToPost(String postId, KomentarzRequest request,  MultipartFile file, Authentication connectedUser) throws FileUploadException {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Post post = postRepository.findPostByPostId(postId).orElseThrow();

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        return komentarzRepository.addKomentarzToPost(uzyt.getEmail(), post.getPostId(), kom);
    }

    public Komentarz addOdpowiedzToKomentarz(String komentarzId, @Valid KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Komentarz kom = komentarzMapper.toKomentarz(request);
        kom.setKomentarzId(createKomentarzId());
        
        return komentarzRepository.addKomentarzToKomentarz(uzyt.getEmail(), kom, komentarzId);
    }

    public Komentarz addOdpowiedzToKomentarz(String komentarzId, @Valid KomentarzRequest request, MultipartFile file, Authentication connectedUser) throws FileUploadException {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Komentarz kom = createKomentarz(request);
        saveKomentarzFile(file, kom, uzyt);
        
        return komentarzRepository.addKomentarzToKomentarz(uzyt.getEmail(), kom, komentarzId);
    }

    public Komentarz updateKomentarz(String komentarzId, @Valid KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Komentarz kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .filter(k -> uzyt.hasAuthenticationRights(k.getUzytkownik(), connectedUser))
                .orElseThrow();
        return komentarzRepository.updateKomentarz(uzyt.getEmail(), komentarzId, kom);
    }


    public void deleteKomentarz(String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());        
        Komentarz komentarz = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .filter(k -> uzyt.hasAuthenticationRights(k.getUzytkownik(), connectedUser))
                .orElseThrow();
        komentarzRepository.removeKomentarz(komentarzId);
    }

    public void deleteKomentarzFromPost(String postId, String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(postId).orElseThrow();
        Komentarz kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .filter(k -> uzyt.hasAuthenticationRights(k.getUzytkownik(), connectedUser))
                .orElseThrow();
        komentarzRepository.removeKomentarz(komentarzId);
    }

    // Pomocnicze

    private RozmowaPrywatna getRozmowaPrywatna(Uzytkownik uzyt2, Uzytkownik uzyt) {
        return uzytkownikRepository.findRozmowaPrywatna(uzyt2.getNazwa(), uzyt.getNazwa()).orElseThrow()
                .getRozmowyPrywatne().stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono rozmowy prywatnej"));
    }

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

    /* 
    public void uploadKomentarzObraz(MultipartFile file, Authentication connectedUser, String komentarzId) {
        Optional<Komentarz> komentarzOptional = komentarzRepository.findKomentarzByKomentarzId(komentarzId);
    
        if (komentarzOptional.isEmpty()) {
            throw new NoSuchElementException("Nie znaleziono posta o podanym ID: " + komentarzId);
        }
        Komentarz komentarz = komentarzOptional.get();
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        String pfp = fileStoreService.saveKomentarz(file, komentarz.getKomentarzId(), uzyt.getNazwa());

        if (pfp == null) {
            throw new IllegalStateException("Nie udało się zapisać obrazu.");
        }
    
        
        komentarz.setObraz(pfp);
        //komentarzRepository.updateKomentarzObraz(komentarz.getKomentarzId(), komentarz.getObraz());
    }
        */

}
