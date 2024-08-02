    package com.example.yukka.model.roslina.wlasciwosc;

    import java.util.List;
    import java.util.Objects;

    import org.springframework.data.neo4j.core.schema.DynamicLabels;
    import org.springframework.data.neo4j.core.schema.GeneratedValue;
    import org.springframework.data.neo4j.core.schema.Id;
    import org.springframework.data.neo4j.core.schema.Node;
    import org.springframework.data.neo4j.core.schema.Property;
    import org.springframework.data.neo4j.core.schema.Relationship;

    import com.example.yukka.model.roslina.Roslina;

    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Node
    @Getter
    @Setter
    @NoArgsConstructor
    public class Wlasciwosc {

        @DynamicLabels
        private List<String> labels;

        @Id @GeneratedValue
        private long id;
        @Property("nazwa")
        private String nazwa;

        @Relationship(type="MA_ROSLINE", direction=Relationship.Direction.OUTGOING)
        private List<Roslina> plants;

        // Na razie nieużywane
        /* 
        public List<Roslina> getPlants() {
            return plants;
        }
        */

        public Wlasciwosc(List<String> labels, String nazwa) {
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
            Wlasciwosc that = (Wlasciwosc) o;
            return Objects.equals(labels, that.labels) &&
                // Objects.equals(id, that.id) &&
                Objects.equals(nazwa, that.nazwa);
        }

        @Override
        public int hashCode() {
            return Objects.hash(labels, nazwa);
        }



        @Override
        public String toString() {
            return "Wlasciwosc [labels=" + labels + ", id=" + id + ", nazwa=" + nazwa + ", plants=" + plants + "]";
        }

        


    }
