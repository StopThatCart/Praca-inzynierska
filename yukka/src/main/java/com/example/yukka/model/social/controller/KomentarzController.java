package com.example.yukka.model.social.controller;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.service.KomentarzService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/neo4j/komentarze")
@RequiredArgsConstructor
@Tag(name = "Komentarz")
public class KomentarzController {

    private final KomentarzService komentarzService;

    @GetMapping("/{komentarz-id}")
    public ResponseEntity<KomentarzResponse> findKomentarzById(@PathVariable("komentarz-id") String komentarzId) {
        
        return ResponseEntity.ok(komentarzService.findByKomentarzIdWithOdpowiedzi(komentarzId));
       // return ResponseEntity.ok(komentarzService.findByKomentarzId(komentarzId));
    }

    @GetMapping("/uzytkownicy/{email}")
    public ResponseEntity<PageResponse<KomentarzResponse>> findKomentarzeOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        @PathVariable("email") String email, Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.findKomentarzeOfUzytkownik(page, size, email, connectedUser));
    }

    @PostMapping("/{komentarz-id}")
    public ResponseEntity<Komentarz> addOdpowiedzToKomentarz(
                    @PathVariable("komentarz-id") String komentarzId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addOdpowiedzToKomentarz(komentarzId, request, connectedUser));
    }

    @PostMapping(value = "/{komentarz-id}", consumes = "multipart/form-data")
    public ResponseEntity<Komentarz> addOdpowiedzToKomentarz(
                    @PathVariable("komentarz-id") String komentarzId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    @Parameter() @RequestPart("file") MultipartFile file, 
                    Authentication connectedUser) throws FileUploadException {
        return ResponseEntity.ok(komentarzService.addOdpowiedzToKomentarz(komentarzId, request, file, connectedUser));
    }

    @PostMapping("posty/{post-id}/")
    public ResponseEntity<Komentarz> addKomentarzToPost(
                    @PathVariable("post-id") String postId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addKomentarzToPost(postId, request, connectedUser));
    }

    @PostMapping(value =  "posty/{post-id}/", consumes = "multipart/form-data")
    public ResponseEntity<Komentarz> addKomentarzToPost(
                    @PathVariable("post-id") String postId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    @Parameter() @RequestPart("file") MultipartFile file,
                    Authentication connectedUser) throws FileUploadException {
        return ResponseEntity.ok(komentarzService.addKomentarzToPost(postId, request, file, connectedUser));
    }

    @PostMapping(value =  "wiadomosciPrywatne/{other-uzyt-nazwa}/", consumes = "multipart/form-data")
    public ResponseEntity<Komentarz> addKomentarzToWiadomoscPrywatna(
                    @PathVariable("other-uzyt-nazwa") String otherUzytNazwa, 
                    @Valid @RequestBody KomentarzRequest request, 
                    @Parameter() @RequestPart("file") MultipartFile file,
                    Authentication connectedUser) throws FileUploadException {
        return ResponseEntity.ok(komentarzService.addKomentarzToWiadomoscPrywatna(otherUzytNazwa, request, file, connectedUser));
    }

    @PostMapping("wiadomosciPrywatne/{other-uzyt-nazwa}/")
    public ResponseEntity<Komentarz> addKomentarzToWiadomoscPrywatna(
                    @PathVariable("other-uzyt-nazwa") String otherUzytNazwa, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser){
        return ResponseEntity.ok(komentarzService.addKomentarzToWiadomoscPrywatna(otherUzytNazwa, request, connectedUser));
    }

    @PutMapping("/ocena")
    public ResponseEntity<Komentarz> addOcenaToKomentarz(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addOcenaToKomentarz(request, connectedUser));
    }

    @PatchMapping("/{komentarz-id}")
    public ResponseEntity<Komentarz> updateKomentarz(
                    @PathVariable("komentarz-id") String komentarzId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.updateKomentarz(komentarzId, request, connectedUser));
    }
/*
    // UWAGA: nietestowane
    @PostMapping(value = "/obraz/{post-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPostImage(
            @PathVariable("post-id") String postId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser) {
        komentarzService.uploadPostObraz(file, connectedUser, postId);
        return ResponseEntity.accepted().build();
    }
*/
    @DeleteMapping("/{komentarz-id}")
    public ResponseEntity<String> removeKomentarz(
                    @PathVariable("komentarz-id") String komentarzId,
                    Authentication currentUser) {

        komentarzService.deleteKomentarz(komentarzId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // Być może to wywalić i używać tylko removeKomentarz
    @DeleteMapping("{post-id}/komentarze/{komentarz-id}")
    public ResponseEntity<String> removeKomentarzFromPost(
                    @PathVariable("post-id") String postId,
                    @PathVariable("komentarz-id") String komentarzId,
                    Authentication connectedUser) {

        komentarzService.deleteKomentarzFromPost(postId, komentarzId, connectedUser);
        return ResponseEntity.noContent().build();
    }


}