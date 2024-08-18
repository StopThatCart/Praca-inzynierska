package com.example.yukka.model.social.post;
import java.time.LocalDateTime;
import java.util.List;

import com.example.yukka.model.social.komentarz.KomentarzResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String postId;
    private String tytul;
    private String opis;
     @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer ocenyLubi;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer ocenyNieLubi;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer liczbaKomentarzy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KomentarzResponse> komentarze;
    private String uzytkownik;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] obraz;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] avatar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dataUtworzenia;
    
}
