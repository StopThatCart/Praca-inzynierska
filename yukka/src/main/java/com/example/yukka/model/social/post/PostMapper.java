package com.example.yukka.model.social.post;


import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.request.PostRequest;

import jakarta.validation.Valid;

@Service
public class PostMapper {
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
                .uzytkownik(post.getAutor().getNazwa())
                .obraz(FileUtils.readFileFromLocation(post.getObraz()))
                .build();
    }

}
