package com.example.yukka.model.uzytkownik.controller;

import static java.io.File.separator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.email.EmailService;
import com.example.yukka.auth.email.EmailTemplateName;
import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.auth.requests.RegistrationRequest;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.social.models.powiadomienie.controller.PowiadomienieService;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Serwis do zarządzania użytkownikami w systemie.
 * 
 * <ul>
 * <li><strong>unbanUzytkownik</strong> - Odbanowuje użytkownika na podstawie jego nazwy.</li>
 * <li><strong>setBanUzytkownik</strong> - Ustawia ban na użytkownika na podstawie żądania BanRequest.</li>
 * <li><strong>remove</strong> - Usuwa użytkownika na podstawie jego nazwy.</li>
 * </ul>
 * 
 * 
 * <strong>Wyjątki:</strong>
 * <ul>
 * <li>EntityNotFoundException - Rzucany, gdy użytkownik nie zostanie znaleziony.</li>
 * <li>IllegalArgumentException - Rzucany, gdy wystąpi nieprawidłowy argument.</li>
 * <li>ForbiddenException - Rzucany, gdy użytkownik nie ma odpowiednich uprawnień.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PracownikService {
    private final EmailService emailService;
    private final UzytkownikService uzytkownikService;
    private final UzytkownikRepository uzytkownikRepository;
    private final PowiadomienieService powiadomienieService;
    private final FileUtils fileUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.file.uploads.photos-output-path}")
    String fileUploadPath;

    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    public Boolean addPracownik(RegistrationRequest request) throws MessagingException {
        log.info("Rejestracja pracownika: " + request.getNazwa());

        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.checkIfUzytkownikExists(request.getNazwa(), request.getEmail());
        if (targetUzyt.isPresent()) {
            throw new IllegalArgumentException("Aktywny użytkownik o podanej nazwie lub adresie e-mail już istnieje.");
        }

        Uzytkownik uzyt = Uzytkownik.builder()
                .uzytId(uzytkownikService.createUzytkownikId())
                .nazwa(request.getNazwa())
                .email(request.getEmail())
                .haslo(passwordEncoder.encode(request.getHaslo()))
                .avatar(defaultAvatarObrazName)
                .build();

        uzytkownikService.addPracownik(uzyt);
        emailService.sendValidationEmail(uzyt, EmailTemplateName.AKTYWACJA_KONTA);
        return true;
    }

    /**
     * Odbanowuje użytkownika o podanej nazwie.
     *
     * @param nazwa Nazwa użytkownika, który ma zostać odbanowany.
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return true, jeśli użytkownik został pomyślnie odbanowany, w przeciwnym razie false.
     * @throws EntityNotFoundException Jeśli użytkownik o podanej nazwie nie istnieje.
     * @throws IllegalArgumentException Jeśli użytkownik nie jest zbanowany.
     * @throws ForbiddenException Jeśli aktualnie zalogowany użytkownik nie ma uprawnień do odbanowywania tego użytkownika.
     */
    public Boolean unbanUzytkownik(String nazwa, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Odbanowywanie użytkownika o nazwie: " + nazwa);

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if(!targetUzyt.isBan()) {
            throw new IllegalArgumentException("Użytkownik nie jest zbanowany");
        }

        if(targetUzyt.isPracownik() && !uzyt.isAdmin()) {
            throw new ForbiddenException("Nie masz uprawnień do odbanowywania tego użytkownika");
        }

        log.info("Użytkownik {} odbanowany przez {}", targetUzyt.getNazwa(), uzyt.getNazwa());
        Boolean unbanus = uzytkownikRepository.banUzytkownik(nazwa, false, null);
        powiadomienieService.sendPowiadomienieOfUnban(targetUzyt);
        return unbanus;
    }

    /**
     * Ustawia ban na użytkownika.
     *
     * @param request Obiekt zawierający szczegóły dotyczące bana.
     * @param currentUser Obiekt reprezentujący aktualnie zalogowanego użytkownika.
     * @return Odpowiedź zawierająca informację, czy operacja się powiodła.
     * @throws EntityNotFoundException Jeśli użytkownik o podanej nazwie nie istnieje.
     * @throws IllegalArgumentException Jeśli użytkownik jest już zbanowany/odbanowany.
     * @throws ForbiddenException Jeśli aktualnie zalogowany użytkownik nie ma uprawnień do banowania/odbanowywania tego użytkownika.
     */
    public Boolean setBanUzytkownik(BanRequest request, Authentication currentUser){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info( "Użytkownik {} próbuje zbanować użytkownika {}", uzyt.getNazwa(), request.getNazwa());

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(request.getNazwa())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + request.getNazwa()));

        if(targetUzyt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można banować samego siebie");
        }

        if(targetUzyt.isBan() == request.getBan()) {
            throw new IllegalArgumentException("Użytkownik jest już zbanowany/odbanowany");
        }
        
        if(!targetUzyt.isAktywowany()) {
            throw new IllegalArgumentException("Konto użytkownika nie jest aktywne");
        }
        
        if(targetUzyt.isPracownik() && !uzyt.isAdmin()) { 
            throw new ForbiddenException("Nie masz uprawnień do banowania/odbanowywania tego użytkownika");
        }

        if(targetUzyt.isAdmin()) {
            throw new IllegalArgumentException("Admini nie mogą być banowani ani odbanowywani");
        }

        if(request.getBan() == false) {
            throw new IllegalArgumentException("Użytkownik nie może być odbanowany podczas banowania");
        }

        Boolean banus = uzytkownikRepository.banUzytkownik(request.getNazwa(), request.getBan(), request.getBanDo());
        powiadomienieService.sendPowiadomienieOfBan(targetUzyt, request);
        return banus;
    }

    /**
     * Usuwa użytkownika o podanej nazwie.
     *
     * @param nazwa Nazwa użytkownika, który ma zostać usunięty.
     * @param currentUser Obecnie zalogowany użytkownik wykonujący operację.
     * @throws EntityNotFoundException Jeśli użytkownik o podanej nazwie nie istnieje.
     * @throws IllegalArgumentException Jeśli wystąpi nieprawidłowy argument.
     * @throws ForbiddenException Jeśli użytkownik nie ma odpowiednich uprawnień.
     */
    public void remove(String nazwa, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Usuwanie użytkownika o nazwie: " + nazwa);
        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));
        
        if(targetUzyt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można usuwać samego siebie w tym requescie.");
        }

        if(targetUzyt.isAdmin()) {
            throw new IllegalArgumentException("Nie można usuwać admina.");
        }else if(targetUzyt.isPracownik() && !uzyt.isAdmin()) {
            throw new IllegalArgumentException("Nikt oprócz admina nie może usuwać pracownika.");
        } else if (targetUzyt.isNormalUzytkownik() && !uzyt.isPracownik()) {
            throw new IllegalArgumentException("Nie można usuwać innych użytkowników.");
        }


        //uzytkownikRepository.removeUzytkownik(uzytOpt.getEmail());
        removeUzytkownikQueries(targetUzyt.getEmail());

        Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + targetUzyt.getUzytId());
        System.out.println("Usuwanie folderu: " + path);
        fileUtils.deleteDirectory(path);
    }

    /**
     * Seria zapytań do usunięcia użytkownika.
     *
     * @param email Email użytkownika, który ma zostać usunięty.
     */
    private void removeUzytkownikQueries(String email) {
        uzytkownikRepository.removePostyOfUzytkownik(email);
        uzytkownikRepository.removeKomentarzeOfUzytkownik(email);
        uzytkownikRepository.removeRoslinyOfUzytkownik(email);
        uzytkownikRepository.removeUzytkownik(email);
    }

}
