package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.UzytkownikRoslina;
import com.example.yukka.model.roslina.UzytkownikRoslinaRequest;
import com.example.yukka.model.roslina.UzytkownikRoslinaResponse;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UzytkownikRoslinaService {
    private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;

    private final UzytkownikRepository uzytkownikRepository;


    private final FileUtils fileUtils;
    private final FileStoreService fileStoreService;

    private final RoslinaMapper roslinaMapper;
    private final CommonMapperService commonMapperService;

    public Optional<UzytkownikRoslina> findByRoslinaId(String roslinaId) {
        return uzytkownikRoslinaRepository.findByRoslinaIdWithRelations(roslinaId);
    }


    public PageResponse<UzytkownikRoslinaResponse> findAllRoslinyOfUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());
        Page<UzytkownikRoslina> rosliny = uzytkownikRoslinaRepository.findAllRoslinyOfUzytkownik(uzyt.getUzytId(), pageable);
        List<UzytkownikRoslinaResponse> roslinyResponse = rosliny.stream()
                .map(roslinaMapper::toUzytkownikRoslinaResponse)
                .toList();
        return new PageResponse<>(
                roslinyResponse,
                rosliny.getNumber(),
                rosliny.getSize(),
                rosliny.getTotalElements(),
                rosliny.getTotalPages(),
                rosliny.isFirst(),
                rosliny.isLast()
        );
    }



    public Roslina save(UzytkownikRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<UzytkownikRoslina> roslina = uzytkownikRoslinaRepository.findByRoslinaId(request.getRoslinaId());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nUZYT IS PRESENT\n\n\n");
            return null;
        }
        
        if(request.areWlasciwosciEmpty()) {
            UzytkownikRoslina pl = roslinaMapper.toUzytkownikRoslina(request);
            return uzytkownikRoslinaRepository.addRoslina(uzyt.getUzytId(), pl);
        }

        return uzytkownikRoslinaRepository.addRoslina(uzyt.getUzytId(),
            request.getNazwa(), createRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

    }


    public Roslina save(UzytkownikRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;

        Optional<UzytkownikRoslina> roslina = uzytkownikRoslinaRepository.findByRoslinaId(request.getRoslinaId());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nUZYT IS PRESENT\n\n\n");
            return null;
        }
        
        if(request.areWlasciwosciEmpty()) {
            UzytkownikRoslina pl = roslinaMapper.toUzytkownikRoslina(request);
            return uzytkownikRoslinaRepository.addRoslina(uzyt.getUzytId(), pl);
        }

        return uzytkownikRoslinaRepository.addRoslina(uzyt.getUzytId(),
            request.getNazwa(), request.getRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

    }

    public Roslina save(UzytkownikRoslinaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<UzytkownikRoslina> roslina = uzytkownikRoslinaRepository.findByRoslinaId(request.getRoslinaId());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nUZYT IS PRESENT\n\n\n");
            return null;
        }
        
        String leObraz = fileStoreService.saveRoslina(file, request.getRoslinaId(), uzyt.getUzytId());
        request.setObraz(leObraz);
        if(request.areWlasciwosciEmpty()) {
            UzytkownikRoslina pl = roslinaMapper.toUzytkownikRoslina(request);
            UzytkownikRoslina ros = uzytkownikRoslinaRepository.addRoslina(uzyt.getUzytId(), pl);
            return ros;
        }
        Roslina ros = uzytkownikRoslinaRepository.addRoslina(
            uzyt.getUzytId(),
            request.getNazwa(), createRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
    }


    // Niechronione. Potem sie poprawi
    public UzytkownikRoslina update(UzytkownikRoslinaRequest request) {
        if(request.areWlasciwosciEmpty()) {
            return uzytkownikRoslinaRepository.updateRoslina(
            request.getNazwa(), request.getRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax());
        }
        
        return uzytkownikRoslinaRepository.updateRoslina(
            request.getNazwa(), request.getRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());
    }


    public void uploadUzytkownikRoslinaObraz(MultipartFile file, Authentication connectedUser, String roslinaId) {
        UzytkownikRoslina roslina = uzytkownikRoslinaRepository.findByRoslinaId(roslinaId).orElse(null);
        if (roslina == null) {
            return;
        }
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        if(!roslina.getUzytkownik().getUzytId().equals(uzyt.getUzytId())){
            return;
        }
        
        //fileUtils.deleteObraz(roslina.getObraz());
        String pfp = fileStoreService.saveUzytkownikRoslinaObraz(file, roslinaId, uzyt.getUsername());
        if(pfp == null){
            return;
        }
       
        roslina.setObraz(pfp);
        uzytkownikRoslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }


    public void deleteByRoslinaId(String roslinaId) {
        UzytkownikRoslina roslina = uzytkownikRoslinaRepository.findByRoslinaId(roslinaId).orElse(null);
        if (roslina == null) {
            return;
        }
        uzytkownikRoslinaRepository.deleteByRoslinaId(roslinaId);
    }



    public String createRoslinaId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<UzytkownikRoslina> kom =uzytkownikRoslinaRepository.findByRoslinaId(resultId);
            if(kom.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }
}
