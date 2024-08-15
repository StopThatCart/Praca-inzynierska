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
import com.example.yukka.model.social.Ocenil;
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
@RequestMapping("/rest/neo4j/posty")
@RequiredArgsConstructor
@Tag(name = "Post")
public class PostController {
    private final PostService postService;

    @GetMapping("/{post-id}")
    public ResponseEntity<PostResponse> findPostById(@PathVariable("post-id") String postId) {
        return ResponseEntity.ok(postService.findByPostId(postId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PostResponse>> findAllPosty(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(postService.findAllPosts(page, size));
    }

    @GetMapping("/uzytkownik")
    public ResponseEntity<PageResponse<PostResponse>> findAllPostyByConnectedUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(postService.findAllPostyByConnectedUzytkownik(page, size, connectedUser));
    }

    @GetMapping("/uzytkownik/{email}")
    public ResponseEntity<PageResponse<PostResponse>> findAllPostyByUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser,
            @PathVariable("email") String email) {
        return ResponseEntity.ok(postService.findAllPostyByUzytkownik(page, size, connectedUser, email));
    }

    @PostMapping
    public ResponseEntity<Post> addPost(@Valid @RequestBody PostRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(postService.save(request, connectedUser));
    }


    @PutMapping("/ocena")
    public ResponseEntity<Ocenil> addOcenaToPost(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(postService.addOcenaToPost(request, connectedUser));
    }


    // UWAGA: nietestowane
    @PostMapping(value = "/obraz/{post-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPostImage(
            @PathVariable("post-id") String postId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser) {
        postService.uploadPostObraz(file, connectedUser, postId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<String> removePost(
                    @PathVariable("post-id") String postId,
                    Authentication currentUser) {

        postService.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }



}
