package com.example.yukka.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Obiekt odpowiedzi zawierający token uwierzytelniający.
 * Ta klasa jest używana do enkapsulacji tokenu zwracanego po pomyślnej autoryzacji.
 * 
 * <p>
 * Klasa {@code AuthenticationResponse} jest oznaczona adnotacjami Lombok:
 * <ul>
 *   <li>{@code @Getter} - Generuje metody getter dla wszystkich pól.</li>
 *   <li>{@code @Setter} - Generuje metody setter dla wszystkich pól.</li>
 *   <li>{@code @Builder} - Implementuje wzorzec buildera do tworzenia obiektów.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Pola:
 * <ul>
 *   <li>{@code token} - Token uwierzytelniający jako {@code String}.</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
}
