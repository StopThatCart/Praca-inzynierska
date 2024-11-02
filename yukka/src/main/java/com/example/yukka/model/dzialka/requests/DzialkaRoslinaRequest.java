package com.example.yukka.model.dzialka.requests;


import com.example.yukka.validations.pozycje.ValidPozycje;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder(toBuilder = true)

@AllArgsConstructor
@NoArgsConstructor
@ToString

//@ValidRoslinaIdAlboNazwaLacinska
@ValidPozycje
public class DzialkaRoslinaRequest extends BaseDzialkaRequest {
    private String roslinaId;
    private String nazwaLacinska;

    @NotEmpty(message = "Kolor jest wymagany")
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Kolor musi być w formacie hex")
    private String kolor;


    // Null jak nie dopina się żadnego obrazu
    private String obraz;

}
