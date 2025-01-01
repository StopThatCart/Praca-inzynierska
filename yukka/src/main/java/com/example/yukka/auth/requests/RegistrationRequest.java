package com.example.yukka.auth.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * RegistrationRequest jest obiektem transferu danych używanym do rejestracji użytkownika.
 * Zawiera następujące pola:
 * 
 * <ul>
 *   <li>{@code nazwa} - Nazwa użytkownika, która musi mieć od 3 do 100 znaków i może zawierać tylko znaki alfanumeryczne oraz polskie znaki diakrytyczne.</li>
 *   <li>{@code email} - Adres email użytkownika, który musi być w poprawnym formacie email.</li>
 *   <li>{@code haslo} - Hasło użytkownika, które musi mieć co najmniej 8 znaków.</li>
 *   <li>{@code powtorzHaslo} - Potwierdzenie hasła użytkownika, które musi być takie samo jak hasło.</li>
 * </ul>
 * 
 * Klasa zawiera adnotacje walidacyjne, aby upewnić się, że pola spełniają wymagane ograniczenia.
 * Zawiera również metodę do sprawdzania, czy hasło i potwierdzenie hasła są takie same.
 * 
 * Użyte adnotacje:
 * <ul>
 *   <li>{@code @ToString} - Generuje metodę toString.</li>
 *   <li>{@code @Getter} - Generuje metody getter dla wszystkich pól.</li>
 *   <li>{@code @Setter} - Generuje metody setter dla wszystkich pól.</li>
 *   <li>{@code @Builder} - Zapewnia wzorzec budowniczego do tworzenia obiektów.</li>
 *   <li>{@code @AllArgsConstructor} - Generuje konstruktor z wszystkimi polami jako parametrami.</li>
 *   <li>{@code @Pattern} - Zapewnia, że nazwa użytkownika zawiera tylko dozwolone znaki.</li>
 *   <li>{@code @Size} - Określa ograniczenia rozmiaru dla pól.</li>
 *   <li>{@code @NotEmpty} - Zapewnia, że pola nie są puste.</li>
 *   <li>{@code @Email} - Waliduje format email.</li>
 *   <li>{@code @JsonIgnore} - Ignoruje metodę podczas serializacji JSON.</li>
 *   <li>{@code @AssertTrue} - Waliduje, że hasło i potwierdzenie hasła są takie same.</li>
 * </ul>
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    //@JsonProperty("nazwa")
    @Pattern(regexp = "^[a-zA-Z0-9_ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+( [a-zA-Z0-9_ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+)*$", 
    message = "Nazwa może zawierać tylko litery, cyfry, pojedyńcze spacje i polskie znaki")
    @Size(min = 3, max = 100, message = "Nazwa powinna mieć od 3 do 100 znaków")
    @NotEmpty(message = "nazwa jest wymagana")
    private String nazwa;

   // @JsonProperty("email")
    @Email(message = "Niepoprawny format adresu email")
    @NotEmpty(message = "Email jest wymagany")
    private String email;

   // @JsonProperty("haslo")
    @NotEmpty(message = "haslo jest wymagane")
    @Size(min = 8, message = "Hasło powinno mieć co najmniej 8 znaków")
    private String haslo;

    @NotEmpty(message = "Hasła nie zgadzają się")
    @Size(min = 8, message = "Nowe hasło powinno mieć co najmniej 8 znaków")
    private String powtorzHaslo;

    
    /** 
     * @return boolean
     */
    @JsonIgnore
    @AssertTrue(message = "Hasło i potwierdzenie hasła muszą być takie same")
    public boolean isHasloMatching() {
        return haslo != null && haslo.equals(powtorzHaslo);
    }

    public RegistrationRequest() {
        super();
    }

}