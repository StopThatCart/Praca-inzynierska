package com.example.yukka.model.social.komentarz;


import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.yukka.model.social.request.KomentarzRequest;

import jakarta.validation.Valid;

@Service
public class KomentarzMapper {
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

}
