package com.example.yukka.model.social.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostMapper;
import com.example.yukka.model.social.post.PostResponse;

import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.request.PostRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    @Value("${post.add.cooldown}")
    private Integer postAddCD;

    private final PostRepository postRepository;

    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikService uzytkownikService;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostResponse findByPostId(String postId) {
        Post post = postRepository.findPostByPostId(postId).orElse(null);
        if(post == null) {
            throw new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId);
        }

        return postRepository.findPostByPostId(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> findAllPosts(int page, int size, String szukaj) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPosts(szukaj, pageable);

        return postMapper.postResponsetoPageResponse(posts);
    }

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> findAllPostyByConnectedUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPostyByUzytkownik(user.getEmail(), pageable);
        return postMapper.postResponsetoPageResponse(posts);
    }

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> findAllPostyByUzytkownik(int page, int size, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByNazwa(nazwa);
        if(targetUzyt.isEmpty()) {
            throw new EntityNotFoundException("Nie znaleziono użytkownika o podanej nazwie: " + nazwa);
        }
        if(!uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)){
            throw new ForbiddenException("Nie masz uprawnień do przeglądania postów tego użytkownika");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPostyByUzytkownik(nazwa, pageable);
        return postMapper.postResponsetoPageResponse(posts);
    }


    // @Transactional(readOnly = true)
    // public Integer findAllPostyCountOfUzytkownik(String nazwa) {
    //     Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByNazwa(nazwa);
    //     if(targetUzyt.isEmpty()) {
    //         return 0;
    //     }
    //     return postRepository.findAllPostyCountOfUzytkownik(nazwa);
    // }

    public Post save(PostRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        
        Optional<Post> newestPost = postRepository.findNewestPostOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastPost(newestPost);

        return save(request, file, uzyt);
    }

    public Post save(PostRequest request, MultipartFile file, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;

        Post post = postMapper.toPost(request);
        post.setPostId(createPostId());

        if (file != null) {
            post.setObraz(fileStoreService.savePost(file, post.getPostId(), uzyt.getUzytId()));
        }

        return postRepository.addPost(uzyt.getEmail(), post, LocalDateTime.now()).get();
    }

    public PostResponse addOcenaToPost(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(request.getOcenialnyId())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getOcenialnyId()));
        
        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), uzyt);

        post =  postRepository.addOcenaToPost(uzyt.getEmail(), post.getPostId(), request.isLubi());
        // postRepository.updateOcenyCountOfPost(post.getPostId());

        return postMapper.toPostResponse(post);
    }

    public void removeOcenaFromPost(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(request.getOcenialnyId())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getOcenialnyId()));
        
        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), uzyt);

        postRepository.removeOcenaFromPost(uzyt.getEmail(), post.getPostId());
        // postRepository.updateOcenyCountOfPost(post.getPostId());
    }

    public void deletePost(String postId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(postId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        if(!uzyt.hasAuthenticationRights(post.getAutor(), connectedUser)) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia tego posta");
        }

        deletePost(postId, uzyt);
    }

    public void deletePost(String postId, Uzytkownik uzyt) {
        Post post = postRepository.findPostByPostId(postId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));

        fileUtils.deleteObraz(post.getObraz());
        postRepository.deletePost(postId);

        for (Komentarz kom : post.getKomentarze()) {
            fileUtils.deleteObraz(kom.getObraz());
        }
    }

    public void seedRemovePostyObrazy() {
        List<Post> posty = postRepository.findAll();
        for (Post post : posty) {
            fileUtils.deleteObraz(post.getObraz());
            postRepository.deletePostButBetter(post.getPostId());
        }
    }

    // Pomocnicze

    public String createPostId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<Post> kom = postRepository.findPostByPostId(resultId);
            if(kom.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }

    private void checkTimeSinceLastPost(Optional<Post> newestPost) {
        if (newestPost.isPresent()) {
            LocalDateTime lastPostTime = newestPost.get().getDataUtworzenia();
            LocalDateTime now = LocalDateTime.now();
    
            Duration timeElapsed = Duration.between(lastPostTime, now);
            if (timeElapsed.getSeconds() < postAddCD) {
                throw new IllegalStateException("Musisz poczekać " + postAddCD + " sekund przed dodaniem kolejnego posta.");
            }
        }
    }
}
