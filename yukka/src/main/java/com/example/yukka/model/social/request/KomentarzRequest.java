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
public class KomentarzRequest {
    @NotEmpty(message = "Treść komentarza jest wymagana")
    private String opis;

    private String obraz;

    @NotEmpty(message = "Wymagane jest wskazanie co dokładnie jest komentowane")
    private String targetId;

}
