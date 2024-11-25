package com.example.yukka.model.social.request;

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
@Getter
@Setter
public class ZgloszenieRequest {

    @NotEmpty(message = "Nie podano zgłaszanego użytkownika")
    @Size(max = 200, message = "Nazwa użytkownika jest za długa")
    private String zglaszany;

    @NotEmpty(message = "Podaj powód zgłoszenia")
    @Size(max = 200, message = "Zgłoszenie może mieć co najwyżej do 200 znaków")
    private String opis;

}
