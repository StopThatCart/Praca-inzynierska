package com.example.yukka.model.social.models.komentarz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje uproszczoną odpowiedź komentarza.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator komentarza.</li>
 *   <li><strong>uuid</strong>: Identyfikator komentarza.</li>
 *   <li><strong>opis</strong>: Treść komentarza.</li>
 *   <li><strong>edytowany</strong>: Flaga oznaczająca, czy komentarz został edytowany.</li>
 *   <li><strong>dataUtworzenia</strong>: Data utworzenia komentarza.</li>
 *   <li><strong>uzytkownikNazwa</strong>: Nazwa użytkownika, który dodał komentarz.</li>
 *   <li><strong>postUUID</strong>: Identyfikator posta, do którego odnosi się komentarz.</li>
 *   <li><strong>obraz</strong>: Obraz związany z komentarzem.</li>
 *   <li><strong>avatar</strong>: Avatar użytkownika, który dodał komentarz.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KomentarzSimpleResponse {
    private Long id;
    private String uuid;
    private String opis;
    private boolean edytowany;
    private String dataUtworzenia;
    private String uzytkownikNazwa;
    private String postUUID;
    public byte[] obraz;
    public byte[] avatar;
}
