package com.example.yukka.auth.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie uwierzytelnienia zawierające dane uwierzytelniające użytkownika.
 * Ta klasa jest używana do przechwytywania adresu e-mail i hasła użytkownika w celu uwierzytelnienia.
 * 
 * <p>
 * Instancja tej klasy musi zawierać niepusty adres e-mail oraz hasło, które ma co najmniej 8 znaków.
 * </p>
 * 
 * <p>
 * Do pól stosowane są następujące ograniczenia:
 * <ul>
 *   <li>{@code email} - Nie może być pusty. Jeśli jest pusty, zostanie wyświetlony komunikat "Podaj nazwę użytkownika albo email".</li>
 *   <li>{@code haslo} - Nie może być puste i musi mieć co najmniej 8 znaków. Jeśli jest puste, zostanie wyświetlony komunikat "Podaj hasło". Jeśli ma mniej niż 8 znaków, zostanie wyświetlony komunikat "Hasło powinno mieć co najmniej 8 znaków".</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Ta klasa używa adnotacji Lombok do generowania kodu szablonowego, takiego jak gettery, settery, metody wzorca budowniczego oraz metodę toString.
 * </p>
 * 
 * <p>
 * Użyte adnotacje:
 * <ul>
 *   <li>{@code @Getter} - Generuje metody getter dla wszystkich pól.</li>
 *   <li>{@code @Setter} - Generuje metody setter dla wszystkich pól.</li>
 *   <li>{@code @Builder} - Implementuje wzorzec budowniczego dla tej klasy.</li>
 *   <li>{@code @ToString} - Generuje metodę toString dla tej klasy.</li>
 *   <li>{@code @NotEmpty} - Zapewnia, że adnotowane pole nie jest puste.</li>
 *   <li>{@code @Size} - Zapewnia, że adnotowane pole spełnia określone ograniczenia dotyczące rozmiaru.</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
@Builder
@ToString
public class AuthRequest {
    @NotEmpty(message = "Podaj nazwę użytkownika albo email") 
    private String email;

    @NotEmpty(message = "Podaj hasło")
    @Size(min = 8, message = "Hasło powinno mieć co najmniej 8 znaków")
    private String haslo;
}
