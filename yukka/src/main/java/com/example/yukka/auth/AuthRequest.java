package com.example.yukka.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AuthRequest {
   // @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Podaj nazwę użytkownika albo email") 
  //  @NotNull(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Podaj hasło")
   // @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Hasło powinno mieć co najmniej 8 znaków")
    private String haslo;
}
