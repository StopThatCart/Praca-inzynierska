package com.example.yukka.model.dzialka;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.PostLoad;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.example.yukka.model.roslina.Roslina;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca relację zasadzoną na odwrocie.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator relacji.</li>
 *   <li><strong>x</strong>: Współrzędna X.</li>
 *   <li><strong>y</strong>: Współrzędna Y.</li>
 *   <li><strong>kolor</strong>: Kolor rośliny.</li>
 *   <li><strong>tekstura</strong>: Tekstura rośliny.</li>
 *   <li><strong>wyswietlanie</strong>: Sposób wyświetlania rośliny.</li>
 *   <li><strong>notatka</strong>: Notatka dotycząca rośliny.</li>
 *   <li><strong>obraz</strong>: Obraz rośliny.</li>
 *   <li><strong>tabX</strong>: Tablica współrzędnych X.</li>
 *   <li><strong>tabY</strong>: Tablica współrzędnych Y.</li>
 *   <li><strong>roslina</strong>: Obiekt rośliny powiązany z relacją.</li>
 *   <li><strong>pozycje</strong>: Zbiór pozycji rośliny.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 *   <li><strong>initPozycje</strong>: Inicjalizuje zbiór pozycji na podstawie tablic współrzędnych.</li>
 *   <li><strong>equalsRoslina(Roslina roslina)</strong>: Sprawdza, czy podana roślina jest równa roślinie w relacji.</li>
 *   <li><strong>equalsRoslina(String roslinaUUID)</strong>: Sprawdza, czy podane ID rośliny jest równe ID rośliny w relacji.</li>
 *   <li><strong>equalsRoslina(ZasadzonaNaReverse zasadzinaNaReverse)</strong>: Sprawdza, czy roślina w podanej relacji jest równa roślinie w tej relacji.</li>
 * </ul>
 */
@RelationshipProperties
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZasadzonaNaReverse {
    @RelationshipId 
    @GeneratedValue
    private Long id;

    @Property("x")
    private int x;

    @Property("y")
    private int y;

    @Property("kolor")
    private String kolor;

    @Property("tekstura")
    private String tekstura;

    @Property("wyswietlanie")
    private String wyswietlanie;

    @Property("notatka")
    private String notatka;

    @Property("obraz")
    private String obraz;

    @Property("tabX")
    private int[] tabX;

    @Property("tabY")
    private int[] tabY;

    
    @TargetNode
    private Roslina roslina;


    @JsonIgnore
    private Set<Pozycja> pozycje;

    @PostLoad
    private void initPozycje() {
        pozycje = new HashSet<Pozycja>();
        for (int i = 0; i < tabX.length; i++) {
            pozycje.add(new Pozycja(tabX[i], tabY[i]));
        }
    }


    @JsonIgnore
    public boolean equalsRoslina(Roslina roslina) {
        return this.roslina.getUuid().equals(roslina.getUuid());
    }

    @JsonIgnore
    public boolean equalsRoslina(String roslinaUUID) {
        Roslina roslina = this.roslina;
        if(roslina == null) {
            return false;
        } 

        return this.roslina.getUuid().equals(roslinaUUID);
    }

    @JsonIgnore
    public boolean equalsRoslina(ZasadzonaNaReverse zasadzinaNaReverse) {
        Roslina roslina = this.roslina;
        Roslina roslinaWhatever = zasadzinaNaReverse.getRoslina();
        if(roslina == null || roslinaWhatever == null) {
            return false;
        } 
        return this.roslina.getUuid().equals(roslinaWhatever.getUuid());
    }

}
