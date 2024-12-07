package com.example.yukka.model.roslina.wlasciwosc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca właściwość z relacjami. Używane głównie przy tworzeniu requestów wyszukiwania, tworzenia i aktualizacji rośliny.
 * 
 * <ul>
 *   <li><strong>etykieta</strong> - etykieta właściwości</li>
 *   <li><strong>nazwa</strong> - nazwa właściwości</li>
 *   <li><strong>relacja</strong> - relacja właściwości</li>
 * </ul>
 */
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WlasciwoscWithRelations {
    private String etykieta;
    private String nazwa;
    private String relacja;
}
