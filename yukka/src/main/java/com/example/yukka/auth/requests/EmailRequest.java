package com.example.yukka.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie aktualizacji adresu email.
 * Ta klasa zawiera adnotacje walidacyjne, aby upewnić się, że podane
 * hasło i nowy adres email spełniają wymagane kryteria.
 * 
 * Pola:
 * <ul>
 *   <li>{@code haslo} - Hasło związane z kontem email. Nie może być puste
 *   i musi mieć co najmniej 8 znaków.</li>
 *   <li>{@code nowyEmail} - Nowy adres email do ustawienia. Musi być w poprawnym formacie
 *   email i nie może być pusty.</li>
 * </ul>
 * 
 * Adnotacje:
 * <ul>
 *   <li>{@code @ToString} - Generuje metodę toString.</li>
 *   <li>{@code @Getter} - Generuje metody getter dla wszystkich pól.</li>
 *   <li>{@code @Setter} - Generuje metody setter dla wszystkich pól.</li>
 *   <li>{@code @Builder} - Umożliwia wzorzec budowniczego do tworzenia obiektów.</li>
 *   <li>{@code @AllArgsConstructor} - Generuje konstruktor z wszystkimi polami jako parametrami.</li>
 *   <li>{@code @NoArgsConstructor} - Generuje konstruktor bezargumentowy.</li>
 *   <li>{@code @NotEmpty} - Zapewnia, że pole nie jest puste.</li>
 *   <li>{@code @Size} - Zapewnia, że pole spełnia określone ograniczenia rozmiaru.</li>
 *   <li>{@code @Email} - Zapewnia, że pole jest w poprawnym formacie email.</li>
 * </ul>
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    @NotEmpty(message = "Hasło nie może być puste")
    @Size(min = 8, max = 100, message = "Hasło powinno mieć od 8 do 100 znaków")
    private String haslo;

    @Email(message = "Niepoprawny format adresu email")
    @NotEmpty(message = "Nowy email jest wymagany")
    private String nowyEmail;
}
