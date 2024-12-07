package com.example.yukka.handler;


import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca odpowiedź na wyjątek.
 * 
 * <p>Używana do przekazywania informacji o błędach biznesowych, walidacyjnych oraz innych błędach.</p>
 * 
 * <p>Adnotacje:</p>
 * <ul>
 *   <li>{@code @Getter} - generuje metody getter dla wszystkich pól</li>
 *   <li>{@code @Setter} - generuje metody setter dla wszystkich pól</li>
 *   <li>{@code @Builder} - umożliwia budowanie obiektów tej klasy za pomocą wzorca Builder</li>
 *   <li>{@code @AllArgsConstructor} - generuje konstruktor z wszystkimi argumentami</li>
 *   <li>{@code @NoArgsConstructor} - generuje konstruktor bezargumentowy</li>
 *   <li>{@code @JsonInclude(JsonInclude.Include.NON_EMPTY)} - włącza do serializacji tylko niepuste pola</li>
 * </ul>
 * 
 * <p>Pola:</p>
 * <ul>
 *   <li>{@code businessErrorCode} - kod błędu biznesowego</li>
 *   <li>{@code businessErrorDescription} - opis błędu biznesowego</li>
 *   <li>{@code error} - ogólny komunikat błędu</li>
 *   <li>{@code validationErrors} - zestaw błędów walidacyjnych</li>
 *   <li>{@code errors} - mapa dodatkowych błędów</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
    private Integer businessErrorCode;
    private String businessErrorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;
}
