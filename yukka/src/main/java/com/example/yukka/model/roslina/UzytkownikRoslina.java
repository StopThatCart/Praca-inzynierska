package com.example.yukka.model.roslina;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.ZasadzonaNa;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder    
public class UzytkownikRoslina extends Roslina {
    @Property("roslinaId")
    private String roslinaId;


    @Relationship(type = "STWORZONA_PRZEZ", direction = Relationship.Direction.OUTGOING)
    private Uzytkownik uzytkownik;
}
