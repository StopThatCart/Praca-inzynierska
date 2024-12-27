package com.example.yukka.model.social.requests;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie dodania komentarza.
 * 
 * <ul>
 * <li><strong>opis</strong> - Opis komentarza, maksymalnie 3000 znaków.</li>
 * <li><strong>obraz</strong> - Opcjonalny obraz dołączony do komentarza.</li>
 * <li><strong>targetId</strong> - Identyfikator obiektu, który jest komentowany. Pole wymagane.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 * <li><strong>isOpisOrObrazPresent</strong> - Sprawdza, czy komentarz zawiera opis lub obraz. Zwraca wartość boolean.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class KomentarzRequest {
    @Size(max = 3000, message = "Komentarz nie może zawierać więcej niż 3000 znaków")
    private String opis;

    private String obraz;

    @NotEmpty(message = "Wymagane jest wskazanie co dokładnie jest komentowane")
    private String targetId;


    
    /** 
     * @return boolean
     */
    @AssertTrue(message = "Komentarz musi zawierać opis lub obraz")
    private boolean isOpisOrObrazPresent() {
        return (opis != null && !opis.isEmpty()) || (obraz != null && !obraz.isEmpty());
    }

}
