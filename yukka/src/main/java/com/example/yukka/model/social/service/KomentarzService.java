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

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.social.Ocenil;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzDTO;
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

    PostRepository postRepository;
    UzytkownikRepository uzytkownikRepository;
    KomentarzRepository komentarzRepository;
    FileStoreService fileStoreService;

    PostMapper postMapper;
    KomentarzMapper komentarzMapper;

    
    public KomentarzResponse findByKomentarzId(String komentarzId) {
        return  komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow();
    }

    public KomentarzDTO findByKomentarzIdWithOdpowiedzi(String komentarzId) {
        return  komentarzRepository.findKomentarzWithOdpowiedziByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzDTO)
                .orElseThrow();
    }

    public PageResponse<KomentarzResponse> findKomentarzeOfUzytkownik(int page, int size, String email, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByEmail(email);
        if(targetUzyt.isEmpty()) {
            return new PageResponse<>();
        }
        if(!user.hasAuthenticationRights(targetUzyt.get(), connectedUser)){
            return new PageResponse<>();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("komentarz.dataUtworzenia").descending());
        Page<Komentarz> komentarze = komentarzRepository.findKomentarzeOfUzytkownik(pageable, email);
        List<KomentarzResponse> komentarzeResponse = komentarze.stream()
                .map(komentarzMapper::toKomentarzResponse)
                .toList();
        return new PageResponse<>(
                komentarzeResponse,
                komentarze.getNumber(),
                komentarze.getSize(),
                komentarze.getTotalElements(),
                komentarze.getTotalPages(),
                komentarze.isFirst(),
                komentarze.isLast()
        );
    }

    public Ocenil addOcenaToKomentarz(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> komentarz = komentarzRepository.findKomentarzByKomentarzId(request.getOcenialnyId());
        if(komentarz.isEmpty()) {
            return null;
        }
        return komentarzRepository.addOcenaToKomentarz(user.getEmail(), komentarz.get().getKomentarzId(), request.isLubi());
    }

    public String addKomentarzToWiadomoscPrywatna(String email1, String email2, @Valid KomentarzRequest request,
        Authentication connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addKomentarzToWiadomoscPrywatna'");
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

    public Komentarz addOdpowiedzToKomentarz(String komentarzId, @Valid KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> newestKomentarz = komentarzRepository.findNewestKomentarzOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastKomentarz(newestKomentarz);

        Komentarz kom = komentarzMapper.toKomentarz(request); 
        return komentarzRepository.addKomentarzToKomentarz(uzyt.getEmail(), kom, komentarzId);
    }

    // TODO: Stwórz parametr UpdatedBy czy Updated czy kij wie co.
    public Komentarz updateKomentarz(String komentarzId, @Valid KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Komentarz> kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId);

        if (kom.isEmpty()) {
            return null;
        }
        if(!uzyt.hasAuthenticationRights(kom.get().getUzytkownik(), connectedUser)){
            return null;
        }
        return komentarzRepository.updateKomentarz(uzyt.getEmail(), komentarzId, kom.get());
    }


    public void deleteKomentarz(String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());        
        Optional<Komentarz> komentarz = komentarzRepository.findKomentarzByKomentarzId(komentarzId);
        
        if(komentarz.isPresent()){
            if(uzyt.hasAuthenticationRights(komentarz.get().getUzytkownik(), connectedUser)) {
                komentarzRepository.removeKomentarz(komentarzId);
            }
        }
    }

    public void deleteKomentarzFromPost(String postId, String komentarzId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Post> post = postRepository.findPostByPostId(postId);
        Optional<Komentarz> kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId);

        if (kom.isEmpty() || post.isEmpty()) {
            return;
        }
        if(!uzyt.hasAuthenticationRights(kom.get().getUzytkownik(), connectedUser))

        komentarzRepository.removeKomentarz(komentarzId);
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
