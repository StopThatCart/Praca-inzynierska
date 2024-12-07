package com.example.yukka.model.ogrod;

import java.util.List;

import com.example.yukka.model.dzialka.DzialkaResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź zawierającą informacje o ogrodzie.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator ogrodu.</li>
 *   <li><strong>nazwa</strong>: Nazwa ogrodu.</li>
 *   <li><strong>wlascicielNazwa</strong>: Nazwa właściciela ogrodu.</li>
 *   <li><strong>dzialki</strong>: Lista działek należących do ogrodu.</li>
 * </ul>
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OgrodResponse {
    private Long id;
    private String nazwa;
    private String wlascicielNazwa;

    private List<DzialkaResponse> dzialki;
    
}
