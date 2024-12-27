package com.example.yukka.model.social.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje żądanie specjalnego powiadomienia.
 * <ul>
 *   <li><strong>opis</strong> - Opis powiadomienia. Musi być niepusty i może mieć maksymalnie 1000 znaków.</li>
 * </ul>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpecjalnePowiadomienieRequest {
    @NotEmpty(message = "Podaj opis powiadomienia")
    @Size(max = 1000, message = "Powiadomienie może mieć co najwyżej do 1000 znaków")
    private String opis;
}
