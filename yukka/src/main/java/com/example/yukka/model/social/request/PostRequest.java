package com.example.yukka.model.social.request;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PostRequest {

    @NotEmpty(message = "Tytuł jest wymagany")
    private String tytul;

    // Czasem ktoś chce po prostu dać tytuł i tylko tyle
    //@NotEmpty(message = "Nazwa jest wymagana")
    private String opis;
    private String obraz;

}
