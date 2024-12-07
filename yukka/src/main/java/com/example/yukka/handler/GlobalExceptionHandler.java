package com.example.yukka.handler;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_EARLY;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.example.yukka.handler.YukkaErrorCodes.ACCOUNT_BANNED;
import static com.example.yukka.handler.YukkaErrorCodes.ACCOUNT_DISABLED;
import static com.example.yukka.handler.YukkaErrorCodes.BAD_CREDENTIALS;
import static com.example.yukka.handler.YukkaErrorCodes.BLOCKED_UZYTKOWNIK;
import static com.example.yukka.handler.YukkaErrorCodes.ENTITY_NOT_FOUND;
import static com.example.yukka.handler.YukkaErrorCodes.FORBIDDEN_EXCEPTION;
import com.example.yukka.handler.exceptions.BannedUzytkownikException;
import com.example.yukka.handler.exceptions.BlockedUzytkownikException;
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;

import jakarta.mail.MessagingException;

/**
 * GlobalExceptionHandler obsługuje różne wyjątki rzucane przez aplikację i zwraca odpowiednie odpowiedzi HTTP.
 * 
 * <ul>
 * <li><strong>handleIllegalArgumentException</strong>: Obsługuje wyjątek IllegalArgumentException i zwraca odpowiedź z kodem statusu BAD_REQUEST.</li>
 * <li><strong>handleException (LockedException)</strong>: Obsługuje wyjątek LockedException i zwraca odpowiedź z kodem statusu UNAUTHORIZED.</li>
 * <li><strong>handleException (BannedUzytkownikException)</strong>: Obsługuje wyjątek BannedUzytkownikException i zwraca odpowiedź z kodem statusu UNAUTHORIZED.</li>
 * <li><strong>handleException (ForbiddenException)</strong>: Obsługuje wyjątek ForbiddenException i zwraca odpowiedź z kodem statusu FORBIDDEN.</li>
 * <li><strong>handleException (BlockedUzytkownikException)</strong>: Obsługuje wyjątek BlockedUzytkownikException i zwraca odpowiedź z kodem statusu UNAUTHORIZED.</li>
 * <li><strong>handleException (DisabledException)</strong>: Obsługuje wyjątek DisabledException i zwraca odpowiedź z kodem statusu UNAUTHORIZED.</li>
 * <li><strong>handleException (EntityNotFoundException)</strong>: Obsługuje wyjątek EntityNotFoundException i zwraca odpowiedź z kodem statusu NOT_FOUND.</li>
 * <li><strong>handleException (EntityAlreadyExistsException)</strong>: Obsługuje wyjątek EntityAlreadyExistsException i zwraca odpowiedź z kodem statusu BAD_REQUEST.</li>
 * <li><strong>handleException (BadCredentialsException)</strong>: Obsługuje wyjątek BadCredentialsException i zwraca odpowiedź z kodem statusu UNAUTHORIZED.</li>
 * <li><strong>handleException (HttpMessageNotReadableException)</strong>: Obsługuje wyjątek HttpMessageNotReadableException i zwraca odpowiedź z kodem statusu BAD_REQUEST.</li>
 * <li><strong>handleException (IllegalStateException)</strong>: Obsługuje wyjątek IllegalStateException i zwraca odpowiedź z kodem statusu TOO_EARLY.</li>
 * <li><strong>handleException (MessagingException)</strong>: Obsługuje wyjątek MessagingException i zwraca odpowiedź z kodem statusu INTERNAL_SERVER_ERROR.</li>
 * <li><strong>handleMethodArgumentNotValidException</strong>: Obsługuje wyjątek MethodArgumentNotValidException i zwraca odpowiedź z kodem statusu BAD_REQUEST, zawierającą błędy walidacji.</li>
 * <li><strong>handleException (Exception)</strong>: Obsługuje wszystkie inne wyjątki i zwraca odpowiedź z kodem statusu INTERNAL_SERVER_ERROR.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    

        /**
         * Metoda obsługująca wyjątki typu IllegalArgumentException.
         *
         * @param ex wyjątek typu IllegalArgumentException, który został rzucony
         * @param request obiekt WebRequest zawierający szczegóły żądania
         * @return obiekt ResponseEntity z kodem statusu BAD_REQUEST oraz treścią zawierającą wiadomość wyjątku
         */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ex.getMessage()
                );
    }


    /**
     * Metoda obsługująca wyjątek typu LockedException.
     *
     * @param exp wyjątek typu LockedException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu UNAUTHORIZED oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_BANNED.getCode())
                                .businessErrorDescription(ACCOUNT_BANNED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu BannedUzytkownikException.
     *
     * @param exp wyjątek typu BannedUzytkownikException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu UNAUTHORIZED oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(BannedUzytkownikException.class)
    public ResponseEntity<ExceptionResponse> handleException(BannedUzytkownikException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_BANNED.getCode())
                                .businessErrorDescription(ACCOUNT_BANNED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu ForbiddenException.
     *
     * @param exp wyjątek typu ForbiddenException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu FORBIDDEN oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleException(ForbiddenException exp) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(FORBIDDEN_EXCEPTION.getCode())
                                .businessErrorDescription(FORBIDDEN_EXCEPTION.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu BlockedUzytkownikException.
     *
     * @param exp wyjątek typu BlockedUzytkownikException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu UNAUTHORIZED oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(BlockedUzytkownikException.class)
    public ResponseEntity<ExceptionResponse> handleException(BlockedUzytkownikException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BLOCKED_UZYTKOWNIK.getCode())
                                .businessErrorDescription(BLOCKED_UZYTKOWNIK.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }


    /**
     * Metoda obsługująca wyjątek typu DisabledException.
     *
     * @param exp wyjątek typu DisabledException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu UNAUTHORIZED oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_DISABLED.getCode())
                                .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu EntityNotFoundException.
     *
     * @param exp wyjątek typu EntityNotFoundException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu NOT_FOUND oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(EntityNotFoundException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ENTITY_NOT_FOUND.getCode())
                                .businessErrorDescription(ENTITY_NOT_FOUND.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu EntityAlreadyExistsException.
     *
     * @param exp wyjątek typu EntityAlreadyExistsException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu BAD_REQUEST oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(EntityAlreadyExistsException exp) {
                return ResponseEntity
                        .status(BAD_REQUEST)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorCode(ENTITY_NOT_FOUND.getCode())
                                        .businessErrorDescription(ENTITY_NOT_FOUND.getDescription())
                                        .error(exp.getMessage())
                                        .build()
                        );
        }

        /**
         * Metoda obsługująca wyjątek typu BadCredentialsException.
         *
         * @return obiekt ResponseEntity z kodem statusu UNAUTHORIZED oraz treścią zawierającą wiadomość wyjątku
         */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BAD_CREDENTIALS.getCode())
                                .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                                .error("Niepoprawny login lub hasło")
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu HttpMessageNotReadableException.
     *
     * @param exp wyjątek typu HttpMessageNotReadableException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu BAD_REQUEST oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleException(HttpMessageNotReadableException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu IllegalStateException.
     *
     * @param exp wyjątek typu IllegalStateException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu TOO_EARLY oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleException(IllegalStateException exp) {
        return ResponseEntity
                .status(TOO_EARLY)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription(exp.getMessage())
                                .build()
                );
    }

        /**
         * Metoda obsługująca wyjątek typu MessagingException.
         *
         * @param exp wyjątek typu MessagingException, który został rzucony
         * @return obiekt ResponseEntity z kodem statusu INTERNAL_SERVER_ERROR oraz treścią zawierającą wiadomość wyjątku
         */
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu MethodArgumentNotValidException.
     *
     * @param exp wyjątek typu MethodArgumentNotValidException, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu BAD_REQUEST oraz treścią zawierającą błędy walidacji
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    /**
     * Metoda obsługująca wyjątek typu Exception. 
     * Jest ona wywoływana do wyjątków, które nie są obsługiwane przez handlera.
     *
     * @param exp wyjątek typu Exception, który został rzucony
     * @return obiekt ResponseEntity z kodem statusu INTERNAL_SERVER_ERROR oraz treścią zawierającą wiadomość wyjątku
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        exp.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription("Wystąpił błąd wewnętrzny serwera. Spróbuj ponownie później.")
                                .error(exp.getMessage())
                                .build()
                );
    }

}
