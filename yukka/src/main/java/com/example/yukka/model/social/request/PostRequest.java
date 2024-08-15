package com.example.yukka.model.social.request;
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
public class PostRequest {

    @NotEmpty(message = "Tytuł jest wymagany")
    @Size(max = 100, message = "Tytuł nie może zawierać więcej niż 100 znaków")
    private String tytul;

    // Czasem ktoś chce po prostu dać tytuł i tylko tyle
    //@NotEmpty(message = "Nazwa jest wymagana")
    @Size(max = 3000, message = "Opis nie może zawierać więcej niż 3000 znaków")
    private String opis;
    @Size(max = 200, message = "Nazwa pliku jest za długa")
    private String obraz;

}
