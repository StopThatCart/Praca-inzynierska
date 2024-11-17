package com.example.yukka.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    @NotEmpty(message = "Hasło nie może być puste")
    @Size(min = 8, message = "Hasło powinno mieć co najmniej 8 znaków")
    private String haslo;

    @Email(message = "Niepoprawny format adresu email")
    @NotEmpty(message = "Nowy email jest wymagany")
    private String nowyEmail;
}
