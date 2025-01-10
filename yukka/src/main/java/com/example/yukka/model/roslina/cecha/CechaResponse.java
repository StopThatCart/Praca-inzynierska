package com.example.yukka.model.roslina.cecha;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje odpowiedź zawierającą cechy rośliny.
 * 
 * <ul>
 *   <li><strong>etykieta</strong> - Etykieta cechy.</li>
 *   <li><strong>nazwy</strong> - Zbiór nazw związanych z cechą.</li>
 *  <li><strong>liczbaRoslin</strong> - Liczba roślin związanych z cechą.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class CechaResponse {
    private String etykieta;
    private Set<String> nazwy;
}
