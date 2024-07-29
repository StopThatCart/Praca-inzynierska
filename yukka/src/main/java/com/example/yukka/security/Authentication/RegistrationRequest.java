package com.example.yukka.security.Authentication;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    
    @NotEmpty(message = "nazwa nie może być pusta!")
    @NotBlank(message = "nazwa nie może być pusta!")
    private String name;
    
    @NotEmpty(message = "email nie może być pusty!")
    @NotBlank(message = "email nie może być pusty!")
    @Email(message="Email nie jest poprawnie sformatowany.")
    private String email;

    @NotEmpty(message = "hasło nie może być puste!")
    @NotBlank(message = "hasło nie może być puste!")
    //@Size(min=5, message="Hasło powinno mieć przynajmniej 5 znaków!")
    private String password;
}
