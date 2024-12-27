package com.example.yukka.model.dzialka;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.roslina.Roslina;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca działkę.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator działki.</li>
 * <li><strong>nazwa</strong>: Nazwa działki.</li>
 * <li><strong>numer</strong>: Numer działki.</li>
 * <li><strong>ogrod</strong>: Ogród, do którego należy działka.</li>
 * <li><strong>zasadzoneRosliny</strong>: Lista roślin zasadzonych na działce.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 * <li><strong>getZasadzonaNaByCoordinates</strong>: Zwraca roślinę zasadzoną na działce na podstawie współrzędnych.</li>
 * <li><strong>isRoslinaInDzialka</strong>: Sprawdza, czy dana roślina znajduje się na działce.</li>
 * <li><strong>isRoslinaInDzialka</strong>: Sprawdza, czy roślina z żądania znajduje się na działce.</li>
 * <li><strong>isPozycjeOccupied</strong>: Sprawdza, czy pozycje z żądania są zajęte przez inne rośliny na działce.</li>
 * <li><strong>toString</strong>: Zwraca reprezentację tekstową obiektu działki.</li>
 * </ul>
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node
public class Dzialka {

    @Id @GeneratedValue
    private Long id;

    @Property("nazwa")
    private String nazwa;

    @Property("numer")
    private int numer;

    @JsonIgnore
    @Relationship(type = "MA_DZIALKE", direction = Relationship.Direction.INCOMING)
    private Ogrod ogrod;

    // Zwykla roslina
    @Relationship(type = "ZASADZONA_NA", direction = Relationship.Direction.INCOMING)
    private List<ZasadzonaNaReverse> zasadzoneRosliny;
    

    
    /**
     * Metoda zwraca obiekt ZasadzonaNaReverse na podstawie podanych współrzędnych.
     *
     * @param x współrzędna x do wyszukania
     * @param y współrzędna y do wyszukania
     * @return obiekt ZasadzonaNaReverse znajdujący się na podanych współrzędnych,
     *         lub null, jeśli nie znaleziono obiektu na tych współrzędnych
     */
    public ZasadzonaNaReverse getZasadzonaNaByCoordinates(int x, int y) {
        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            if (zasadzonaNa.getX() == x && zasadzonaNa.getY() == y) {
                return zasadzonaNa;
            }
        }
        return null;
    }

    /**
     * Metoda sprawdza, czy dana roślina znajduje się na działce.
     *
     * @param innaRoslina roślina do sprawdzenia
     * @return true, jeśli roślina znajduje się na działce, w przeciwnym wypadku false
     */
    public boolean isRoslinaInDzialka(ZasadzonaNaReverse innaRoslina) {
        if(innaRoslina == null || innaRoslina.getRoslina() == null) return false;

        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            Roslina roslinaZasadzona = zasadzonaNa.getRoslina();
            if (roslinaZasadzona.getRoslinaId().equals(innaRoslina.getRoslina().getRoslinaId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza, czy dana roślina znajduje się na działce.
     *
     * @param roslina roślina do sprawdzenia
     * @return true, jeśli roślina znajduje się na działce, w przeciwnym wypadku false
     */
    public boolean isRoslinaInDzialka(Roslina roslina) {
        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            Roslina roslinaZasadzona = zasadzonaNa.getRoslina();
            if (roslinaZasadzona.getRoslinaId().equals(roslina.getRoslinaId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza, czy roślina z żądania znajduje się na działce.
     *
     * @param request żądanie z danymi rośliny
     * @return true, jeśli roślina znajduje się na działce, w przeciwnym wypadku false
     */
    public boolean isRoslinaInDzialka(DzialkaRoslinaRequest request) {
        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            Roslina roslinaZasadzona = zasadzonaNa.getRoslina();
            if(roslinaZasadzona == null) continue;
            if ((roslinaZasadzona.getRoslinaId() != null && roslinaZasadzona.getRoslinaId().equals(request.getRoslinaId()))) {
                return true;
            }
        }
        return false;
    }

    // public List<Pozycja> arePozycjeOccupied(BaseDzialkaRequest request) {
    //     List<Pozycja> zajetePozycje = new ArrayList<>();
    //     Set<Pozycja> nowePozycje = request.getPozycje();

    //     ZasadzonaNaReverse zasadzonaRoslina = getZasadzonaNaByCoordinates(request.getX(), request.getY());
    //     if(zasadzonaRoslina == null) return zajetePozycje;
        
    //     for (ZasadzonaNaReverse zasadzona : zasadzoneRosliny) {
    //         if (zasadzona.equalsRoslina(zasadzonaRoslina.getRoslina())) {
    //             continue;
    //         }
    
    //         Set<Pozycja> pozycje = zasadzona.getPozycje();
    //         for (Pozycja pozycja : pozycje) {
    //             if (nowePozycje.contains(pozycja)) {
    //                 zajetePozycje.add(pozycja);
    //             }
    //         }
    //     }
    //     return zajetePozycje;
    // }

    /**
     * Metoda sprawdza, czy pozycje z żądania są zajęte przez inne rośliny na działce.
     * @param request żądanie z danymi rośliny
     * @return lista zajętych pozycji
     */
    public List<Pozycja> arePozycjeOccupied(MoveRoslinaRequest request) {
        List<Pozycja> zajetePozycje = new ArrayList<>();
        Set<Pozycja> nowePozycje = request.getPozycje();
        ZasadzonaNaReverse zasadzonaRoslina = null;
        boolean sameDzialka = true;

        if (request.getNumerDzialkiNowy() != null && !request.getNumerDzialki().equals(request.getNumerDzialkiNowy())) {
            sameDzialka = false;
        }

        if (sameDzialka) {
            zasadzonaRoslina = getZasadzonaNaByCoordinates(request.getX(), request.getY());
            if(zasadzonaRoslina == null) return zajetePozycje;
        }

        for (ZasadzonaNaReverse zasadzona : zasadzoneRosliny) {
            if (sameDzialka && zasadzonaRoslina != null && zasadzona.equalsRoslina(zasadzonaRoslina.getRoslina())) {
                continue;
            }
            Set<Pozycja> pozycje = zasadzona.getPozycje();
            for (Pozycja pozycja : pozycje) {
                if (nowePozycje.contains(pozycja)) {
                    zajetePozycje.add(pozycja);
                }
            }
        }
        return zajetePozycje;
    }

    @Override
    public String toString() {
        
        return "Dzialka{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", numer=" + numer +
                ", ogrod=" + ogrod.getNazwa() +
                ", zasadzoneRosliny=" + zasadzoneRosliny.size() +
                '}';
    }
}
