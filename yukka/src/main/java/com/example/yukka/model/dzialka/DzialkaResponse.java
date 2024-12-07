package com.example.yukka.model.dzialka;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź zawierającą informacje o działce.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator działki</li>
 * <li><strong>nazwa</strong>: Nazwa działki</li>
 * <li><strong>numer</strong>: Numer działki</li>
 * <li><strong>wlascicielNazwa</strong>: Nazwa właściciela działki</li>
 * <li><strong>zasadzoneRosliny</strong>: Lista zasadzonych roślin na działce</li>
 * <li><strong>liczbaRoslin</strong>: Liczba roślin na działce</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DzialkaResponse {
    private Long id;
    private String nazwa;
    private int numer;
    private String wlascicielNazwa;
    private List<ZasadzonaRoslinaResponse> zasadzoneRosliny;
    private int liczbaRoslin;
}
