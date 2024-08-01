package com.example.yukka.model.roslina;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface RoslinaRepository extends Neo4jRepository<Roslina, Long> {

    
    @Query("MATCH (ros:Roslina{nazwaLacinska: $latinName}) RETURN ros")
    Roslina findRoslinaByLatinName(@Param("latinName") String latinName);


    //@Query("MATCH (p:Roslina)-[r:ma_wlasciwosc]->(g:Gleba) RETURN p, collect(r), collect(g) LIMIT $amount")
    @Query("MATCH path=(p:Roslina)-[r]->(g:Wlasciwosc) RETURN p, collect(nodes(path)), collect(relationships(path)) LIMIT $amount")

   // @Query("MATCH path=(p:Roslina)-[:ma_wlasciwosc]->(g) RETURN p, collect(nodes(path)), collect(relationships(path)) LIMIT $amount")
    //@Query("MATCH (p:Roslina)-[r1:ma_wlasciwosc]->(g:Gleba), (p)-[r2:ma_wlasciwosc]->(w:Wilgotnosc) RETURN p, collect(g), collect(r1), collect(r2), collect(w) LIMIT $amount")
   // @Query("MATCH (p:Roslina)-[r:ma_wlasciwosc]->(wlasciwosc) RETURN p, collect(r), collect(wlasciwosc) LIMIT $amount")
    Collection<Roslina> getSomePlants(@Param("amount") int amount);
    
    @Query("MATCH (r:Roslina)-[:MA_GLEBE]->(g:Gleba) WHERE r.name = $name RETURN r, collect(g) as glebus")
    Roslina findRoslinaWithGleba(@Param("name") String name);

    @Query("UNWIND $roslina AS ros\n" +
           "MERGE (p:Roslina {nazwa: ros.nazwa, nazwaLacinska: ros.nazwaLacinska, opis: ros.opis, obraz: COALESCE(ros.obraz, 'default_plant.jpg'), wysokoscMin: ros.wysokoscMin, wysokoscMax: ros.wysokoscMax})\n" +
        //"MERGE (p:Roslina {nazwa: $name, nazwaLacinska: $latinName, opis: $description, obraz: $imageFilename, wysokoscMin: $heightMin, wysokoscMax: $heightMax}) " +
           "WITH p, ros, $properties AS properties\n" +
           "UNWIND properties AS property\n" +
           //"CALL apoc.create.node([property.etykieta], {nazwa: property.nazwa}) YIELD node AS w\n" +
           //"WITH p, w, property\n" +
           //"MATCH (w:property.etykieta {nazwa: property.nazwa})\n" +
           "MATCH (w:Wlasciwosc {nazwa: property.nazwa})\n" +
           "CALL apoc.create.relationship(p, property.relacja, {}, w) YIELD rel\n" +
           //"MERGE (p)-[:property.relacja]->(w)\n" +
           "WITH p\n" +
           "MATCH path=(p)-[r]->(w:Wlasciwosc)\n" +
           "RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus")
    Roslina addPlantWithProperties(
      //  @Param("name") String name,
      //  @Param("latinName") String latinName,
      //  @Param("description") String description,
      //  @Param("imageFilename") String imageFilename,
      //  @Param("heightMin") Float heightMin,
      //  @Param("heightMax") Float heightMax,
        @Param("roslina") Roslina plant,
        @Param("properties") List<Map<String, String>> properties
    );


    @Query("MERGE (p:Roslina {nazwa: $name, nazwaLacinska: $latinName, opis: $description, obraz: COALESCE($imageFilename, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax})\n" +
        //"MERGE (p:Roslina {nazwa: $name, nazwaLacinska: $latinName, opis: $description, obraz: $imageFilename, wysokoscMin: $heightMin, wysokoscMax: $heightMax}) " +
          "WITH p, $properties AS properties\n" +
          "UNWIND properties AS property\n" +
           //"CALL apoc.create.node([property.etykieta], {nazwa: property.nazwa}) YIELD node AS w\n" +
           //"WITH p, w, property\n" +
           //"MATCH (w:property.etykieta {nazwa: property.nazwa})\n" +
           

          // Próba ostateczna // O dziwo działa AAAAAAAAAAAAAAAAAA
          "WITH p, property, [ property.labels, 'Wlasciwosc' ] AS labels\n" + 
          "CALL apoc.merge.node(labels, {nazwa: property.nazwa}) YIELD node AS w\n" +
          "WITH p, w, property\n" +
          "CALL apoc.merge.relationship(p, property.relacja, {}, {}, w) YIELD rel\n" +
         // "WITH p, w\n" +
          "MERGE (w)-[:MA_ROSLINE]->(p)\n" +
        //  "CALL apoc.merge.relationship(w, 'MA_ROSLINE', {}, {}, p) YIELD rel2\n" +


           // DUŻA PRÓBA // EDit: działa
        //   "CALL apoc.merge.node(['Wlasciwosc'], {nazwa: property.nazwa}) YIELD node AS w\n" +
      //     "WITH p, w, property\n" +
       //    "CALL apoc.merge.relationship(p, property.relacja, {}, {}, w) YIELD rel\n" +


           // To również jakims cudem działa
       //    "MERGE (w:Wlasciwosc {nazwa: property.nazwa})\n" +
       //    "WITH p, w, property\n" +
       //    "CALL apoc.merge.relationship(p, property.relacja, {}, {}, w) YIELD rel\n" +
           
           // To też działa ale tego nienawidzę
          // "MERGE (w:Wlasciwosc {nazwa: property.nazwa})\n" +
         //  "WITH p, w, property\n" +  // Dodanie WITH między MERGE a CALL
         //  "CALL apoc.create.relationship(p, property.relacja, {}, w) YIELD rel\n" +

           // Działające
          // "MATCH (w:Wlasciwosc {nazwa: property.nazwa})\n" +
          // "CALL apoc.create.relationship(p, property.relacja, {}, w) YIELD rel\n" +


           //"MERGE (p)-[:property.relacja]->(w)\n" +
           "WITH p\n" +
           "MATCH path=(p)-[r]->(w:Wlasciwosc)\n" +
           "RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus")
    Roslina addPlantWithProperties2(
        @Param("name") String name,
        @Param("latinName") String latinName,
        @Param("description") String description,
        @Param("imageFilename") String imageFilename,
        @Param("heightMin") Double heightMin,
        @Param("heightMax") Double heightMax,
      //  @Param("roslina") Roslina plant,
        @Param("properties") List<Map<String, String>> properties
    );
    
    @Query("MATCH (p:Roslina{nazwaLacinska: $latinName}) DETACH DELETE p")
    void removeRoslinaByLatinName(@Param("latinName") String latinName);

}
