package com.example.yukka.model.social.models.post.controller;
import static org.springframework.http.HttpStatus.CREATED;
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
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.models.post.PostResponse;
import com.example.yukka.model.social.requests.OcenaRequest;
import com.example.yukka.model.social.requests.PostRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Kontroler obsługujący operacje związane z postami.
 * 
 * <ul>
 * <li><strong>findPostById:</strong> Znajduje post na podstawie identyfikatora.</li>
 * <li><strong>findAllPosty:</strong> Znajduje wszystkie posty z opcjonalnym filtrem wyszukiwania.</li>
 * <li><strong>findAllPostyByConnectedUzytkownik:</strong> Znajduje wszystkie posty powiązane z zalogowanym użytkownikiem.</li>
 * <li><strong>findAllPostyByUzytkownik:</strong> Znajduje wszystkie posty użytkownika na podstawie jego nazwy.</li>
 * <li><strong>addPost:</strong> Dodaje nowy post z opcjonalnym plikiem.</li>
 * <li><strong>addOcenaToPost:</strong> Dodaje ocenę do postu.</li>
 * <li><strong>removeOcenaFromPost:</strong> Usuwa ocenę z postu.</li>
 * <li><strong>removePost:</strong> Usuwa post na podstawie identyfikatora.</li>
 * </ul>
 */
@RestController
@RequestMapping("posty")
@RequiredArgsConstructor
@Tag(name = "Post")
public class PostController {
    private final PostService postService;

    

    /**
     * Metoda obsługująca żądanie GET do wyszukania posta po jego identyfikatorze.
     *
     * @param uuid identyfikator posta
     * @return ResponseEntity zawierające obiekt PostResponse
     * <ul>
     *   <li><strong>uuid</strong> - identyfikator posta</li>
     *   <li><strong>ResponseEntity</strong> - odpowiedź HTTP zawierająca obiekt PostResponse</li>
     * </ul>
     */
    @GetMapping(value = "/{uuid}", produces="application/json")
    public ResponseEntity<PostResponse> findPostByUUID(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(postService.findByUUID(uuid));
    }

    /**
     * Metoda obsługująca żądanie GET na endpoint /{uuid}/check.
     * Sprawdza istnienie posta o podanym identyfikatorze.
     *
     * @param uuid identyfikator posta, który ma zostać sprawdzony
     * @return ResponseEntity zawierające obiekt PostResponse z informacjami o poście
     */
    @GetMapping(value = "/{uuid}/check", produces="application/json")
    public ResponseEntity<PostResponse> findPostByUUIDCheck(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(postService.findByUUIDCheck(uuid));
    }

    /**
     * Metoda obsługująca żądanie GET do wyszukania wszystkich postów.
     *
     * @param page numer strony wyników, domyślnie 0
     * @param size rozmiar strony wyników, domyślnie 10
     * @param search nazwa lub opis postu
     * @return ResponseEntity zawierające stronę wyników z postami
     */
    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<PostResponse>> findAllPosts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok(postService.findAllPosts(page, size, search));
    }

    /**
     * Metoda obsługująca żądanie GET do wyszukania wszystkich postów użytkownika na podstawie jego nazwy.
     *
     * @param page numer strony wyników, domyślnie 0
     * @param size rozmiar strony wyników, domyślnie 10
     * @param nazwa nazwa użytkownika
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające stronę wyników z postami
     * <ul>
     *   <li><strong>page</strong> - numer strony wyników</li>
     *   <li><strong>size</strong> - rozmiar strony wyników</li>
     *   <li><strong>nazwa</strong> - nazwa użytkownika</li>
     *   <li><strong>connectedUser</strong> - obiekt uwierzytelnionego użytkownika</li>
     *   <li><strong>ResponseEntity</strong> - odpowiedź HTTP zawierająca stronę wyników z postami</li>
     * </ul>
     */
    @GetMapping(value = "/uzytkownik/{nazwa}", produces="application/json")
    public ResponseEntity<PageResponse<PostResponse>> findAllPostyByUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("nazwa") String nazwa,
            Authentication connectedUser) {
        return ResponseEntity.ok(postService.findAllPostyByUzytkownik(page, size, nazwa, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie POST do dodania nowego posta z opcjonalnym plikiem.
     *
     * @param request obiekt żądania zawierający dane posta
     * @param file obiekt MultipartFile zawierający plik obrazu posta, opcjonalny
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające obiekt Post
     * <ul>
     *   <li><strong>request</strong> - obiekt żądania zawierający dane posta</li>
     *   <li><strong>file</strong> - obiekt MultipartFile zawierający plik obrazu posta</li>
     *   <li><strong>connectedUser</strong> - obiekt uwierzytelnionego użytkownika</li>
     *   <li><strong>ResponseEntity</strong> - odpowiedź HTTP zawierająca obiekt Post</li>
     * </ul>
     */
    @PostMapping(consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<PostResponse> addPost(@Valid @RequestPart("request") PostRequest request, 
    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file, Authentication connectedUser) {
        return ResponseEntity.status(CREATED).body(postService.save(request, file, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PUT do dodania oceny do posta.
     *
     * @param request obiekt żądania zawierający ocenę
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające obiekt OcenaResponse
     * <ul>
     *   <li><strong>request</strong> - obiekt żądania zawierający ocenę</li>
     *   <li><strong>connectedUser</strong> - obiekt uwierzytelnionego użytkownika</li>
     *   <li><strong>ResponseEntity</strong> - odpowiedź HTTP zawierająca obiekt OcenaResponse</li>
     * </ul>
     */
    @PutMapping(value = "/oceny", produces="application/json")
    public ResponseEntity<OcenaResponse> addOcenaToPost(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        return ResponseEntity.status(CREATED).body(postService.addOcenaToPost(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie DELETE do usunięcia posta na podstawie identyfikatora.
     *
     * @param uuid identyfikator posta
     * @param currentUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające informację o usunięciu posta
     * <ul>
     *   <li><strong>uuid</strong> - identyfikator posta</li>
     *   <li><strong>currentUser</strong> - obiekt uwierzytelnionego użytkownika</li>
     *   <li><strong>ResponseEntity</strong> - odpowiedź HTTP zawierająca informację o usunięciu posta</li>
     * </ul>
     */
    @DeleteMapping(value = "/{uuid}", produces="application/json")
    public ResponseEntity<String> removePost(@PathVariable("") String uuid, Authentication currentUser) {
        postService.deletePost(uuid, currentUser);
        return ResponseEntity.noContent().build();
    }

}
