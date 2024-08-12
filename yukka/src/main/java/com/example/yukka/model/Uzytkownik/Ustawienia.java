package com.example.yukka.model.uzytkownik;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Ustawienia {
    @Id @GeneratedValue
    private Long id;

    @Property("statystyki_profilu")
    @Builder.Default
    private Boolean statystykiProfilu = true;

    @Property("galeria_pokaz")
    @Builder.Default
    private Boolean galeriaPokaz = true;

    @Property("galeria_ocena_komentarze")
    @Builder.Default
    private Boolean galeriaOcenaKomentarze = true;

    @Property("ogrod_pokaz")
    @Builder.Default
    private Boolean ogrodPokaz = true;

    @Property("ogrod_ocena_komentarze")
    @Builder.Default
    private Boolean ogrodOcenaKomentarze = true;

    @Property("powiadomienia_komentarze_odpowiedz")
    @Builder.Default
    private Boolean powiadomieniaKomentarzeOdpowiedz = true;

    @Property("powiadomienia_komentarze_ogrod")
    @Builder.Default
    private Boolean powiadomieniaKomentarzeOgrod = true;

    @Property("powiadomienia_komentarze_galeria")
    @Builder.Default
    private Boolean powiadomieniaKomentarzeGaleria = true;

    @Property("powiadomienia_ogrod_podlewanie")
    @Builder.Default
    private Boolean powiadomieniaOgrodPodlewanie = true;

    @Property("powiadomienia_ogrod_kwitnienie")
    @Builder.Default
    private Boolean powiadomieniaOgrodKwitnienie = true;

    @Property("powiadomienia_ogrod_owocowanie")
    @Builder.Default
    private Boolean powiadomieniaOgrodOwocowanie = true;

   // @Relationship(type = "MA_USTAWIENIA", direction = Relationship.Direction.INCOMING)
   // private List<Uzytkownik> uzytkownik;

}
