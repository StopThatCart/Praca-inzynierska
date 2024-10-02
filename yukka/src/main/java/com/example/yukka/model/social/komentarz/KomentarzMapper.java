package com.example.yukka.model.social.komentarz;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.social.request.KomentarzRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KomentarzMapper {
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

    public PageResponse<KomentarzResponse> komentarzResponsetoPageResponse(Page<Komentarz> komentarze) {
        List<KomentarzResponse> komentarzeResponse = komentarze.stream()
                .map(this::toKomentarzResponse)
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

    

}
