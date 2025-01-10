package com.example.yukka.model.roslina.cecha;

import java.util.List;
import java.util.Objects;

import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca cechę rośliny.
 * 
 * <ul>
 * <li><strong>labels</strong>: Lista etykiet dynamicznych cechy.</li>
 * <li><strong>id</strong>: Unikalny identyfikator cechy.</li>
 * <li><strong>nazwa</strong>: Nazwa cechy.</li>
 * </ul>
 * 
 * <p>Klasa zawiera również metody do porównywania obiektów oraz generowania hashCode i reprezentacji tekstowej.</p>
 * 
 * @author Autor
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class Cecha {
    @DynamicLabels
    private List<String> labels;
    @JsonIgnore
    @Id @GeneratedValue
    private long id;
    @Property("nazwa")
    private String nazwa;

    public Cecha(List<String> labels, String nazwa) {
        this.labels = labels;
        this.nazwa = nazwa;
    }
    
    public String getLabels() {
        if(labels.isEmpty()) return null;
        return  labels.get(0);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cecha that = (Cecha) o;
        return Objects.equals(labels, that.labels) &&
            Objects.equals(nazwa, that.nazwa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labels, nazwa);
    }

    @Override
    public String toString() {
        return "Cecha [labels=" + labels + ", nazwa=" + nazwa + "]";
    }
}
