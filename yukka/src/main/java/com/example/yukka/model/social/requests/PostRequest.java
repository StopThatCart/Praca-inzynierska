package com.example.yukka.model.social.requests;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie utworzenia nowego posta.
 * <ul>
 *   <li><strong>tytul</strong> - Tytuł posta. Jest to pole wymagane, które nie może zawierać więcej niż 100 znaków.</li>
 *   <li><strong>opis</strong> - Opis posta. Pole opcjonalne, które nie może zawierać więcej niż 3000 znaków.</li>
 *   <li><strong>obraz</strong> - Nazwa pliku obrazu. Pole opcjonalne, które nie może zawierać więcej niż 200 znaków.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class PostRequest {

    @NotEmpty(message = "Tytuł jest wymagany")
    @Size(max = 100, message = "Tytuł nie może zawierać więcej niż 100 znaków")
    private String tytul;

    // Czasem ktoś chce po prostu dać tytuł i tylko tyle
    //@NotEmpty(message = "Nazwa jest wymagana")
    @Size(max = 3000, message = "Opis nie może zawierać więcej niż 3000 znaków")
    private String opis;
    @Size(max = 200, message = "Nazwa pliku jest za długa")
    private String obraz;

}
