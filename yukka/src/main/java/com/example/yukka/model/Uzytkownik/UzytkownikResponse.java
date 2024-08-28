package com.example.yukka.model.uzytkownik;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UzytkownikResponse {
private Long id;
    private String uzytId;
    private List<String> labels;
    private String nazwa;
    private String email;
    private byte[] avatar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int komentarzeOcenyPozytywne;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int komentarzeOcenyNegatywne;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int postyOcenyPozytywne;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int postyOcenyNegatywne;
    private LocalDateTime dataUtworzenia;
    private boolean ban;
}
