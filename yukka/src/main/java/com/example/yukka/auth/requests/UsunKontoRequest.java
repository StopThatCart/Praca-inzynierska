package com.example.yukka.auth.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
