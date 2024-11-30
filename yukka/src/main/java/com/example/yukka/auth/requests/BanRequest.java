package com.example.yukka.auth.requests;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
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
//@NoArgsConstructor
//@AllArgsConstructor
public class BanRequest {
    @NotEmpty(message = "Podaj nazwę użytkownika") 
    String nazwa;

    @NotEmpty(message = "Podaj powód bana")
    String powod;

    @NotNull(message = "Podaj czy użytkownik ma być zbanowany")
    Boolean ban;

    @NotNull(message = "Podaj datę zakończenia bana")
    LocalDate banDo;

    @JsonIgnore
    @AssertTrue(message = "Data zakończenia bana nie może być wcześniejsza niż obecna data")
    public boolean isBanDataOkay() {
        return banDo != null && banDo.isAfter(LocalDate.now());
    }
}
