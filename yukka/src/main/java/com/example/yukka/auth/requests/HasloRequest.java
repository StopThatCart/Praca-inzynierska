package com.example.yukka.auth.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie zmiany hasła.
 * 
 * <p>Ta klasa jest używan`a do enkapsulacji danych wymaganych do żądania zmiany hasła,
 * w tym tokenu, nowego hasła i potwierdzenia nowego hasła.</p>
 * 
 * <p>Adnotacje są używane do wymuszania ograniczeń walidacyjnych na polach:</p>
 * <ul>
 *   <li>{@code @NotEmpty} zapewnia, że pola nie są puste.</li>
 *   <li>{@code @Size(min = 8)} zapewnia, że nowe hasło i jego potwierdzenie mają co najmniej 8 znaków.</li>
 *   <li>{@code @JsonIgnore} zapobiega serializacji metody {@code isHasloMatching}.</li>
 *   <li>{@code @AssertTrue} zapewnia, że nowe hasło i jego potwierdzenie są zgodne.</li>
 * </ul>
 * 
 * <p>Klasa używa adnotacji Lombok do generowania kodu szablonowego:</p>
 * <ul>
 *   <li>{@code @ToString} generuje metodę {@code toString}.</li>
 *   <li>{@code @Getter} i {@code @Setter} generują metody getter i setter dla wszystkich pól.</li>
 *   <li>{@code @Builder} zapewnia wzorzec budowniczego do tworzenia obiektów.</li>
 *   <li>{@code @AllArgsC`onstructor} generuje konstruktor z wszystkimi polami jako parametrami.</li>
 * </ul>
 * 
 * <p>Zapewniony jest również domyślny konstruktor bezargumentowy.</p>
 * 
 * <p>Pola:</p>
 * <ul>
 *   <li>{@code token} - Token do żądania zmiany hasła. Nie może być pusty.</li>
 *   <li>{@code noweHaslo} - Nowe hasło. Musi mieć co najmniej 8 znaków i nie może być puste.</li>
 *   <li>{@code nowePowtorzHaslo} - Potwierdzenie nowego hasła. Musi mieć co najmniej 8 znaków i nie może być puste.</li>
 * </ul>
 * 
 * <p>Metody:</p>
 * <ul>
 *   <li>{@code isHasloMatching} - Waliduje, czy nowe hasło i jego potwierdzenie są zgodne.</li>
 * </ul>
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class HasloRequest {

    @NotEmpty(message = "Token nie może być pusty")
    private String token;

    @NotEmpty(message = "Nowe hasło nie może być puste")
    @Size(min = 8, message = "Nowe hasło powinno mieć co najmniej 8 znaków")
    private String noweHaslo;

    @NotEmpty(message = "Powtórzone hasło nie może być puste")
    @Size(min = 8, message = "Nowe hasło powinno mieć co najmniej 8 znaków")
    private String nowePowtorzHaslo;

    
    /** 
     * @return boolean
     */
    @JsonIgnore
    @AssertTrue(message = "Hasło i potwierdzenie hasła muszą być takie same")
    public boolean isHasloMatching() {
        return noweHaslo != null && noweHaslo.equals(nowePowtorzHaslo);
    }

    public HasloRequest() {
        super();
    }
}
