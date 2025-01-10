package com.example.yukka.model.roslina.cecha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca cechę z relacjami. Używane głównie przy tworzeniu requestów wyszukiwania, tworzenia i aktualizacji rośliny.
 * 
 * <ul>
 *   <li><strong>etykieta</strong> - etykieta cechy</li>
 *   <li><strong>nazwa</strong> - nazwa cechy</li>
 *   <li><strong>relacja</strong> - relacja cechy</li>
 * </ul>
 */
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CechaWithRelations {
    private String etykieta;
    private String nazwa;
    private String relacja;
}
