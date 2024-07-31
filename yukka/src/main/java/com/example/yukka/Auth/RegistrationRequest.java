package com.example.yukka.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    //@JsonProperty("nazwa")
    @NotEmpty(message = "name is mandatory")
    private String nazwa;

   // @JsonProperty("email")
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    private String email;

   // @JsonProperty("haslo")
    @NotEmpty(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String haslo;
    public RegistrationRequest() {
        super();
    }
    public RegistrationRequest(String nazwa, String email, String haslo) {
        this.nazwa = nazwa;
        this.email = email;
        this.haslo = haslo;
    }
    @Override
    public String toString() {
        return "RegistrationRequest [nazwa=" + this.nazwa + ", email=" + this.email + ", haslo=" + this.haslo + "]";
    }

}