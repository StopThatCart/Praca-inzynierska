package com.example.yukka.model.social.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.social.komentarz.KomentarzDTO;
import com.example.yukka.model.social.komentarz.KomentarzMapper;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.post.PostMapper;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;

@Service
public class KomentarzService {
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

    
    public KomentarzResponse findByKomentarzId(String komentarzId) {
        return  komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzResponse)
                .orElseThrow();
    }

    // TODO
    public KomentarzDTO findByKomentarzIdWithOdpowiedzi(String komentarzId) {
        return  komentarzRepository.findKomentarzByKomentarzId(komentarzId)
                .map(komentarzMapper::toKomentarzDTO)
                .orElseThrow();
    }

    public Object findUzytkownikKomentarze(String email, Authentication connectedUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUzytkownikKomentarze'");
    }

}
