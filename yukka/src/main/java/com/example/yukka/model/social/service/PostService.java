package com.example.yukka.model.social.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostMapper;
import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.social.repository.KomentarzRepository;
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
    private final KomentarzRepository komentarzRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikService uzytkownikService;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostResponse findByPostId(String postId) {
        Post post = postRepository.findPostByPostIdButWithPath(postId).orElse(null);
        if(post == null) {
            throw new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId);
        }

        return postRepository.findPostByPostIdButWithPath(postId)
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
            return new PageResponse<>();
        }
        if(!uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)){
            return new PageResponse<>();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPostyByUzytkownik(nazwa, pageable);
        return postMapper.postResponsetoPageResponse(posts);
    }


    @Transactional(readOnly = true)
    public Integer findAllPostyCountOfUzytkownik(String nazwa) {
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByNazwa(nazwa);
        if(targetUzyt.isEmpty()) {
            return 0;
        }
        return postRepository.findAllPostyCountOfUzytkownik(nazwa);
    }


    public Post save(PostRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Post> newestPost = postRepository.findNewestPostOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastPost(newestPost);
        
        return save(request, uzyt);
    }

    public Post save(PostRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;

        Post post = postMapper.toPost(request);
        if(post.getPostId() == null) {
            post.setPostId(createPostId());
        }
        
        
        return postRepository.addPost(uzyt.getEmail(), post, LocalDateTime.now()).get();
    }

    public Post save(PostRequest request, MultipartFile file, Authentication connectedUser) throws FileUploadException {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        
        Optional<Post> newestPost = postRepository.findNewestPostOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastPost(newestPost);

        return save(request, file, uzyt);
    }

    public Post save(PostRequest request, MultipartFile file, Uzytkownik connectedUser) throws FileUploadException {
        Uzytkownik uzyt = connectedUser;

        Post post = postMapper.toPost(request);
        post.setPostId(createPostId());

        String leObraz = fileStoreService.savePost(file, post.getPostId(), uzyt.getUzytId());
        if(leObraz == null) {
            throw new FileUploadException("Wystąpił błąd podczas wysyłania pliku");
        }
        post.setObraz(leObraz);
        
        return postRepository.addPost(uzyt.getEmail(), post, LocalDateTime.now()).get();
    }

    public PostResponse addOcenaToPost(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(request.getOcenialnyId())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getOcenialnyId()));
        
        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), uzyt);

        post =  postRepository.addOcenaToPost(uzyt.getEmail(), post.getPostId(), request.isLubi());
        postRepository.updateOcenyCountOfPost(post.getPostId());

        return postMapper.toPostResponse(post);
    }

    public void removeOcenaFromPost(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(request.getOcenialnyId())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getOcenialnyId()));
        
        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), uzyt);

        postRepository.removeOcenaFromPost(uzyt.getEmail(), post.getPostId());
        postRepository.updateOcenyCountOfPost(post.getPostId());
    }

    public void deletePost(String postId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(postId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        if(!uzyt.hasAuthenticationRights(post.getAutor(), connectedUser)) {
            throw new IllegalArgumentException("Nie masz uprawnień do usunięcia tego posta");
        }
        List<Uzytkownik> uzytkownicyInPost = uzytkownikRepository.getConnectedUzytkownicyFromPostButBetter(postId);

        fileUtils.deleteObraz(post.getObraz());
        postRepository.deletePost(postId);

        for (Uzytkownik u : uzytkownicyInPost) {
            komentarzRepository.updateUzytkownikKomentarzeOcenyCount(u.getUzytId());
        }

        for (Komentarz kom : post.getKomentarze()) {
            fileUtils.deleteObraz(kom.getObraz());
        }

        postRepository.updateOcenyCountOfPost(post.getPostId());
    }

    public void deletePost(String postId, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Post post = postRepository.findPostByPostId(postId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
      //  if(uzyt.hasAuthenticationRights(post.getAutor(), connectedUser)) {
       //     postRepository.deletePost(postId);
      //  }
        List<Uzytkownik> uzytkownicyInPost = uzytkownikRepository.getConnectedUzytkownicyFromPostButBetter(postId);

        fileUtils.deleteObraz(post.getObraz());
        postRepository.deletePost(postId);

        for (Uzytkownik u : uzytkownicyInPost) {
            System.out.println("Aktualizacja użytownika LE POST: " + u.getNazwa());
            komentarzRepository.updateUzytkownikKomentarzeOcenyCount(u.getUzytId());
        }

        for (Komentarz kom : post.getKomentarze()) {
            fileUtils.deleteObraz(kom.getObraz());
        }

        System.out.println("Flex");
        postRepository.updateOcenyCountOfPost(post.getPostId());
    }

    public void seedRemovePostyObrazy() {
        List<Post> posty = postRepository.findAll();
        for (Post post : posty) {
            fileUtils.deleteObraz(post.getObraz());
            postRepository.deletePostButBetter(post.getPostId());
        }
    }

    /* 
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
 */

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
