package com.example.yukka.model.social;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.komentarz.KomentarzSimpleResponse;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonMapperService {
    private final FileUtils fileUtils;


    public UzytkownikResponse toUzytkownikResponse(Uzytkownik uzytkownik) {
        if (uzytkownik == null) {
            return null;
        }
        return UzytkownikResponse.builder()
            .id(uzytkownik.getId())
            .uzytId(uzytkownik.getUzytId())
            .labels(uzytkownik.getLabels())
            .nazwa(uzytkownik.getNazwa())
            .email(uzytkownik.getEmail())
            .avatar(fileUtils.readAvatarFile(uzytkownik.getAvatar()))
            .komentarzeOcenyNegatywne(uzytkownik.getKomentarzeOcenyNegatywne())
            .komentarzeOcenyPozytywne(uzytkownik.getKomentarzeOcenyPozytywne())
            .postyOcenyNegatywne(uzytkownik.getPostyOcenyNegatywne())
            .postyOcenyPozytywne(uzytkownik.getPostyOcenyPozytywne())
            .dataUtworzenia(uzytkownik.getDataUtworzenia())
            .ban(uzytkownik.isBan())
            .build();
    }
    
    public PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .postId(post.getPostId())
                .tytul(post.getTytul())
                .opis(post.getOpis())
                .dataUtworzenia(post.getDataUtworzenia())
                .ocenyLubi(post.getOcenyLubi())
                .ocenyNieLubi(post.getOcenyNieLubi())
                .liczbaKomentarzy(post.getLiczbaKomentarzy())
                .komentarze(post.getKomentarze().stream()
                   .map(this::toKomentarzResponse)
                   .collect(Collectors.toList()))
                .uzytkownik(post.getAutor() != null ? post.getAutor().getNazwa() : null)
                .avatar(fileUtils.readAvatarFile(post.getAutor() != null ? post.getAutor().getAvatar(): null))
                .obraz(fileUtils.readPostObrazFile(post.getObraz()))
                .build();
    }


    public KomentarzResponse toKomentarzResponse(Komentarz komentarz) {
        if (komentarz == null) {
            return null;
        }

        Post post;
        if(komentarz.getPost() != null) {
            post = komentarz.getPost();
        } else if (komentarz.getWPoscie() != null) {
            post = komentarz.getWPoscie();
        } else {
            post = null;
        }
        
      //  System.out.println("Komentarz: " + komentarz);
        return KomentarzResponse.builder()
            .id(komentarz.getId())
            .komentarzId(komentarz.getKomentarzId())
            .post(post != null ? mapPostForKomentarzResponse(post) : null)
            .opis(komentarz.getOpis())
            .edytowany(komentarz.isEdytowany())
            .ocenyLubi(komentarz.getOcenyLubi())
            .ocenyNieLubi(komentarz.getOcenyNieLubi())
            .dataUtworzenia(komentarz.getDataUtworzenia())
            .odpowiadaKomentarzowi(mapOdpowiadaKomentarzowiForKomentarzResponse(komentarz.getOdpowiadaKomentarzowi()))
            .odpowiedzi(komentarz.getOdpowiedzi().stream()
                .map(this::toKomentarzResponse)
                .collect(Collectors.toList()))
            .uzytkownikNazwa(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getNazwa() : null)
            .obraz(fileUtils.readKomentarzObrazFile(komentarz.getObraz()))
            .avatar(fileUtils.readAvatarFile(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getAvatar() : null))
        //    .rozmowaPrywatna(komentarz.getRozmowaPrywatna() != null ? toRozmowaPrywatnaResponse(komentarz.getRozmowaPrywatna()) : null) // New mapping
            .build();
    }

    public PageResponse<RozmowaPrywatnaResponse> rozmowaPrywatnaPagetoPageRozmowaPrywatnaResponse(Page<RozmowaPrywatna> rozmowyPrywatne) {
            List<RozmowaPrywatnaResponse> rozmowyPrywatneResponse = rozmowyPrywatne.getContent().stream()
            .map(this::toRozmowaPrywatnaResponse)
            .collect(Collectors.toList());
    
            return new PageResponse<>(
                rozmowyPrywatneResponse,
                rozmowyPrywatne.getNumber(),
                rozmowyPrywatne.getSize(),
                rozmowyPrywatne.getTotalElements(),
                rozmowyPrywatne.getTotalPages(),
                rozmowyPrywatne.isFirst(),
                rozmowyPrywatne.isLast()
        );
    }

    // pomocnicze


    public RozmowaPrywatnaResponse toRozmowaPrywatnaResponse(RozmowaPrywatna rozmowaPrywatna) {
        if (rozmowaPrywatna == null) {
            return null;
        }
        return RozmowaPrywatnaResponse.builder()
            .id(rozmowaPrywatna.getId())
            .aktywna(rozmowaPrywatna.isAktywna())
            .nadawca(rozmowaPrywatna.getNadawca())
            .uzytkownicy(rozmowaPrywatna.getUzytkownicy().stream()
                .map(Uzytkownik::getNazwa)
                .collect(Collectors.toList()))
            .komentarze(rozmowaPrywatna.getWiadomosci().stream()
                .map(this::toKomentarzSimpleResponse)
                .collect(Collectors.toList()))
            .liczbaWiadomosci(rozmowaPrywatna.getLiczbaWiadomosci())
            .ostatnioAktualizowana(rozmowaPrywatna.getOstatnioAktualizowana())
            .build();
    }


    private KomentarzSimpleResponse toKomentarzSimpleResponse(Komentarz komentarz) {
        if (komentarz == null) {
            return null;
        }

        Post post;
        if(komentarz.getPost() != null) {
            post = komentarz.getPost();
        } else if (komentarz.getWPoscie() != null) {
            post = komentarz.getWPoscie();
        } else {
            post = null;
        }

        return KomentarzSimpleResponse.builder()
            .id(komentarz.getId())
            .komentarzId(komentarz.getKomentarzId())
            .opis(komentarz.getOpis())
            .postId(post != null ? post.getPostId() : null)
            .uzytkownikNazwa(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getNazwa() : null)
            .avatar(fileUtils.readAvatarFile(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getAvatar() : null))
            .obraz(fileUtils.readKomentarzObrazFile(komentarz.getObraz()))
            .build();
    }


    private PostResponse mapPostForKomentarzResponse(Post post) {
        if (post == null) {
            return null;
        }
        return PostResponse.builder()
            .postId(post.getPostId())
            .tytul(post.getTytul())
            .uzytkownik(post.getAutor() != null ? post.getAutor().getNazwa() : null)
            .build();
    }

    private KomentarzResponse mapOdpowiadaKomentarzowiForKomentarzResponse(Komentarz kom) {
        if (kom == null) {
            return null;
        }

        Post post;
        if(kom.getPost() != null) {
            post = kom.getPost();
        } else if (kom.getWPoscie() != null) {
            post = kom.getWPoscie();
        } else {
            post = null;
        }

        return KomentarzResponse.builder()
            .komentarzId(kom.getKomentarzId())
            .post(post != null ? mapPostForKomentarzResponse(post) : null)
            .opis(kom.getOpis())
            .uzytkownikNazwa(kom.getUzytkownik() != null ? kom.getUzytkownik().getNazwa() : null)
            .build();
    }

}
