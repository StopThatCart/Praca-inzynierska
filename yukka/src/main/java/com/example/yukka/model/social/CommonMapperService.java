package com.example.yukka.model.social;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonMapperService {
    private final FileUtils fileUtils;
    
    public PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .postId(post.getPostId())
                .tytul(post.getTytul())
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
        return KomentarzResponse.builder()
            .id(komentarz.getId())
            .komentarzId(komentarz.getKomentarzId())
            .post(komentarz.getPost() != null ?  mapPostForKomentarzResponse(komentarz.getPost()) : null)
            .opis(komentarz.getOpis())
            .edytowany(komentarz.isEdytowany())
            .ocenyLubi(komentarz.getOcenyLubi())
            .ocenyNieLubi(komentarz.getOcenyNieLubi())
            .dataUtworzenia(komentarz.getDataUtworzenia())
            .odpowiadaKomentarzowi(mapKomentarzForKomentarzResponse(komentarz.getOdpowiadaKomentarzowi()))
            .odpowiedzi(komentarz.getOdpowiedzi().stream()
                .map(this::toKomentarzResponse)
                .collect(Collectors.toList()))
            .uzytkownikNazwa(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getNazwa() : null)
            .obraz(fileUtils.readKomentarzObrazFile(komentarz.getObraz()))
            .avatar(fileUtils.readAvatarFile(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getAvatar() : null))
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

    private KomentarzResponse mapKomentarzForKomentarzResponse(Komentarz kom) {
        if (kom == null) {
            return null;
        }
        return KomentarzResponse.builder()
            .komentarzId(kom.getKomentarzId())
            .opis(kom.getOpis())
            .uzytkownikNazwa(kom.getUzytkownik() != null ? kom.getUzytkownik().getNazwa() : null)
            .build();
    }

}
