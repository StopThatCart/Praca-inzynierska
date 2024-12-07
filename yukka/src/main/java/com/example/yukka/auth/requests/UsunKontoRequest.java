package com.example.yukka.auth.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * UsunKontoRequest jest obiektem transferu danych używanym do obsługi żądań usunięcia konta.
 * Zawiera hasło użytkownika, które musi być niepuste i mieć co najmniej 8 znaków.
 * 
 * Adnotacje:
 * <ul>
 *   <li>{@code @Builder} - Generuje wzorzec budowniczego dla klasy.</li>
 *   <li>{@code @NoArgsConstructor} - Generuje konstruktor bezargumentowy.</li>
 *   <li>{@code @AllArgsConstructor} - Generuje konstruktor z 1 parametrem dla każdego pola w klasie.</li>
 *   <li>{@code @Setter} - Generuje metody ustawiające dla wszystkich pól.</li>
 *   <li>{@code @Getter} - Generuje metody pobierające dla wszystkich pól.</li>
 * </ul>
 * 
 * Pola:
 * <ul>
 *   <li>{@code haslo} - Hasło użytkownika. Musi być niepuste i mieć co najmniej 8 znaków.
 *     <ul>
 *       <li>{@code @NotEmpty} - Zapewnia, że hasło nie jest puste.</li>
 *       <li>{@code @Size} - Zapewnia, że hasło ma co najmniej 8 znaków.</li>
 *     </ul>
 *   </li>
 * </ul>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UsunKontoRequest {
    @NotEmpty(message = "Hasło nie może być puste")
    @Size(min = 8, message = "Hasło powinno mieć co najmniej 8 znaków")
    private String haslo;
}
