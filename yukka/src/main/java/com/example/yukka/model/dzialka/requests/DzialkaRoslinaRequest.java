package com.example.yukka.model.dzialka.requests;


import jakarta.validation.constraints.NotEmpty;
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


    private String tekstura;
    // Null jak nie dopina się żadnego obrazu
    private String obraz;

}
