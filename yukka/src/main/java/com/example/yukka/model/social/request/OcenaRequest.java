package com.example.yukka.model.social.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie oceny.
 * <ul>
 *   <li><strong>ocenialnyId</strong> - Identyfikator ocenianego elementu. Musi być niepusty. Wiadomość błędu: "Aby coś ocenić, należy to najpierw wskazać".</li>
 *   <li><strong>lubi</strong> - Wartość oceny. Musi być niepusta. Wiadomość błędu: "Ocena musi być".</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class OcenaRequest {
    @NotEmpty(message = "Aby coś ocenić, należy to najpierw wskazać")
    private String ocenialnyId;

    @NotNull(message = "Ocena musi być")
    private boolean lubi;

}
