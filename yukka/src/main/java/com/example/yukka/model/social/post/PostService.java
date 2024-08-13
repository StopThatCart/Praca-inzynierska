package com.example.yukka.model.social.post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // TODO: Paginacja


    public Optional<Post> findById(String postId) {
        return postRepository.findPostByPostId(postId);
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

    public void addOcena(OcenaRequest request) {
        postRepository.addOcenaToPost(request.getEmail(), request.getOcenialny_id(), request.isLubi());
    }

    public void addKomentarz(KomentarzRequest request, String email) {
        Komentarz kom = komentarzMapper.toKomentarz(request);
        do { 
            Optional<Komentarz> kom2 = komentarzRepository.findKomentarzByKomentarzId(kom.getKomentarzId());
            if(kom2.isEmpty()){
                break;
            }
            kom.setKomentarzId(UUID.randomUUID().toString());
        } while (true);
        
        komentarzRepository.addKomentarzToPost(email, request.getTargetId(), kom);
    }
    public void deletePost(String postId) {
        postRepository.deletePost(postId);
    }

    // TODO: Faktyczna implementacja
    public void uploadPostObraz(MultipartFile file, Authentication connectedUser, String latinName) {
        // TODO: Lepsza obsługa w przypadku nieznalezienia niczego
        Post post = postRepository.findPostByPostId(latinName).get();
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());

        var pfp = fileStoreService.saveFile(file, latinName, user.getUsername());
        
        post.setObraz(pfp);
        //roslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }
}
