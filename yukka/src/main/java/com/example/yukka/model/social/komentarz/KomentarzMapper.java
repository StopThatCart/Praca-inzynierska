package com.example.yukka.model.social.komentarz;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.social.request.KomentarzRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KomentarzMapper {
    private final FileUtils fileUtils;
    private final CommonMapperService commonMapperService;
    public KomentarzRequest toKomentarzRequest(Komentarz post) {
        return KomentarzRequest.builder()
            .opis(post.getOpis())
            .obraz(post.getObraz())
            .build();
    }

    public Komentarz toKomentarz(@Valid KomentarzRequest request) {
        return Komentarz.builder()
            .komentarzId(UUID.randomUUID().toString())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .build();
    }

    public KomentarzResponse toKomentarzResponse(Komentarz komentarz) {
        return commonMapperService.toKomentarzResponse(komentarz);
    }
/*
    public KomentarzResponse toKomentarzResponse(Komentarz komentarz) {
        System.out.println("post: " + komentarz.getPost());
        return KomentarzResponse.builder()
            .id(komentarz.getId())
            .komentarzId(komentarz.getKomentarzId())
            .post(komentarz.getPost() != null ? commonMapperService.mapToPostResponse(komentarz.getPost()) : null)
          //  .post(komentarz.getPost() != null ? PostResponse.builder().postId(komentarz.getPost().getPostId()).build() : null)
            .opis(komentarz.getOpis())
            .edytowany(komentarz.isEdytowany())
            .ocenyLubi(komentarz.getOcenyLubi())
            .ocenyNieLubi(komentarz.getOcenyNieLubi())
            .dataUtworzenia(komentarz.getDataUtworzenia())
            .uzytkownikNazwa(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getNazwa() : null)
            .obraz(fileUtils.readKomentarzObrazFile(komentarz.getObraz()))
            .avatar(fileUtils.readAvatarFile(komentarz.getUzytkownik() != null ? komentarz.getUzytkownik().getAvatar() : null))
            .odpowiedzi(komentarz.getOdpowiedzi().stream()
                .map(this::toKomentarzDTO)
                .collect(Collectors.toList()))
            .build();
    }

    public PageResponse<KomentarzResponse> toPageResponse(List<KomentarzResponse> komentarzeResponse, Page<Komentarz> komentarze) {
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
 */
    public PageResponse<KomentarzResponse> komentarzResponsetoPageResponse(List<KomentarzResponse> komentarzeResponse, Page<Komentarz> komentarze) {
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

    

}
