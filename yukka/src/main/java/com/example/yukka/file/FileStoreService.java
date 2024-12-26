package com.example.yukka.file;

import java.awt.image.BufferedImage;
import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <strong>FileStoreService</strong> - Serwis do zarządzania plikami obrazów.
 * 
 * <ul>
 * <li><strong>acceptableImageExtensions</strong> - Tablica akceptowanych rozszerzeń plików obrazów.</li>
 * <li><strong>fileUploadPath</strong> - Ścieżka do katalogu, gdzie będą zapisywane pliki.</li>
 * <li><strong>defaultRoslinaObrazName</strong> - Domyślna nazwa obrazu rośliny.</li>
 * <li><strong>defaultAvatarObrazName</strong> - Domyślna nazwa obrazu avatara użytkownika.</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>saveSeededRoslina</strong> - Zapisuje obraz rośliny z seedowanych danych.</li>
 * <li><strong>saveUzytkownikRoslinaObraz</strong> - Zapisuje obraz rośliny użytkownika.</li>
 * <li><strong>saveRoslina</strong> - Zapisuje obraz rośliny.</li>
 * <li><strong>saveRoslinaObrazInDzialka</strong> - Zapisuje obraz rośliny w działce użytkownika.</li>
 * <li><strong>savePost</strong> - Zapisuje obraz posta użytkownika.</li>
 * <li><strong>saveKomentarz</strong> - Zapisuje obraz komentarza użytkownika.</li>
 * <li><strong>saveAvatar</strong> - Zapisuje obraz avatara użytkownika.</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>uploadFile</strong> - Przesyła plik na serwer.</li>
 * <li><strong>generateFileName</strong> - Generuje nazwę pliku na podstawie podanej nazwy.</li>
 * <li><strong>getFileExtension</strong> - Pobiera rozszerzenie pliku.</li>
 * <li><strong>validateImage</strong> - Waliduje obraz pod kątem akceptowanych rozszerzeń i minimalnych wymiarów.</li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileStoreService {

    private String[] acceptableImageExtensions = {"jpg", "jpeg", "png", "gif"};

    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;

    @Value("${roslina.obraz.default.name}")
    private String defaultRoslinaObrazName;

    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    

    // Jako iż seedowane rośliny już mają swoje ścieżki, to zwraca się tylko wygenerowane nazwy zamiast ścieżki do pliku
    /**
     * Zapisuje plik rośliny z nasionami.
     *
     * @param sourceFile Plik źródłowy do przesłania.
     * @param obraz Nazwa obrazu rośliny.
     * @return Nazwa przesłanego pliku lub oryginalna nazwa obrazu, jeśli jest domyślna.
     */
    public String saveSeededRoslina(@Nonnull MultipartFile sourceFile, @Nonnull String obraz) {
        //validateImage(sourceFile, false);
        String fileUploadSubPath = "defaults";
        if(!obraz.equals(defaultRoslinaObrazName)) {
            fileUploadSubPath = fileUploadSubPath + separator + "rosliny" + separator + "seed";
            return uploadFile(sourceFile, fileUploadSubPath, obraz);
        }
        return obraz;
    }

    
    /**
     * Zapisuje obraz rośliny użytkownika.
     *
     * @param sourceFile Plik obrazu do zapisania.
     * @param roslinaId Identyfikator rośliny.
     * @param uzytId Identyfikator użytkownika.
     * @return Ścieżka do zapisanego pliku.
     * @throws IllegalArgumentException Jeśli plik obrazu jest nieprawidłowy.
     */
    public String saveUzytkownikRoslinaObraz(@Nonnull MultipartFile sourceFile, @Nonnull String roslinaId, @Nonnull String uzytId) {
        validateImage(sourceFile, false);
        String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "rosliny" + separator + roslinaId;
        String fileName = roslinaId;
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    /**
     * Zapisuje obraz rośliny.
     *
     * @param sourceFile Plik obrazu do zapisania.
     * @param obraz Nazwa obrazu rośliny.
     * @return Ścieżka do zapisanego pliku.
     * @throws IllegalArgumentException Jeśli plik obrazu jest nieprawidłowy.
     */
    public String saveRoslina(@Nonnull MultipartFile sourceFile, @Nonnull String obraz) {
        validateImage(sourceFile, false);
        if(!obraz.equals(defaultRoslinaObrazName)) {
            String fileUploadSubPath = "rosliny" + separator + "pracownicy";
            String fileName = generateFileName(obraz) + "_" + System.currentTimeMillis();
            return uploadFile(sourceFile, fileUploadSubPath, fileName);
        }
        return obraz;
    }

    /**
     * Zapisuje obraz rośliny w działce użytkownika.
     *
     * @param sourceFile Plik obrazu do zapisania.
     * @param uzytId Identyfikator użytkownika.
     * @return Ścieżka do zapisanego pliku.
     * @throws IllegalArgumentException Jeśli plik obrazu jest nieprawidłowy.
     */
    public String saveRoslinaObrazInDzialka(@Nonnull MultipartFile sourceFile, @Nonnull String uzytId) {
        validateImage(sourceFile, false);
        String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "dzialki" + separator + "rosliny";
        String fileName = generateFileName(uzytId) + "_" + System.currentTimeMillis();
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    /**
     * Zapisuje obraz posta użytkownika.
     *
     * @param sourceFile Plik obrazu do zapisania.
     * @param postId Identyfikator posta.
     * @param uzytId Identyfikator użytkownika.
     * @return Ścieżka do zapisanego pliku.
     * @throws IllegalArgumentException Jeśli plik obrazu jest nieprawidłowy.
     */
    public String savePost(@Nonnull MultipartFile sourceFile,
                           @Nonnull String postId, @Nonnull String uzytId) {
        
        validateImage(sourceFile, true);
        final String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "posty";
        String fileName = "";
        if (!sourceFile.getName().isEmpty()) {
            fileName =  generateFileName(sourceFile.getName()) + System.currentTimeMillis();
        } else {
            fileName = generateFileName(postId) + System.currentTimeMillis();
        }
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    /**
     * Zapisuje obraz komentarza użytkownika.
     *
     * @param sourceFile Plik obrazu do zapisania.
     * @param komentarzId Identyfikator komentarza.
     * @param uzytId Identyfikator użytkownika.
     * @return Ścieżka do zapisanego pliku.
     * @throws IllegalArgumentException Jeśli plik obrazu jest nieprawidłowy.
     */
    public String saveKomentarz(@Nonnull MultipartFile sourceFile,
                                @Nonnull String komentarzId, @Nonnull String uzytId) {
        
        validateImage(sourceFile, true);
        final String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "komentarze";
        
        String fileName = "";
        if (!sourceFile.getName().isEmpty()) {
            fileName =  generateFileName(sourceFile.getName()) + "_" + System.currentTimeMillis();
        } else {
            fileName = generateFileName(komentarzId) + "_" + System.currentTimeMillis();
        }
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    /**
     * Zapisuje obraz avatara użytkownika.
     *
     * @param sourceFile Plik obrazu do zapisania.
     * @param uzytId Identyfikator użytkownika.
     * @return Ścieżka do zapisanego pliku.
     * @throws IllegalArgumentException Jeśli plik obrazu jest nieprawidłowy.
     */
    public String saveAvatar(@Nonnull MultipartFile sourceFile, @Nonnull String uzytId) {
        validateImage(sourceFile, false);

        String fileUploadSubPath = "uzytkownicy"+ separator + uzytId + separator + "avatar";
        String fileName = "avatar";
        String avatar = uploadFile(sourceFile, fileUploadSubPath, fileName);
        if(avatar == null) {
            return defaultAvatarObrazName;
        }else return avatar;
    }
    
    /**
     * Przesyła plik do określonej ścieżki na serwerze.
     *
     * @param sourceFile         plik do przesłania
     * @param fileUploadSubPath  podkatalog, do którego plik ma zostać przesłany
     * @param fileName           nazwa pliku docelowego
     * @return                   pełna ścieżka do zapisanego pliku lub null w przypadku błędu
     */
    private String uploadFile(@Nonnull MultipartFile sourceFile, @Nonnull String fileUploadSubPath, @Nonnull String fileName) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            log.warn("Nie udało się stworzyć folderu: " + targetFolder);
            return null;
        }

        String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFileName = fileName;
        if (!fileName.toLowerCase().endsWith("." + fileExtension)) {
            targetFileName += "." + fileExtension;
        }
        
        String targetFilePath = finalUploadPath + separator + targetFileName;
        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Zapisano plik do: " + targetPath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("Błąd podczas zapisu pliku do ścieżki: " + targetFilePath, e);
            return null;
        }
    }

    /**
     * Generuje nazwę pliku na podstawie podanej nazwy.
     *
     * @param baseName nazwa pliku
     * @return nazwa pliku zastąpiona znakami niedozwolonymi
     */
    private String generateFileName(String baseName) {
        String normalized = baseName.replaceAll("[\\s\"'!,!@#$%^&*()-=+{}<>?~`]", "_") + "_";
        normalized = normalized.replaceAll("_+", "_");
        
        return normalized;
    }

    /**
     * Pobiera rozszerzenie pliku.
     *
     * @param fileName nazwa pliku
     * @return rozszerzenie pliku
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    
    /**
     * Waliduje obraz pod kątem akceptowanych rozszerzeń i minimalnych wymiarów.
     *
     * @param sourceFile plik obrazu
     * @param gifAllowed czy pliki .gif są dozwolone
     * @throws IllegalArgumentException jeśli plik nie jest obrazem lub nie spełnia wymagań
     */
    private void validateImage(MultipartFile sourceFile, boolean gifAllowed) {
        String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        if (fileExtension.equals("gif") && !gifAllowed) {
            log.warn("Nie można zapisać pliku .gif jako obrazu");
            throw new IllegalArgumentException("akceptowane są tylko pliki .jpg, .jpeg, .png");
        }
        if(!Arrays.asList(acceptableImageExtensions).contains(fileExtension)) {

            log.warn("Nie można zapisać pliku " + fileExtension + " jako obrazu");
            throw new IllegalArgumentException("akceptowane są tylko pliki .jpg, .jpeg, .png .gif");
        }

        try {
            BufferedImage image = ImageIO.read(sourceFile.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("plik nie jest prawidłowym obrazem");
            }
            int width = image.getWidth();
            int height = image.getHeight();
            if (width < 25 || height < 25) {
                log.warn("Obraz musi mieć przynajmniej 25x25 pikseli");
                throw new IllegalArgumentException("obraz musi mieć przynajmniej 25x25 pikseli");
            }
        } catch (IOException e) {
            log.error("Błąd podczas odczytu obrazu", e);
            throw new IllegalArgumentException("nie udało się odczytać obrazu");
        }
    }
}

