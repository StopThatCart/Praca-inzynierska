package com.example.yukka.model.social.models;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Oceniany {

    @Relationship(type = "OCENIL", direction = Relationship.Direction.INCOMING)
    private List<OcenilReverse> ocenil;

    
    /** 
     * @return int
     */
    public int getOcenyLubiButGood() {
        return (int) ocenil.stream().filter(OcenilReverse::isLubi).count();
    }

    public int getOcenyNieLubiButGood() {
        return (int) ocenil.stream().filter(ocenil -> !ocenil.isLubi()).count();
    }
}
