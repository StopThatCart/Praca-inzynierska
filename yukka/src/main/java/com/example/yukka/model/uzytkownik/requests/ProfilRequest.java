package com.example.yukka.model.uzytkownik.requests;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Reprezentuje żądanie profilu użytkownika.
 * <ul>
 *   <li><strong>imie</strong> - Imię użytkownika. Musi zawierać od 1 do 100 znaków.</li>
 *   <li><strong>miasto</strong> - Miasto użytkownika. Musi zawierać od 1 do 100 znaków.</li>
 *   <li><strong>miejsceZamieszkania</strong> - Miejsce zamieszkania użytkownika. Musi zawierać od 1 do 100 znaków.</li>
 *   <li><strong>opis</strong> - Opis użytkownika. Musi zawierać od 1 do 500 znaków.</li>
 * </ul>
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfilRequest {

    @Size(min = 1, max = 100, message = "Imię musi zawierać od 1 do 100 znaków")
    private String imie;
    @Size(min = 1, max = 100, message = "Miasto musi zawierać od 1 do 100 znaków")
    private String miasto;
    @Size(min = 1, max = 100, message = "Miejsce zamieszkania musi zawierać od 1 do 100 znaków")
    private String miejsceZamieszkania;

    @Size(min = 1, max = 500, message = "Opis musi zawierać od 1 do 500 znaków")
    private String opis;
}