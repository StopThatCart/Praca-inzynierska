package com.example.yukka.model.social.komentarz;
import java.util.List;

import com.example.yukka.model.social.post.PostResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź komentarza w systemie.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator komentarza.</li>
 * <li><strong>komentarzId</strong>: Identyfikator komentarza.</li>
 * <li><strong>opis</strong>: Treść komentarza.</li>
 * <li><strong>edytowany</strong>: Flaga wskazująca, czy komentarz został edytowany.</li>
 * <li><strong>ocenyLubi</strong>: Liczba pozytywnych ocen komentarza.</li>
 * <li><strong>ocenyNieLubi</strong>: Liczba negatywnych ocen komentarza.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia komentarza.</li>
 * <li><strong>post</strong>: Powiązany post, jeśli istnieje.</li>
 * <li><strong>odpowiadaKomentarzowi</strong>: Komentarz, na który odpowiada ten komentarz, jeśli istnieje.</li>
 * <li><strong>odpowiedzi</strong>: Lista odpowiedzi na ten komentarz.</li>
 * <li><strong>uzytkownikNazwa</strong>: Nazwa użytkownika, który dodał komentarz.</li>
 * <li><strong>obraz</strong>: Obraz powiązany z komentarzem.</li>
 * <li><strong>avatar</strong>: Avatar użytkownika, który dodał komentarz.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KomentarzResponse {
    private Long id;
    private String komentarzId;
    private String opis;
    private boolean edytowany;
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
    private String dataUtworzenia;
     @JsonInclude(JsonInclude.Include.NON_NULL)
    private PostResponse post;
    //private RozmowaPrywatnaResponse rozmowaPrywatna;
    @JsonInclude(JsonInclude.Include.NON_NULL) 
    private KomentarzResponse odpowiadaKomentarzowi;
    private List<KomentarzResponse> odpowiedzi;
    private String uzytkownikNazwa;
    public byte[] obraz;
    public byte[] avatar;
}
