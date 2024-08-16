package com.example.yukka.model.social.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostMapper;
import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.request.PostRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    @Value("${post.add.cooldown}")
    private Integer postAddCD;

    private final PostRepository postRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final FileStoreService fileStoreService;
    private final PostMapper postMapper;

    public PostResponse findByPostId(String postId) {
        return postRepository.findPostByPostIdButWithPath(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow();
    }

    public PageResponse<PostResponse> findAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPosts(pageable);
        System.out.println("\n\n\n post: " + posts.get().findFirst().get().toString() + "\n\n\n");
        List<PostResponse> postsResponse = posts.stream()
                .map(postMapper::toPostResponse)
                .toList();
        return new PageResponse<>(
                postsResponse,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }

    public PageResponse<PostResponse> findAllPostyByConnectedUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPostyByUzytkownik(pageable, user.getEmail());
        List<PostResponse> postsResponse = posts.stream()
                .map(postMapper::toPostResponse)
                .toList();
        return new PageResponse<>(
                postsResponse,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }

    public PageResponse<PostResponse> findAllPostyByUzytkownik(int page, int size, Authentication connectedUser, String email) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByEmail(email);
        if(targetUzyt.isEmpty()) {
            return new PageResponse<>();
        }
        if(!uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)){
            return new PageResponse<>();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPostyByUzytkownik(pageable, email);
        List<PostResponse> postsResponse = posts.stream()
                .map(postMapper::toPostResponse)
                .toList();
        return new PageResponse<>(
                postsResponse,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }


    public Post save(PostRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Post> newestPost = postRepository.findNewestPostOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastPost(newestPost);

        Post post = postMapper.toPost(request);
        post.setPostId(createPostId());
        
        return postRepository.addPost(uzyt.getEmail(), post).get();
    }

    public Post addOcenaToPost(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(request.getOcenialnyId()).orElseThrow();

        return postRepository.addOcenaToPost(uzyt.getEmail(), post.getPostId(), request.isLubi());
    }

    public void deletePost(String postId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(postId).orElseThrow();
        if(uzyt.hasAuthenticationRights(post.getAutor(), connectedUser)) {
            postRepository.deletePost(postId);
        }
    }

    public void uploadPostObraz(MultipartFile file, Authentication connectedUser, String postId) {
        Optional<Post> postOptional = postRepository.findPostByPostId(postId);
    
        if (postOptional.isEmpty()) {
            throw new NoSuchElementException("Nie znaleziono posta o podanym ID: " + postId);
        }
        Post post = postOptional.get();
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        String pfp = fileStoreService.savePost(file, post.getPostId(), uzyt.getNazwa());

        if (pfp == null) {
            throw new IllegalStateException("Nie udało się zapisać obrazu.");
        }
    
        
        post.setObraz(pfp);
        postRepository.updatePostObraz(post.getPostId(), post.getObraz());
    }


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
