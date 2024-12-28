package com.example.yukka.model.roslina.wlasciwosc;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje odpowiedź zawierającą właściwości rośliny.
 * 
 * <ul>
 *   <li><strong>etykieta</strong> - Etykieta właściwości.</li>
 *   <li><strong>nazwy</strong> - Zbiór nazw związanych z właściwością.</li>
 *  <li><strong>liczbaRoslin</strong> - Liczba roślin związanych z właściwością.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class WlasciwoscResponse {
    private String etykieta;
    private Set<String> nazwy;
}
