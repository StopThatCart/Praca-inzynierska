package com.example.yukka.model.social.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.example.yukka.model.social.service.KomentarzService;

import io.swagger.v3.oas.annotations.tags.Tag;
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

/*
    @GetMapping
    public ResponseEntity<PageResponse<KomentarzResponse>> findAllPosty(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.findAllPosts(page, size));
    }

    @GetMapping("/uzytkownik")
    public ResponseEntity<PageResponse<KomentarzResponse>> findAllPostyByUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.findAllPostyByUzytkownik(page, size, connectedUser));
    }

    @PatchMapping("/ocena")
    public ResponseEntity<Long> addOcena(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        Uzytkownik user = ((Uzytkownik) connectedUser.getPrincipal());
        return ResponseEntity.ok(komentarzService.addOcena(request, connectedUser));
    }

    @PatchMapping("{post-id}/komentarze")
    public ResponseEntity<String> addKomentarz(
                    @PathVariable("post-id") String postId, 
                    @Valid @RequestBody KomentarzRequest request, 
                    Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.addKomentarz(postId, request, connectedUser));
    }

    @DeleteMapping("{post-id}/komentarze/{komentarz-id}")
    public ResponseEntity<String> removeKomentarz(
                    @PathVariable("post-id") String postId,
                    @PathVariable("komentarz-id") String komentarzId,
                    Authentication connectedUser) {

        komentarzService.deleteKomentarz(postId, komentarzId, connectedUser);
        return ResponseEntity.noContent().build();
    }

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

    @DeleteMapping("/{post-id}")
    public ResponseEntity<String> removePost(
                    @PathVariable("post-id") String postId,
                    Authentication currentUser) {

        komentarzService.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }
*/


}
