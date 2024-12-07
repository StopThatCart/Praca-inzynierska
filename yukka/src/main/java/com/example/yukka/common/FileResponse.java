package com.example.yukka.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Obiekt odpowiedzi, który zawiera zawartość pliku.
 * Ta klasa jest używana do enkapsulacji zawartości pliku w tablicy bajtów.
 * 
 * <p>Użyte adnotacje:</p>
 * <ul>
 *   <li>{@code @Getter} - Generuje metody getter dla wszystkich pól.</li>
 *   <li>{@code @Setter} - Generuje metody setter dla wszystkich pól.</li>
 *   <li>{@code @Builder} - Zapewnia wzorzec budowniczego do tworzenia obiektów.</li>
 *   <li>{@code @AllArgsConstructor} - Generuje konstruktor z jednym parametrem dla każdego pola.</li>
 *   <li>{@code @NoArgsConstructor} - Generuje konstruktor bezargumentowy.</li>
 * </ul>
 * 
 * <p>Pola:</p>
 * <ul>
 *   <li>{@code content} - Tablica bajtów reprezentująca zawartość pliku.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private byte[] content;
}
