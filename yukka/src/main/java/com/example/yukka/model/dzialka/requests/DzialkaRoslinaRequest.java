package com.example.yukka.model.dzialka.requests;


import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.enums.Wyswietlanie;
import com.example.yukka.validations.valueOfEnum.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
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
//@ValidPozycje
//@YetAnotherConstraint
public class DzialkaRoslinaRequest extends BaseDzialkaRequest {
    private String roslinaId;
    private String nazwaLacinska;

    @NotEmpty(message = "Kolor jest wymagany")
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Kolor musi być w formacie hex")
    private String kolor;

    @NotEmpty(message = "Wybierz sposób wyświetlania")
    @ValueOfEnum(enumClass = Wyswietlanie.class, message = "Niepoprawny sposób wyświetlania")
    private String wyswietlanie;

    private String tekstura;
    // Null jak nie dopina się żadnego obrazu
    private String obraz;

    @JsonIgnore
    public boolean isValidDzialkaRoslinaRequest() {
        if (this.getX() == null || this.getY() == null) {
            return false;
        }
        Pozycja pos = Pozycja.builder().x(this.getX()).y(this.getY()).build();

        return this.getPozycje().contains(pos);
    }

}
