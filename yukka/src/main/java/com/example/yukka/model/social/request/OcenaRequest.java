package com.example.yukka.model.social.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OcenaRequest {
    @NotEmpty(message = "Aby coś ocenić, należy to najpierw wskazać")
    private String ocenialny_id;

    @NotNull(message = "Ocena musi być")
    private boolean lubi;

}
