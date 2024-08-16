package com.example.yukka.model.social.post;


import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.komentarz.KomentarzMapper;
import com.example.yukka.model.social.request.PostRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostMapper {
    private final KomentarzMapper komentarzMapper;
    private final FileUtils fileUtils;
    public PostRequest toPostRequest(Post post) {
        return PostRequest.builder()
            .tytul(post.getTytul())
            .opis(post.getOpis())
            .obraz(post.getObraz())
            .build();
    }

    public Post toPost(@Valid PostRequest request) {
        return Post.builder()
            .postId(UUID.randomUUID().toString())
            .tytul(request.getTytul())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .build();
    }

     public PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .postId(post.getPostId())
                .tytul(post.getTytul())
                .ocenyLubi(post.getOcenyLubi())
                .ocenyNieLubi(post.getOcenyNieLubi())
                .liczbaKomentarzy(post.getLiczbaKomentarzy())
                .komentarze(post.getKomentarze().stream()
                    .map(komentarzMapper::toKomentarzDTO)
                    .collect(Collectors.toList()))
                .uzytkownik(post.getAutor().getNazwa())
                .obraz(fileUtils.readPostObrazFile(post.getObraz()))
                .build();
    }

}
