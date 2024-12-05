package com.example.yukka.model.social.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.request.PostRequest;
import com.example.yukka.model.social.service.PostService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("posty")
@RequiredArgsConstructor
@Tag(name = "Post")
public class PostController {
    private final PostService postService;

    @GetMapping(value = "/{post-id}", produces="application/json")
    public ResponseEntity<PostResponse> findPostById(@PathVariable("post-id") String postId) {
        return ResponseEntity.ok(postService.findByPostId(postId));
    }

    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<PostResponse>> findAllPosty(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "szukaj", required = false) String szukaj) {
        return ResponseEntity.ok(postService.findAllPosts(page, size, szukaj));
    }

    @GetMapping(value = "/uzytkownik", produces="application/json")
    public ResponseEntity<PageResponse<PostResponse>> findAllPostyByConnectedUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(postService.findAllPostyByConnectedUzytkownik(page, size, connectedUser));
    }

    @GetMapping(value = "/uzytkownik/{nazwa}", produces="application/json")
    public ResponseEntity<PageResponse<PostResponse>> findAllPostyByUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("nazwa") String nazwa,
            Authentication connectedUser) {
        return ResponseEntity.ok(postService.findAllPostyByUzytkownik(page, size, nazwa, connectedUser));
    }

    // @GetMapping(value = "/uzytkownik/{nazwa}/count", produces="application/json")
    // public ResponseEntity<Integer> findAllPostyCountOfUzytkownik(
    //         @PathVariable("nazwa") String nazwa) {
    //     return ResponseEntity.ok(postService.findAllPostyCountOfUzytkownik(nazwa));
    // }

    @PostMapping(consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<Post> addPost(@Valid @RequestPart("request") PostRequest request, 
    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file, Authentication connectedUser) {
        return ResponseEntity.ok(postService.save(request, file, connectedUser));
    }

    @PutMapping(value = "/oceny", produces="application/json")
    public ResponseEntity<PostResponse> addOcenaToPost(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(postService.addOcenaToPost(request, connectedUser));
    }

    @DeleteMapping(value = "/oceny", produces="application/json")
    public ResponseEntity<String> removeOcenaFromPost(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        postService.removeOcenaFromPost(request, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{post-id}", produces="application/json")
    public ResponseEntity<String> removePost(
                    @PathVariable("post-id") String postId,
                    Authentication currentUser) {
                        
        postService.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }

}
