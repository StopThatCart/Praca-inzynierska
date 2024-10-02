package com.example.yukka.auth;

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

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    //@JsonProperty("nazwa")
    @Pattern(regexp = "^[a-zA-Z0-9_ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]*$", message = "Nazwa nie może zawierać znaków specjalnych")
    @Size(min = 3, message = "Nazwa powinna mieć co najmniej 3 znaki")
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
    private String powtorzHaslo;

    @JsonIgnore
    @AssertTrue(message = "Hasło i potwierdzenie hasła muszą być takie same")
    public boolean isHasloMatching() {
        return haslo != null && haslo.equals(powtorzHaslo);
    }

    public RegistrationRequest() {
        super();
    }

}