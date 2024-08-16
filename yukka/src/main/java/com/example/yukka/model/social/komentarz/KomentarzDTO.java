package com.example.yukka.model.social.komentarz;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KomentarzDTO {
    private Long id;
    private String komentarzId;
    private String opis;
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
    private LocalDateTime dataUtworzenia;
    private List<KomentarzDTO> odpowiedzi;
    private String uzytkownikNazwa;
    public byte[] obraz;
    public byte[] avatar;
}
