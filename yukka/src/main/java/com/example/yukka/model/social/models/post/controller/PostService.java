package com.example.yukka.model.social.models.post.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.social.mappers.PostMapper;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.post.PostResponse;
import com.example.yukka.model.social.requests.OcenaRequest;
import com.example.yukka.model.social.requests.PostRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.RequiredArgsConstructor;

/**
 * Serwis odpowiedzialny za operacje związane z postami.
 * 
 * Metody:
 * 
 * <ul>
 * <li><strong>findByPostId</strong>: Znajduje post na podstawie jego ID.</li>
 * <li><strong>findAllPosts</strong>: Znajduje wszystkie posty z możliwością paginacji i filtrowania.</li>
 * <li><strong>findAllPostyByConnectedUzytkownik</strong>: Znajduje wszystkie posty powiązane z zalogowanym użytkownikiem.</li>
 * <li><strong>findAllPostyByUzytkownik</strong>: Znajduje wszystkie posty konkretnego użytkownika z możliwością paginacji.</li>
 * <li><strong>save</strong>: Zapisuje nowy post.</li>
 * <li><strong>addOcenaToPost</strong>: Dodaje ocenę do posta.</li>
 * <li><strong>removeOcenaFromPost</strong>: Usuwa ocenę z posta.</li>
 * <li><strong>deletePost</strong>: Usuwa post na podstawie jego ID.</li>
 * <li><strong>seedRemovePostyObrazy</strong>: Usuwa obrazy z wszystkich postów.</li>
 * <li><strong>createPostId</strong>: Tworzy unikalne ID dla posta.</li>
 * <li><strong>checkTimeSinceLastPost</strong>: Sprawdza czas, który upłynął od ostatniego posta.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    @Value("${post.add.cooldown}")
    private Integer postAddCD;

    private final PostRepository postRepository;

    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikService uzytkownikService;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;
    private final PostMapper postMapper;

    

    /**
     * Znajduje post na podstawie podanego identyfikatora postu.
     *
     * @param postId <ul><li><strong>String</strong>: Identyfikator postu do znalezienia.</li></ul>
     * @return <ul><li><strong>PostResponse</strong>: Odpowiedź zawierająca dane znalezionego postu.</li></ul>
     * @throws EntityNotFoundException <ul><li><strong>EntityNotFoundException</strong>: Wyjątek rzucany, gdy post o podanym identyfikatorze nie zostanie znaleziony.</li></ul>
     */
    @Transactional(readOnly = true)
    public PostResponse findByPostId(String postId) {
        Post post = postRepository.findPostByPostId(postId)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));

        return postMapper.toPostResponse(post);
    }

    /**
     * Znajduje post na podstawie podanego ID posta, ale bez komentarzy.
     * Używane do sprawdzania, czy post o podanym ID istnieje.
     * 
     * @param postId ID posta, który ma zostać znaleziony.
     * @return PostResponse obiekt zawierający dane znalezionego posta.
     * @throws EntityNotFoundException jeśli post o podanym ID nie zostanie znaleziony.
     */
    @Transactional(readOnly = true)
    public PostResponse findByPostIdCheck(String postId) {
        Post post = postRepository.findPostByPostIdCheck(postId)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));

        return postMapper.toPostResponse(post);
    }

    /**
     * Znajduje wszystkie posty z możliwością paginacji i filtrowania.
     *
     * @param page <ul><li><strong>int</strong>: Numer strony.</li></ul>
     * @param size <ul><li><strong>int</strong>: Rozmiar strony.</li></ul>
     * @param szukaj <ul><li><strong>String</strong>: Wartość, po której ma być filtrowana lista postów.</li></ul>
     * @return <ul><li><strong>PageResponse(PostResponse)</strong>: Odpowiedź zawierająca listę postów oraz dane paginacji.</li></ul>
     */
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> findAllPosts(int page, int size, String szukaj) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPosts(szukaj, pageable);

        return postMapper.postResponsetoPageResponse(posts);
    }

    /**
     * Znajduje wszystkie posty użytkownika na podstawie nazwy użytkownika.
     *
     * @param page <strong>Numer strony</strong> - numer strony do pobrania.
     * @param size <strong>Rozmiar strony</strong> - liczba elementów na stronie.
     * @param nazwa <strong>Nazwa użytkownika</strong> - nazwa użytkownika, którego posty mają być pobrane.
     * @param connectedUser <strong>Uwierzytelniony użytkownik</strong> - aktualnie zalogowany użytkownik.
     * @return <strong>PageResponse<PostResponse></strong> - strona z postami użytkownika.
     * @throws EntityNotFoundException <strong>Nie znaleziono użytkownika</strong> - jeśli użytkownik o podanej nazwie nie istnieje.
     * @throws ForbiddenException <strong>Brak uprawnień</strong> - jeśli zalogowany użytkownik nie ma uprawnień do przeglądania postów tego użytkownika.
     */
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> findAllPostyByUzytkownik(int page, int size, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByNazwa(nazwa);
        if(targetUzyt.isEmpty()) {
            throw new EntityNotFoundException("Nie znaleziono użytkownika o podanej nazwie: " + nazwa);
        }
        if(!uzyt.hasAuthenticationRights(targetUzyt.get())){
            throw new ForbiddenException("Nie masz uprawnień do przeglądania postów tego użytkownika");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("post.dataUtworzenia").descending());
        Page<Post> posts = postRepository.findAllPostyByUzytkownik(nazwa, pageable);
        return postMapper.postResponsetoPageResponse(posts);
    }

    /**
     * Zapisuje nowy post.
     *
     * @param request <ul><li><strong>PostRequest</strong>: Dane nowego postu.</li></ul>
     * @param file <ul><li><strong>MultipartFile</strong>: Obraz postu.</li></ul>
     * @param connectedUser <ul><li><strong>Authentication</strong>: Zalogowany użytkownik.</li></ul>
     * @return <ul><li><strong>Post</strong>: Zapisany post.</li></ul>
     */
    public PostResponse save(PostRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        
        Optional<Post> newestPost = postRepository.findNewestPostOfUzytkownik(uzyt.getEmail());
        checkTimeSinceLastPost(newestPost);

        return postMapper.toPostResponse(save(request, file, uzyt));
    }

    /**
     * Dodaje ocenę do posta.
     *
     * @param request <ul><li><strong>OcenaRequest</strong>: Dane oceny.</li></ul>
     * @param connectedUser <ul><li><strong>Authentication</strong>: Zalogowany użytkownik.</li></ul>
     * @return <ul><li><strong>PostResponse</strong>: Odpowiedź zawierająca dane posta po dodaniu oceny.</li></ul>
     */
    public Post save(PostRequest request, MultipartFile file, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;

        Post post = postMapper.toPost(request);
        post.setPostId(createPostId());

        if (file != null) {
            post.setObraz(fileStoreService.savePost(file, post.getPostId(), uzyt.getUzytId()));
        }

        return postRepository.addPost(uzyt.getEmail(), post, LocalDateTime.now()).get();
    }

    /**
     * Dodaje ocenę do posta.
     *
     * @param request <ul><li><strong>OcenaRequest</strong>: Dane oceny.</li></ul>
     * @param connectedUser <ul><li><strong>Authentication</strong>: Zalogowany użytkownik.</li></ul>
     * @return <ul><li><strong>OcenaResponse</strong>: Odpowiedź zawierająca oceny posta.</li></ul>
     */
    public OcenaResponse addOcenaToPost(OcenaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostIdCheck(request.getOcenialnyId())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + request.getOcenialnyId()));
        
        uzytkownikService.sprawdzBlokowanie(post.getAutor().getNazwa(), uzyt);

        post =  postRepository.addOcenaToPost(uzyt.getEmail(), post.getPostId(), request.isLubi());
        return postMapper.toOcenaResponse(post);
    }

    /**
     * Usuwa post na podstawie jego identyfikatora.
     *
     * @param postId <ul><li><strong>String</strong>: Identyfikator postu do usunięcia.</li></ul>
     * @param connectedUser <ul><li><strong>Authentication</strong>: Zalogowany użytkownik.</li></ul>
     * @throws EntityNotFoundException <ul><li><strong>EntityNotFoundException</strong>: Wyjątek rzucany, gdy post o podanym identyfikatorze nie zostanie znaleziony.</li></ul>
     * @throws ForbiddenException <ul><li><strong>ForbiddenException</strong>: Wyjątek rzucany, gdy zalogowany użytkownik nie ma uprawnień do usunięcia posta.</li></ul>
     */
    public void deletePost(String postId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Post post = postRepository.findPostByPostIdCheck(postId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));
        if(!uzyt.hasAuthenticationRights(post.getAutor())) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia tego posta");
        }

        deletePost(postId, uzyt);
    }

    /**
     * Usuwa post na podstawie jego identyfikatora.
     *
     * @param postId <ul><li><strong>String</strong>: Identyfikator postu do usunięcia.</li></ul>
     * @param uzyt <ul><li><strong>Uzytkownik</strong>: Użytkownik, który usuwa post.</li></ul>
     */
    public void deletePost(String postId, Uzytkownik uzyt) {
        Post post = postRepository.findPostByPostId(postId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono posta o podanym ID: " + postId));

        fileUtils.deleteObraz(post.getObraz());
        postRepository.deletePost(postId);

        for (Komentarz kom : post.getKomentarze()) {
            fileUtils.deleteObraz(kom.getObraz());
        }
    }

    /**
     * Usuwa obrazy z wszystkich postów.
     */
    public void seedRemovePostyObrazy() {
        List<Post> posty = postRepository.findAll();
        for (Post post : posty) {
            fileUtils.deleteObraz(post.getObraz());
            postRepository.deletePostButBetter(post.getPostId());
        }
    }

    // Pomocnicze
    /**
     * Tworzy unikalne ID dla posta.
     *
     * @return <ul><li><strong>String</strong>: Unikalne ID posta.</li></ul>
     */
    public String createPostId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<Post> kom = postRepository.findPostByPostIdCheck(resultId);
            if(kom.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }

    /**
     * Sprawdza czas, który upłynął od ostatniego posta.
     *
     * @param newestPost <ul><li><strong>Optional&lt;Post&gt;</strong>: Ostatni post użytkownika.</li></ul>
     * @throws IllegalStateException <ul><li><strong>IllegalStateException</strong>: Wyjątek rzucany, gdy użytkownik próbuje dodać post zbyt szybko po poprzednim.</li></ul>
     */
    private void checkTimeSinceLastPost(Optional<Post> newestPost) {
        if (newestPost.isPresent()) {
            LocalDateTime lastPostTime = newestPost.get().getDataUtworzenia();
            LocalDateTime now = LocalDateTime.now();
    
            Duration timeElapsed = Duration.between(lastPostTime, now);
            if (timeElapsed.getSeconds() < postAddCD) {
                throw new IllegalStateException("Musisz poczekać " + postAddCD + " sekund przed dodaniem kolejnego posta.");
            }
        }
    }
}
