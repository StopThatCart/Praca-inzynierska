package com.example.yukka.model.dzialka;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.roslina.Roslina;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
     * @param x
     * @param y
     * @return ZasadzonaNaReverse
     */
    // Customowa roslina


    public ZasadzonaNaReverse getZasadzonaNaByCoordinates(int x, int y) {
        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            if (zasadzonaNa.getX() == x && zasadzonaNa.getY() == y) {
                return zasadzonaNa;
            }
        }
        return null;
    }

    public boolean isRoslinaInDzialka(Roslina roslina) {
        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            Roslina roslinaZasadzona = zasadzonaNa.getRoslina();
            if (roslinaZasadzona.getRoslinaId().equals(roslina.getRoslinaId())) {
                return true;
            }
        }
        return false;
    }

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

    public List<Pozycja> isPozycjeOccupied(BaseDzialkaRequest request) {
        List<ZasadzonaNaReverse> zasadzoneRosliny = this.getZasadzoneRosliny();
        ZasadzonaNaReverse zasadzonaRoslina = getZasadzonaNaByCoordinates(request.getX(), request.getY());
        List<Pozycja> zajetePozycje = new ArrayList<>();

        if(zasadzonaRoslina == null) return zajetePozycje;
        
        for (ZasadzonaNaReverse zasadzona : zasadzoneRosliny) {
            if (zasadzona.equalsRoslina(zasadzonaRoslina.getRoslina())) {
                continue;
            }
    
            Set<Pozycja> pozycje = zasadzona.getPozycje();
            Set<Pozycja> nowePozycje = request.getPozycje();
    
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
