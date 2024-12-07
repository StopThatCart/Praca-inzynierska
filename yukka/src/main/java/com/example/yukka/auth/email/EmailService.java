
package com.example.yukka.auth.email;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.token.Token;
import com.example.yukka.model.uzytkownik.token.TokenRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Klasa serwisowa do obsługi operacji związanych z e-mailami, takich jak 
 * wysyłanie e-maili aktywacyjnych, resetowania hasła i zmiany adresu e-mail.
 * Wykorzystuje JavaMailSender Springa do wysyłania e-maili oraz Thymeleaf do 
 * szablonów e-maili.
 * 
 * Zależności:
 * - JavaMailSender: do wysyłania e-maili.
 * - SpringTemplateEngine: do przetwarzania szablonów e-maili.
 * - TokenRepository: do zarządzania tokenami aktywacyjnymi.
 * 
 * Konfiguracja:
 * - activationUrl: URL do aktywacji konta.
 * - resetHasloUrl: URL do resetowania hasła.
 * 
 * Metody:
 * - sendEmail: Wysyła e-mail z określonymi parametrami.
 * - generateAndSaveActivationToken: Generuje i zapisuje token aktywacyjny 
 *   dla użytkownika.
 * - sendValidationEmail: Wysyła e-mail weryfikacyjny na podstawie 
 *   określonego szablonu.
 * - generateActivationCode: Generuje losowy kod aktywacyjny o określonej 
 *   długości.
 * 
 * Logowanie:
 * - Loguje różne działania, takie jak wysyłanie e-maili i generowanie tokenów.
 * 
 * Asynchroniczność:
 * - Metody do wysyłania e-maili są oznaczone adnotacją @Async, aby działały 
 *   asynchronicznie.
 * 
 * Wyjątki:
 * - Rzuca IllegalArgumentException dla nieprawidłowych typów szablonów e-maili.
 * - Rzuca MessagingException dla błędów wysyłania e-maili.
 * 
 * Użycie:
 * - Ten serwis może być używany do wysyłania różnych typów e-maili do 
 *   użytkowników, w tym powiadomień o aktywacji konta, resetowaniu hasła 
 *   i zmianie adresu e-mail.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final TokenRepository tokenRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${application.mailing.frontend.zmiana-haslo-url}")
    private String resetHasloUrl;


    
    /**
     * Wysyła e-mail asynchronicznie przy użyciu określonych parametrów.
     *
     * @param to adres e-mail odbiorcy
     * @param username nazwa użytkownika odbiorcy
     * @param emailTemplate nazwa szablonu dla treści e-maila
     * @param confirmationUrl URL do potwierdzenia e-maila
     * @param activationCode kod aktywacyjny dołączony do e-maila
     * @param subject temat e-maila
     * @throws MessagingException jeśli wystąpi błąd podczas wysyłania e-maila
     */    
    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        log.info("Wysyłanie e-maila do: " + to
        + " z tematem: " + subject
        + " i treścią: " + emailTemplate
        + " oraz linkiem: " + confirmationUrl
        + " oraz kodem aktywacyjnym: " + activationCode);

        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.name();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("nazwa", username);
        if(confirmationUrl != null) {
            properties.put("UrlPotwierdzenia", confirmationUrl);
        }

        properties.put("kod_aktywacyjny", activationCode);
        

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("yukka@email.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }


    /**
     * Generuje i zapisuje token aktywacyjny dla danego użytkownika.
     * 
     * @param uzyt Użytkownik, dla którego generowany jest token aktywacyjny.
     * @param emailTemplateName Nazwa szablonu e-maila powiązanego z tokenem.
     * @return Wygenerowany token aktywacyjny jako String.
     * 
     * Metoda wykonuje następujące kroki:
     * 1. Loguje generowanie i zapisywanie tokena aktywacyjnego.
     * 2. Generuje 6-znakowy kod aktywacyjny.
     * 3. Tworzy nowy obiekt Token z wygenerowanym kodem, bieżącą datą jako datą utworzenia
     *    oraz datą wygaśnięcia ustawioną na 15 minut później.
     * 4. Sprawdza, czy istnieje już token dla danego użytkownika i szablonu e-maila, 
     *    jeśli tak, usuwa go.
     * 5. Dodaje nowy token do repozytorium tokenów.
     */
    private String generateAndSaveActivationToken(Uzytkownik uzyt, EmailTemplateName emailTemplateName) {
        log.info("Generowanie i zapis tokena aktywacyjnego dla użytkownika: " + uzyt.getEmail());
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .dataUtworzenia(LocalDateTime.now())
                .dataWygasniecia(LocalDateTime.now().plusMinutes(15))
                .build();
        

        Optional<Token> presentToken = tokenRepository.findByUzytkownikEmail(uzyt.getEmail(), emailTemplateName.getName());
        if(presentToken.isPresent()) {
            tokenRepository.removeToken(uzyt.getEmail(), presentToken.get().getToken());
        }

        tokenRepository.add(uzyt.getEmail(), 
        token.getToken(), emailTemplateName.getName(), 
        null,
        token.getDataUtworzenia(), token.getDataWygasniecia(),
        token.getDataWalidacji());

        return generatedToken;
    }


    
    /**
     * Generuje i zapisuje token aktywacyjny dla danego użytkownika.
     *
     * @param uzyt Użytkownik, dla którego generowany jest token aktywacyjny.
     * @param nowyEmail Nowy adres e-mail powiązany z tokenem aktywacyjnym.
     * @param emailTemplateName Nazwa szablonu e-maila powiązanego z tokenem.
     * @return Wygenerowany token aktywacyjny jako String.
     */
    private String generateAndSaveActivationToken(Uzytkownik uzyt, String nowyEmail, EmailTemplateName emailTemplateName) {
        log.info("Generowanie i zapis tokena aktywacyjnego dla użytkownika: " + uzyt.getEmail());
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .dataUtworzenia(LocalDateTime.now())
                .dataWygasniecia(LocalDateTime.now().plusMinutes(15))
                .build();
        

        Optional<Token> presentToken = tokenRepository.findByUzytkownikEmail(uzyt.getEmail(), emailTemplateName.getName());
        if(presentToken.isPresent()) {
            tokenRepository.removeToken(uzyt.getEmail(), presentToken.get().getToken());
        }

        tokenRepository.add(uzyt.getEmail(), 
        token.getToken(), emailTemplateName.getName(), 
        nowyEmail,
        token.getDataUtworzenia(), token.getDataWygasniecia(),
        token.getDataWalidacji());

        return generatedToken;
    }
    

    /**
     * Wysyła e-mail weryfikacyjny do użytkownika na podstawie podanego typu szablonu e-maila.
     * 
     * @param uzyt Użytkownik, do którego zostanie wysłany e-mail.
     * @param templateName Typ szablonu e-maila do użycia (np. AKTYWACJA_KONTA, RESET_HASLO).
     * @throws MessagingException Jeśli wystąpi błąd podczas wysyłania e-maila.
     * @throws IllegalArgumentException Jeśli typ szablonu e-maila jest nieprawidłowy lub jeśli szablon ZMIANA_EMAIL jest używany bez podania nowego adresu e-mail.
     */ 
    @Async
    public void sendValidationEmail(Uzytkownik uzyt, EmailTemplateName templateName) throws MessagingException {
        String temat = null;
        String newToken = null;
        if(templateName == EmailTemplateName.AKTYWACJA_KONTA) {
            log.info("Wysyłanie e-maila aktywacyjnego do: " + uzyt.getEmail());
            newToken = generateAndSaveActivationToken(uzyt, EmailTemplateName.AKTYWACJA_KONTA);
            temat = "Aktywacja konta";
        } else if(templateName == EmailTemplateName.RESET_HASLO) {
            log.info("Wysyłanie e-maila resetowania hasła do: " + uzyt.getEmail());
            newToken = generateAndSaveActivationToken(uzyt, EmailTemplateName.RESET_HASLO);
            temat = "Zmiana hasła";
        } else if (templateName == EmailTemplateName.ZMIANA_EMAIL) {
            throw new IllegalArgumentException("Nie można wysłać e-maila zmiany adresu e-mail bez podania nowego adresu e-mail");
        } else {
            throw new IllegalArgumentException("Nieprawidłowy typ szablonu e-maila");
        }

        sendEmail(
                uzyt.getEmail(), uzyt.getNazwa(),
                templateName, activationUrl,
                newToken, temat);
    }


    /**
     * Wysyła e-mail weryfikacyjny do użytkownika na podstawie podanego typu szablonu e-maila.
     *
     * @param uzyt Użytkownik, do którego zostanie wysłany e-mail.
     * @param nowyEmail Nowy adres e-mail, który ma zostać zweryfikowany.
     * @param templateName Typ szablonu e-maila do użycia (np. ZMIANA_EMAIL).
     * @throws MessagingException Jeśli wystąpi błąd podczas wysyłania e-maila.
     * @throws IllegalArgumentException Jeśli typ szablonu e-maila jest nieprawidłowy.
     */
    @Async
    public void sendValidationEmail(Uzytkownik uzyt, String nowyEmail, EmailTemplateName templateName) throws MessagingException {
        String temat = null;
        String newToken = null;
        if (templateName == EmailTemplateName.ZMIANA_EMAIL) {
            log.info("Wysyłanie e-maila zmiany adresu e-mail do: " + uzyt.getEmail());
            newToken = generateAndSaveActivationToken(uzyt, nowyEmail, EmailTemplateName.ZMIANA_EMAIL);
            temat = "Zmiana adresu e-mail";
        } else {
            throw new IllegalArgumentException("Nieprawidłowy typ szablonu e-maila");
        }

        sendEmail(
                nowyEmail, uzyt.getNazwa(),
                templateName, null,
                newToken, temat);
    }

    /**
     * Generuje losowy kod aktywacyjny o określonej długości.
     *
     * @param length Długość kodu aktywacyjnego.
     * @return Wygenerowany kod aktywacyjny jako String.
     */
    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        if (tokenRepository.findByToken(codeBuilder.toString()).isPresent()) {
            return generateActivationCode(length);
        }

        return codeBuilder.toString();
    }




}
