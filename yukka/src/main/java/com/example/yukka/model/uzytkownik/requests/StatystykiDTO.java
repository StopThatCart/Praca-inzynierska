package com.example.yukka.model.uzytkownik.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StatystykiDTO {
    // private ArrayList<Long> posty;
    // private ArrayList<Long> komentarze;
    // private ArrayList<Long> rosliny;
    private Integer posty;
    private Integer komentarze;
    private Integer rosliny;

   // private Integer komentarzeOceny;
    private Integer komentarzeOcenyPozytywne;
    private Integer komentarzeOcenyNegatywne;
   // private Integer postyOceny;
    private Integer postyOcenyPozytywne;
    private Integer postyOcenyNegatywne;

    // @JsonIgnore
    // public static StatystykiDTO fromMap(Map<String, Object> map) {
    //     return StatystykiDTO.builder()
    //             .posty(((Number) map.get("posty")).longValue())
    //             .komentarze(((Number) map.get("komentarze")).longValue())
    //             .rosliny(((Number) map.get("rosliny")).longValue())
    //             .build();
    // }
}
