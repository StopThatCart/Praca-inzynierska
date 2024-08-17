package com.example.yukka.model.social.komentarz;
import java.time.LocalDateTime;
import java.util.List;

import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;

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
public class KomentarzResponse {
    private Long id;
    private String komentarzId;
    private String opis;
    private boolean edytowany;
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
    private LocalDateTime dataUtworzenia;
    private PostResponse post;
    private RozmowaPrywatnaResponse rozmowaPrywatna; 
    private KomentarzResponse odpowiadaKomentarzowi;
    private List<KomentarzResponse> odpowiedzi;
    private String uzytkownikNazwa;
    public byte[] obraz;
    public byte[] avatar;
}
