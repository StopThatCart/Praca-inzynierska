package com.example.yukka.model.social;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.DefaultImage;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.komentarz.KomentarzSimpleResponse;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;
import com.example.yukka.model.uzytkownik.requests.UstawieniaRequest;

import lombok.RequiredArgsConstructor;

/**
 * Serwis mapujący różne obiekty na odpowiedzi API.
 * <ul>
 * <li><strong>toSimpleAvatar</strong> - Mapuje obiekt Uzytkownik na UzytkownikResponse z prostym avatarem.</li>
 * <li><strong>toUzytkownikResponse</strong> - Mapuje obiekt Uzytkownik na pełną odpowiedź UzytkownikResponse.</li>
 * <li><strong>toUstawienia</strong> - Mapuje obiekt UstawieniaRequest na Ustawienia.</li>
 * <li><strong>mapToPostResponse</strong> - Mapuje obiekt Post na PostResponse.</li>
 * <li><strong>toKomentarzResponse</strong> - Mapuje obiekt Komentarz na KomentarzResponse.</li>
 * <li><strong>rozmowaPrywatnaPagetoPageRozmowaPrywatnaResponse</strong> - Mapuje stronę obiektów RozmowaPrywatna na PageResponse z RozmowaPrywatnaResponse.</li>
 * <li><strong>toRozmowaPrywatnaResponse</strong> - Mapuje obiekt RozmowaPrywatna na RozmowaPrywatnaResponse.</li>
 * <li><strong>toUzytkownikSimpleResponse</strong> - Mapuje obiekt Uzytkownik na uproszczoną odpowiedź UzytkownikResponse.</li>
 * <li><strong>toKomentarzSimpleResponse</strong> - Mapuje obiekt Komentarz na uproszczoną odpowiedź KomentarzSimpleResponse.</li>
 * <li><strong>mapPostForKomentarzResponse</strong> - Mapuje obiekt Post na uproszczoną odpowiedź PostResponse dla KomentarzResponse.</li>
 * <li><strong>mapOdpowiadaKomentarzowiForKomentarzResponse</strong> - Mapuje obiekt Komentarz na odpowiedź KomentarzResponse dla odpowiedzi na komentarz.</li>
 * <li><strong>timeAgo</strong> - Formatuje datę na tekst w stylu "czas temu" w języku polskim.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CommonMapperService {
    private final FileUtils fileUtils;

    

    /**
     * Konwertuje obiekt Uzytkownik na obiekt UzytkownikResponse zawierający tylko avatar.
     *
     * @param uzytkownik obiekt Uzytkownik, który ma być przekształcony
     * @return obiekt UzytkownikResponse zawierający avatar lub null, jeśli uzytkownik jest null
     */
    public UzytkownikResponse toSimpleAvatar(Uzytkownik uzytkownik) {
        if (uzytkownik == null) {
            return null;
        }
        return UzytkownikResponse.builder()
            .avatar(fileUtils.readFile(uzytkownik.getAvatar(), DefaultImage.AVATAR))
            .build();
    }

    /**
     * Konwertuje obiekt Uzytkownik na obiekt UzytkownikResponse.
     *
     * @param uzytkownik obiekt Uzytkownik, który ma być przekształcony
     * @return obiekt UzytkownikResponse lub null, jeśli uzytkownik jest null
     */
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
            .avatar(fileUtils.readFile(uzytkownik.getAvatar(), DefaultImage.AVATAR))
            .dataUtworzenia(uzytkownik.getDataUtworzenia())
            .ban(uzytkownik.isBan())
            .banDo(uzytkownik.getBanDo())
            .imie(uzytkownik.getImie())
            .miasto(uzytkownik.getMiasto())
            .miejsceZamieszkania(uzytkownik.getMiejsceZamieszkania())
            .opis(uzytkownik.getOpis())
            .ustawienia(uzytkownik.getUstawienia())
            .blokowaniUzytkownicy(uzytkownik.getBlokowaniUzytkownicy().stream()
                .map(this::toUzytkownikSimpleResponse)
                .collect(Collectors.toSet()))
            .blokujacyUzytkownicy(uzytkownik.getBlokujacyUzytkownicy().stream()
                .map(this::toUzytkownikSimpleResponse)
                .collect(Collectors.toSet()))
            .build();
    }

    /**
     * Konwertuje obiekt UstawieniaRequest na obiekt Ustawienia.
     *
     * @param ustawieniaRequest obiekt UstawieniaRequest, który ma być przekształcony
     * @return obiekt Ustawienia lub null, jeśli ustawieniaRequest są null
     */
    public Ustawienia toUstawienia(UstawieniaRequest ustawieniaRequest) {
        if (ustawieniaRequest == null) {
            return null;
        }
        return Ustawienia.builder()
            .statystykiProfilu(ustawieniaRequest.isStatystykiProfilu())
            .ogrodPokaz(ustawieniaRequest.isOgrodPokaz())
            .powiadomieniaKomentarzeOdpowiedz(ustawieniaRequest.isPowiadomieniaKomentarzeOdpowiedz())
            .powiadomieniaOgrodKwitnienie(ustawieniaRequest.isPowiadomieniaOgrodKwitnienie())
            .powiadomieniaOgrodOwocowanie(ustawieniaRequest.isPowiadomieniaOgrodOwocowanie())
            .build();
    }

    
    /**
     * Konwertuje obiekt Post na obiekt PostResponse.
     *
     * @param post obiekt Post, który ma być przekształcony
     * @return obiekt PostResponse lub null, jeśli post jest null
     */
    public PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .postId(post.getPostId())
                .tytul(post.getTytul())
                .opis(post.getOpis())
                .dataUtworzenia(timeAgo(post.getDataUtworzenia()))
                .ocenyLubi(post.getOcenyLubi())
                .ocenyNieLubi(post.getOcenyNieLubi())
                .liczbaKomentarzy(post.getKomentarzeWPoscie().size())
                .komentarze(post.getKomentarze().stream()
                   .map(this::toKomentarzResponse)
                   .collect(Collectors.toList()))
                .uzytkownik(post.getAutor() != null ? post.getAutor().getNazwa() : null)
                .avatar(fileUtils.readFile(post.getAutor() != null ? post.getAutor().getAvatar(): null, DefaultImage.AVATAR))
                .obraz(fileUtils.readFile(post.getObraz(), null))
                .build();
    }


    /**
     * Konwertuje obiekt Komentarz na obiekt KomentarzResponse.
     *
     * @param komentarz obiekt Komentarz, który ma być przekształcony
     * @return obiekt KomentarzResponse lub null, jeśli komentarz jest null
     */
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
            .dataUtworzenia(timeAgo(komentarz.getDataUtworzenia()))
            .odpowiadaKomentarzowi(mapOdpowiadaKomentarzowiForKomentarzResponse(komentarz.getOdpowiadaKomentarzowi()))
            .odpowiedzi(komentarz.getOdpowiedzi().stream()
                .map(this::toKomentarzResponse)
                .collect(Collectors.toList()))
            .uzytkownikNazwa(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getNazwa() : null)
            .obraz(fileUtils.readFile(komentarz.getObraz(), null))
            .avatar(fileUtils.readFile(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getAvatar() : null, DefaultImage.AVATAR))
        //    .rozmowaPrywatna(komentarz.getRozmowaPrywatna() != null ? toRozmowaPrywatnaResponse(komentarz.getRozmowaPrywatna()) : null) // New mapping
            .build();
    }

    /**
     * Konwertuje stronę obiektów RozmowaPrywatna na obiekt PageResponse z RozmowaPrywatnaResponse.
     *
     * @param rozmowyPrywatne strona obiektów RozmowaPrywatna, która ma być przekształcona
     * @return obiekt PageResponse z RozmowaPrywatnaResponse
     */
    public PageResponse<RozmowaPrywatnaResponse> rozmowaPrywatnaPagetoPageRozmowaPrywatnaResponse(
        Page<RozmowaPrywatna> rozmowyPrywatne) {
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

    /**
     * Konwertuje obiekt RozmowaPrywatna na obiekt RozmowaPrywatnaResponse.
     *
     * @param rozmowaPrywatna obiekt RozmowaPrywatna, który ma być przekształcony
     * @return obiekt RozmowaPrywatnaResponse lub null, jeśli rozmowaPrywatna jest null
     */
    public RozmowaPrywatnaResponse toRozmowaPrywatnaResponse(RozmowaPrywatna rozmowaPrywatna) {
        if (rozmowaPrywatna == null) {
            return null;
        }
        return RozmowaPrywatnaResponse.builder()
            .id(rozmowaPrywatna.getId())
            .aktywna(rozmowaPrywatna.isAktywna())
            .nadawca(rozmowaPrywatna.getNadawca())
            .uzytkownicy(rozmowaPrywatna.getUzytkownicy().stream()
                .map(this::toUzytkownikSimpleResponse)
                .collect(Collectors.toList()))
            .komentarze(rozmowaPrywatna.getWiadomosci().stream()
                .map(this::toKomentarzSimpleResponse)
                .collect(Collectors.toList()))
            .liczbaWiadomosci(rozmowaPrywatna.getWiadomosci().size())
            .ostatnioAktualizowana(timeAgo(rozmowaPrywatna.getOstatnioAktualizowana()))
            .build();
    }

    /**
     * Konwertuje obiekt Uzytkownik na uproszczoną odpowiedź UzytkownikResponse.
     *
     * @param uzytkownik obiekt Uzytkownik, który ma być przekształcony
     * @return uproszczona odpowiedź UzytkownikResponse lub null, jeśli uzytkownik jest null
     */
    private UzytkownikResponse toUzytkownikSimpleResponse(Uzytkownik uzytkownik) {
        return UzytkownikResponse.builder()
            .id(uzytkownik.getId())
            .uzytId(uzytkownik.getUzytId())
            .nazwa(uzytkownik.getNazwa())
            .avatar(fileUtils.readFile(uzytkownik.getAvatar(), DefaultImage.AVATAR))
            .build();
    }


    /**
     * Konwertuje obiekt Komentarz na uproszczoną odpowiedź KomentarzSimpleResponse.
     *
     * @param komentarz obiekt Komentarz, który ma być przekształcony
     * @return uproszczona odpowiedź KomentarzSimpleResponse lub null, jeśli komentarz jest null
     */
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
            .edytowany(komentarz.isEdytowany())
            .dataUtworzenia(timeAgo(komentarz.getDataUtworzenia()))
            .postId(post != null ? post.getPostId() : null)
            .uzytkownikNazwa(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getNazwa() : null)
            .avatar(fileUtils.readFile(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getAvatar() : null, DefaultImage.AVATAR))
            .obraz(fileUtils.readFile(komentarz.getObraz(), null))
            
            .build();
    }


    /**
     * Mapuje obiekt typu <strong>Post</strong> na obiekt typu <strong>PostResponse</strong> dla odpowiedzi komentarza.
     *
     * @param post obiekt typu <strong>Post</strong>, który ma być zmapowany
     * @return obiekt typu <strong>PostResponse</strong> lub <strong>null</strong>, jeśli post jest <strong>null</strong>
     */
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

    /**
     * Mapuje obiekt typu <strong>Komentarz</strong> na obiekt typu <strong>KomentarzResponse</strong> dla odpowiedzi na komentarz.
     *
     * @param kom obiekt typu <strong>Komentarz</strong>, który ma być zmapowany
     * @return obiekt typu <strong>KomentarzResponse</strong> lub <strong>null</strong>, jeśli kom jest <strong>null</strong>
     */
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

    /**
     * Formatuje datę na tekst w stylu "czas temu" w języku polskim.
     *
     * @param dateTime data, która ma być sformatowana
     * @return sformatowana data
     */
    public static String timeAgo(LocalDateTime dateTime) {
        PrettyTime p = new PrettyTime(Locale.forLanguageTag("pl"));
        return p.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

}
