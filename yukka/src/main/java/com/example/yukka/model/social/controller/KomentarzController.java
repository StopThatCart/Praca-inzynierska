package com.example.yukka.model.social.controller;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.service.KomentarzService;

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
        return ResponseEntity.ok(komentarzService.findByKomentarzId(komentarzId));
    }

    @GetMapping("/uzytkownik/{email}")
    public ResponseEntity<PageResponse<KomentarzResponse>> findKomentarzeOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        @PathVariable("email") String email, Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.findKomentarzeOfUzytkownik(page, size, email, connectedUser));
    }

    // TODO: dodawanie komentarza do wiadomości prywatnej
        /*
    @PostMapping("/wiadomoscPrywatna/{email1}/{email2}/komentarze")
    public ResponseEntity<Komentaz> addKomentarzToWiadomoscPrywatna(
                    @PathVariable("email1") String email1, 
                    @PathVariable("email2") String email2,
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addKomentarzToWiadomoscPrywatna(email1, email2, request, connectedUser));
    }
 */
    @PostMapping("/{komentarz-id}")
    public ResponseEntity<Komentarz> addOdpowiedzToKomentarz(
                    @PathVariable("komentarz-id") String komentarzId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addOdpowiedzToKomentarz(komentarzId, request, connectedUser));
    }

    @PostMapping("post/{post-id}/")
    public ResponseEntity<Komentarz> addKomentarzToPost(
                    @PathVariable("post-id") String postId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addKomentarzToPost(postId, request, connectedUser));
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
