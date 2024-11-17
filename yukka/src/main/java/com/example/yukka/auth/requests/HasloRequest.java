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

    @JsonIgnore
    @AssertTrue(message = "Hasło i potwierdzenie hasła muszą być takie same")
    public boolean isHasloMatching() {
        return noweHaslo != null && noweHaslo.equals(nowePowtorzHaslo);
    }

    public HasloRequest() {
        super();
    }
}
