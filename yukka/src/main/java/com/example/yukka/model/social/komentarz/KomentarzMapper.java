package com.example.yukka.model.social.komentarz;


import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.request.KomentarzRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KomentarzMapper {
    private final FileUtils fileUtils;
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

    public KomentarzDTO toKomentarzDTO(Komentarz komentarz) {
        return KomentarzDTO.builder()
            .id(komentarz.getId())
            .komentarzId(komentarz.getKomentarzId())
            .opis(komentarz.getOpis())
            .ocenyLubi(komentarz.getOcenyLubi())
            .ocenyNieLubi(komentarz.getOcenyNieLubi())
            .dataUtworzenia(komentarz.getDataUtworzenia())
            .odpowiedzi(komentarz.getOdpowiedzi().stream()
                .map(this::toKomentarzDTO)
                .collect(Collectors.toList()))
            .uzytkownikNazwa(komentarz.getUzytkownik().getNazwa())
            .obraz(fileUtils.readKomentarzObrazFile(komentarz.getObraz()))
            .avatar(fileUtils.readAvatarFile(komentarz.getUzytkownik().getAvatar()))
            .build();
    }

    public KomentarzResponse toKomentarzResponse(Komentarz komentarz) {
        return KomentarzResponse.builder()
            .id(komentarz.getId())
            .komentarzId(komentarz.getKomentarzId())
            .opis(komentarz.getOpis())
            .ocenyLubi(komentarz.getOcenyLubi())
            .ocenyNieLubi(komentarz.getOcenyNieLubi())
            .dataUtworzenia(komentarz.getDataUtworzenia())
            .uzytkownikNazwa(komentarz.getUzytkownik().getNazwa())
            .obraz(fileUtils.readKomentarzObrazFile(komentarz.getObraz()))
            .avatar(fileUtils.readAvatarFile(komentarz.getUzytkownik().getAvatar()))
          //  .odpowiedzi(komentarz.getOdpowiedzi().stream()
          //      .map(this::toKomentarzDTO)
          //      .collect(Collectors.toList()))
            .build();
    }

}
