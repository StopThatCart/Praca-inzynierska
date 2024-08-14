package com.example.yukka.model.social.post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzMapper;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.request.PostRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    KomentarzRepository komentarzRepository;
    @Autowired
    FileStoreService fileStoreService;

    @Autowired
    PostMapper postMapper;

    @Autowired
    KomentarzMapper komentarzMapper;

    public PostResponse findByPostId(String postId) {
        return postRepository.findPostByPostId(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow();
    }

    public PageResponse<PostResponse> findAllPosts(int page, int size, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPosts(pageable);
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

    public PageResponse<PostResponse> findAllPostyByUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataUtworzenia").descending());
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


    public Post save(PostRequest request, String email) {

        Post post = postMapper.toPost(request);
        do { 
            Optional<Post> post2 = postRepository.findPostByPostId(post.getPostId());
            if(post2.isEmpty()){
                break;
            }
            post.setPostId(UUID.randomUUID().toString());
        } while (true);
        
        return postRepository.addPost(email, post).get();
    }

    public Long addOcena(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(request.getOcenialny_id()).orElseThrow();

        return postRepository.addOcenaToPost(user.getEmail(), post.getPostId(), request.isLubi());
    }

    public String addKomentarz(String postId, KomentarzRequest request, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostId(postId).orElseThrow();

        Komentarz kom = komentarzMapper.toKomentarz(request);
        do { 
            Optional<Komentarz> kom2 = komentarzRepository.findKomentarzByKomentarzId(kom.getKomentarzId());
            if(kom2.isEmpty()){
                break;
            }
            kom.setKomentarzId(UUID.randomUUID().toString());
        } while (true);
        
        return komentarzRepository.addKomentarzToPost(user.getEmail(), post.getPostId(), kom);
    }

    public void deleteKomentarz(String postId, String komentarzId, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Post> post = postRepository.findPostByPostId(postId);
        Optional<Komentarz> kom = komentarzRepository.findKomentarzByKomentarzId(komentarzId);

        if (kom.isEmpty() || post.isEmpty()) {
            return;
        }

        if(!user.isAdmin() && !user.isPracownik()) {
            if(!user.getEmail().equals(kom.get().getUzytkownik().getEmail())){
                return;
            }
        }
        
        komentarzRepository.removeKomentarz(komentarzId);
    }

    public void deletePost(String postId, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Post> post = postRepository.findPostByPostId(postId);
        if(!user.isAdmin() && !user.isPracownik()) {
            if(!user.getEmail().equals(post.get().getAutor().getEmail())){
                return;
            }
        }
        postRepository.deletePost(postId);
    }

    // TODO: Faktyczna implementacja + Jak zmieni się nazwę użytkownika, to też jego folder z obrazami
    public void uploadPostObraz(MultipartFile file, Authentication connectedUser, String latinName) {
        // TODO: Lepsza obsługa w przypadku nieznalezienia niczego
        Post post = postRepository.findPostByPostId(latinName).get();
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());

        var pfp = fileStoreService.savePost(file, post.getPostId(), user.getNazwa());
        
        post.setObraz(pfp);
        postRepository.updatePostObraz(post.getPostId(), post.getObraz());
        //roslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }
}
