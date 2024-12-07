package com.example.yukka.model.social.post;


import java.util.List;
import java.util.UUID;

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


    @SuppressWarnings("unused")
    private final FileUtils fileUtils;
    private final CommonMapperService commonMapperService;

    
    
    /** 
     * @param post
     * @return PostRequest
     */
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
        return commonMapperService.mapToPostResponse(post);
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
