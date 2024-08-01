package com.example.yukka.model.roslina;
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
public class RoslinaRequest {

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwa;

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwaLacinska;

    // To nie ma prawa działać jakby co
   // @Default("Ta roślina nie posiada opisu")
    @NotEmpty(message = "Opis jest wymagany.")
    private String opis;

    @NotEmpty(message = "wysokość musi być zdefiniowana")
    private float wysokoscMin;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String haslo;
}
