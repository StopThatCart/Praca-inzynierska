package com.example.yukka.model.social.mappers;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.post.PostResponse;
import com.example.yukka.model.social.requests.PostRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Klasa PostMapper jest odpowiedzialna za mapowanie obiektów Post na różne reprezentacje.
 * 
 * <ul>
 * <li><strong>fileUtils</strong>: Narzędzie do operacji na plikach (nieużywane w tej klasie).</li>
 * <li><strong>commonMapperService</strong>: Usługa wspomagająca mapowanie obiektów.</li>
 * </ul>
 * 
 * Metody:
 * 
 * <ul>
 * <li><strong>toPostRequest(Post post)</strong>: Mapuje obiekt Post na PostRequest.</li>
 * <li><strong>toPost(@Valid PostRequest request)</strong>: Mapuje obiekt PostRequest na Post.</li>
 * <li><strong>toPostResponse(Post post)</strong>: Mapuje obiekt Post na PostResponse.</li>
 * <li><strong>postResponsetoPageResponse(Page<Post> posty)</strong>: Mapuje stronę obiektów Post na PageResponse<PostResponse>.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class PostMapper {


    @SuppressWarnings("unused")
    private final FileUtils fileUtils;
    private final CommonMapperService commonMapperService;

    
    

    /**
     * Konwertuje obiekt typu {@link Post} na obiekt typu {@link PostRequest}.
     *
     * @param post obiekt typu {@link Post}, który ma zostać przekonwertowany
     * @return obiekt typu {@link PostRequest} zawierający dane z obiektu {@link Post}
     */
    public PostRequest toPostRequest(Post post) {
        return PostRequest.builder()
            .tytul(post.getTytul())
            .opis(post.getOpis())
            .obraz(post.getObraz())
            .build();
    }

    /**
     * Konwertuje obiekt typu {@link PostRequest} na obiekt typu {@link Post}.
     *
     * @param request obiekt typu {@link PostRequest} zawierający dane do utworzenia nowego posta
     * @return nowo utworzony obiekt typu {@link Post}
     */
    public Post toPost(@Valid PostRequest request) {
        return Post.builder()
            .postId(UUID.randomUUID().toString())
            .tytul(request.getTytul())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .build();
    }

    /**
     * Konwertuje obiekt typu <strong>Post</strong> na obiekt typu <strong>PostResponse</strong>.
     *
     * @param post obiekt typu <strong>Post</strong> do konwersji
     * @return obiekt typu <strong>PostResponse</strong>
     */
     public PostResponse toPostResponse(Post post) {
        return commonMapperService.mapToPostResponse(post);
    }

    /**
     * Konwertuje obiekt Post na obiekt OcenaResponse.
     *
     * @param post obiekt typu <strong>Post</strong> do konwersji
     * @return obiekt typu <strong>OcenaResponse</strong> zawierający oceny pozytywne i negatywne posta
     */
    public OcenaResponse toOcenaResponse(Post post) {
        return OcenaResponse.builder()
            .ocenyLubi(post.getOcenyLubi())
            .ocenyNieLubi(post.getOcenyNieLubi())
            .build();
    }

    /**
     * Konwertuje stronę obiektów typu <strong>Post</strong> na obiekt typu <strong>PageResponse<PostResponse></strong>.
     *
     * @param posty strona obiektów typu <strong>Post</strong> do konwersji
     * @return obiekt typu <strong>PageResponse<PostResponse></strong>
     */
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
