package com.example.yukka.model.social.komentarz;
import java.time.LocalDateTime;
import java.util.List;

import com.example.yukka.model.social.post.PostDTO;

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
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
    private PostDTO post;
    private KomentarzDTO komentarzOd;
    private List<KomentarzDTO> odpowiedzi;
    private String uzytkownikNazwa;
    private byte[] obraz;
    private LocalDateTime dataUtworzenia;
    
}
