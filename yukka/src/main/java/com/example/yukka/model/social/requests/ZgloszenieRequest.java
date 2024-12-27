package com.example.yukka.model.social.requests;

import com.example.yukka.model.social.models.powiadomienie.TypPowiadomienia;
import com.example.yukka.security.validations.valueOfEnum.ValueOfEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Reprezentuje żądanie zgłoszenia użytkownika.
 * 
 * <ul>
 * <li><strong>zglaszany</strong> - Nazwa zgłaszanego użytkownika. Musi być niepusta i nie dłuższa niż 200 znaków.</li>
 * <li><strong>opis</strong> - Powód zgłoszenia. Musi być niepusty i nie dłuższy niż 200 znaków.</li>
 * <li><strong>typPowiadomienia</strong> - Typ zgłoszenia. Musi być niepusty i zgodny z wartościami wyliczenia TypPowiadomienia.</li>
 * <li><strong>odnosnik</strong> - Odnośnik do zgłoszenia. Musi być niepusty.</li>
 * </ul>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZgloszenieRequest {

    @NotEmpty(message = "Nie podano zgłaszanego użytkownika")
    @Size(max = 200, message = "Nazwa użytkownika jest za długa")
    private String zglaszany;

    @NotEmpty(message = "Podaj powód zgłoszenia")
    @Size(max = 200, message = "Zgłoszenie może mieć co najwyżej do 200 znaków")
    private String opis;

    @NotEmpty(message = "Nie podano typu zgłoszenia")
    @ValueOfEnum(enumClass = TypPowiadomienia.class, message = "Niepoprawny typ zgłoszenia")
    private String typPowiadomienia;

    @NotEmpty(message = "Nie podano odnośnika")
    private String odnosnik;

}
