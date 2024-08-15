package com.example.yukka.model.social.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.model.social.komentarz.Komentarz;
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

    public PageResponse<KomentarzResponse> findAllRosliny(int page, int size, String email, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("kom.dataUtworzenia").descending());
        Page<Komentarz> komentarze = komentarzRepository.findKomentarzeOfUzytkownik(pageable, email);
        List<KomentarzResponse> komentarzeResponse = komentarze.stream()
                .map(komentarzMapper::toKomentarzResponse)
                .toList();
        return new PageResponse<>(
                komentarzeResponse,
                komentarze.getNumber(),
                komentarze.getSize(),
                komentarze.getTotalElements(),
                komentarze.getTotalPages(),
                komentarze.isFirst(),
                komentarze.isLast()
        );
    }

}
