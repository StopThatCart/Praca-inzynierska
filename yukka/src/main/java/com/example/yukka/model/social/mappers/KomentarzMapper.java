package com.example.yukka.model.social.mappers;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.komentarz.KomentarzResponse;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.requests.KomentarzRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * KomentarzMapper jest odpowiedzialny za mapowanie obiektów Komentarz na różne reprezentacje.
 * 
 * <ul>
 * <li><strong>commonMapperService</strong> - Usługa wspólna do mapowania obiektów.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 * <li><strong>toKomentarzRequest(Komentarz post)</strong> - Mapuje obiekt Komentarz na KomentarzRequest.</li>
 * <li><strong>toKomentarz(@Valid KomentarzRequest request)</strong> - Mapuje obiekt KomentarzRequest na Komentarz.</li>
 * <li><strong>toKomentarzResponse(Komentarz komentarz)</strong> - Mapuje obiekt Komentarz na KomentarzResponse.</li>
 * <li><strong>komentarzResponsetoPageResponse(Page<Komentarz> komentarze)</strong> - Mapuje stronę obiektów Komentarz na PageResponse z KomentarzResponse.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class KomentarzMapper {
    private final CommonMapperService commonMapperService;
    

    /**
     * Konwertuje obiekt typu <strong>Komentarz</strong> na obiekt typu <strong>KomentarzRequest</strong>.
     *
     * @param post obiekt typu <strong>Komentarz</strong> do konwersji
     * @return obiekt typu <strong>KomentarzRequest</strong>
     */
    public KomentarzRequest toKomentarzRequest(Komentarz post) {
        return KomentarzRequest.builder()
            .opis(post.getOpis())
            .obraz(post.getObraz())
            .build();
    }

    /**
     * Konwertuje obiekt typu <strong>KomentarzRequest</strong> na obiekt typu <strong>Komentarz</strong>.
     *
     * @param request obiekt typu <strong>KomentarzRequest</strong> do konwersji
     * @return obiekt typu <strong>Komentarz</strong>
     */
    public Komentarz toKomentarz(@Valid KomentarzRequest request) {
        return Komentarz.builder()
            .komentarzId(UUID.randomUUID().toString())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .build();
    }

    /**
     * Konwertuje obiekt typu <strong>Komentarz</strong> na obiekt typu <strong>KomentarzResponse</strong>.
     *
     * @param komentarz obiekt typu <strong>Komentarz</strong> do konwersji
     * @return obiekt typu <strong>KomentarzResponse</strong>
     */
    public KomentarzResponse toKomentarzResponse(Komentarz komentarz) {
        return commonMapperService.toKomentarzResponse(komentarz);
    }

    /**
     * Konwertuje obiekt Komentarz na obiekt OcenaResponse.
     *
     * @param komentarz obiekt typu <strong>Komentarz</strong> do konwersji
     * @return obiekt typu <strong>OcenaResponse</strong> zawierający oceny pozytywne i negatywne komentarza
     */
    public OcenaResponse toOcenaResponse(Komentarz komentarz) {
        return OcenaResponse.builder()
            .ocenyLubi(komentarz.getOcenyLubi())
            .ocenyNieLubi(komentarz.getOcenyNieLubi())
            .build();
    }

    /**
     * Konwertuje stronę obiektów typu <strong>Komentarz</strong> na obiekt typu <strong>PageResponse</strong> z obiektami typu <strong>KomentarzResponse</strong>.
     *
     * @param komentarze strona obiektów typu <strong>Komentarz</strong> do konwersji
     * @return obiekt typu <strong>PageResponse</strong> z obiektami typu <strong>KomentarzResponse</strong>
     */
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
