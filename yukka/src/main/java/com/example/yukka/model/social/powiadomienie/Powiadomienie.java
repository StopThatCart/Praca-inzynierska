package com.example.yukka.model.social.powiadomienie;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Powiadomienie {
    @Id @GeneratedValue
    private Long id;

    @Property(name = "typ")
    private String typ;

    @Property(name = "odnosnik")
    private String odnosnik;

    @Property(name = "tytul")
    private String tytul;

    @Property(name = "uzytkownikNazwa")
    private String uzytkownikNazwa;

    @Property(name = "opis")
    private String opis;

    @Property(name = "avatar")
    private String avatar;

    @Property(name = "nazwyRoslin")
    private List<String> nazwyRoslin;

    @Property(name = "iloscPolubien")
    private int iloscPolubien;

    @Property(name = "data")
    private LocalDateTime data;

    @CreatedDate
    @Builder.Default
    @Property(name = "dataUtworzenia")
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

    @Relationship(type = "POWIADAMIA", direction = Relationship.Direction.OUTGOING)
    private Uzytkownik uzytkownik;

    @Override
    public String toString() {
        return "Powiadomienie{" +
                "id=" + id +
                ", typ='" + typ + '\'' +
                ", odnosnik='" + odnosnik + '\'' +
                ", tytul='" + tytul + '\'' +
                ", opis='" + opis + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nazwyRoslin=" + nazwyRoslin +
                ", iloscPolubien=" + iloscPolubien +
                ", dataUtworzenia=" + dataUtworzenia +
                ", uzytkownik=" + uzytkownik +
                '}';
    }
}
