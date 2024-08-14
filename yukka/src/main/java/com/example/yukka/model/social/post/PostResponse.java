package com.example.yukka.model.social.post;
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
public class PostResponse {
    private Long id;
    private String postId;
    private String tytul;
    private String opis;
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
    private Integer liczbaKomentarzy;
    private String uzytkownik;
    private byte[] obraz;
}
