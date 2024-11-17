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
