package com.example.yukka.model.social.post;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.social.request.PostRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostMapper {


    private final FileUtils fileUtils;
    private final CommonMapperService commonMapperService;

    
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
                .opis(post.getOpis())   
                .dataUtworzenia(post.getDataUtworzenia())
                .ocenyLubi(post.getOcenyLubi())
                .ocenyNieLubi(post.getOcenyNieLubi())
                .liczbaKomentarzy(post.getLiczbaKomentarzy())
                .komentarze(post.getKomentarze().stream()
                    .map(commonMapperService::toKomentarzResponse)
                    .collect(Collectors.toList()))
                .uzytkownik(post.getAutor().getNazwa())
                .avatar(fileUtils.readAvatarFile(post.getAutor().getAvatar()))
                .obraz(fileUtils.readPostObrazFile(post.getObraz()))
                .build();
    }

    public PageResponse<PostResponse> postResponsetoPageResponse(Page<Post> posty) {
        List<PostResponse> postyResponse = posty.stream()
                .map(this::toPostResponse)
                .toList();
        return new PageResponse<>(
            postyResponse,
            posty.getNumber(),
            posty.getSize(),
            posty.getTotalElements(),
            posty.getTotalPages(),
            posty.isFirst(),
            posty.isLast()
        );
    }
}
