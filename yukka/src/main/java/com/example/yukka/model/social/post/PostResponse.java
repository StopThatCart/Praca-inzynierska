package com.example.yukka.model.social.post;
import java.util.List;

import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje formę response obiektu Post.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator posta.</li>
 * <li><strong>postId</strong>: Identyfikator posta.</li>
 * <li><strong>tytul</strong>: Tytuł posta.</li>
 * <li><strong>opis</strong>: Opis posta.</li>
 * <li><strong>ocenyLubi</strong>: Liczba polubień posta.</li>
 * <li><strong>ocenyNieLubi</strong>: Liczba niepolubień posta.</li>
 * <li><strong>liczbaKomentarzy</strong>: Liczba komentarzy pod postem.</li>
 * <li><strong>komentarze</strong>: Lista komentarzy do posta.</li>
 * <li><strong>uzytkownik</strong>: Nazwa użytkownika, który opublikował post.</li>
 * <li><strong>obraz</strong>: Obraz dołączony do posta.</li>
 * <li><strong>avatar</strong>: Avatar użytkownika, który opublikował post.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia posta.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String postId;
    private String tytul;
    private String opis;
     @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer ocenyLubi;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer ocenyNieLubi;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer liczbaKomentarzy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KomentarzResponse> komentarze;
    private String uzytkownik;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] obraz;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] avatar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dataUtworzenia;
    
}
