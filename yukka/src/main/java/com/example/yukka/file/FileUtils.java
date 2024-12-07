package com.example.yukka.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * Klasa FileUtils zapewnia funkcje do operacji na plikach obrazów, takie jak odczyt, 
 * usuwanie oraz ładowanie plików obrazów. Klasa korzysta z adnotacji Spring do wstrzykiwania 
 * wartości konfiguracyjnych oraz logowania.
 * 
 * Adnotacje:
 * <ul>
 *   <li><strong>@Slf4j</strong>: Umożliwia logowanie za pomocą biblioteki Lombok.</li>
 *   <li><strong>@Service</strong>: Oznacza klasę jako serwis Springa.</li>
 *   <li><strong>@Value</strong>: Wstrzykuje wartości konfiguracyjne z pliku properties.</li>
 * </ul>
 * 
 * Pola:
 * <ul>
 *   <li><strong>defaultRoslinaObrazPath</strong>: Ścieżka do domyślnego obrazu rośliny.</li>
 *   <li><strong>defaultRoslinaObrazName</strong>: Nazwa domyślnego obrazu rośliny.</li>
 *   <li><strong>seedRoslinaObrazyPath</strong>: Ścieżka do obrazów nasion roślin.</li>
 *   <li><strong>defaultAvatarObrazPath</strong>: Ścieżka do domyślnego awatara użytkownika.</li>
 *   <li><strong>defaultAvatarObrazName</strong>: Nazwa domyślnego awatara użytkownika.</li>
 *   <li><strong>powiadomieniaAvatarObrazPath</strong>: Ścieżka do domyślnego obrazu powiadomień.</li>
 *   <li><strong>powiadomieniaAvatarObrazName</strong>: Nazwa domyślnego obrazu powiadomień.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 *   <li><strong>readRoslinaObrazFile(String fileUrl)</strong>: Odczytuje plik obrazu rośliny na podstawie podanego URL.</li>
 *   <li><strong>readPostObrazFile(String fileUrl)</strong>: Odczytuje plik obrazu posta na podstawie podanego URL.</li>
 *   <li><strong>readFile(String fileUrl)</strong>: Odczytuje plik na podstawie podanego URL, uwzględniając domyślne ścieżki.</li>
 *   <li><strong>readFileFromLocation(Path path)</strong>: Odczytuje plik z podanej ścieżki.</li>
 *   <li><strong>loadImageFile(Path path)</strong>: Ładuje plik obrazu jako MultipartFile.</li>
 *   <li><strong>deleteDirectory(Path path)</strong>: Usuwa katalog wraz z jego zawartością.</li>
 *   <li><strong>deleteObraz(String fileUrl)</strong>: Usuwa plik obrazu na podstawie podanego URL.</li>
 *   <li><strong>deleteFile(Path path)</strong>: Usuwa plik z podanej ścieżki.</li>
 * </ul>
 */
@Slf4j
@Service
public class FileUtils {
    @Value("${roslina.obraz.default.jpg-file-path}")
    private  String defaultRoslinaObrazPath;
    @Value("${roslina.obraz.default.name}")
    private  String defaultRoslinaObrazName;

    @Value("${roslina.seed.obrazy-path}")
    private  String seedRoslinaObrazyPath;

    @Value("${uzytkownik.obraz.default.png-file-path}")
    private  String defaultAvatarObrazPath;
    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    @Value("${powiadomienia.obraz.default.png-file-path}")
    private String powiadomieniaAvatarObrazPath;
    @Value("${powiadomienia.obraz.default.name}")
    private String powiadomieniaAvatarObrazName;


    
    /**
     * Odczytuje plik obrazu rośliny z podanego URL.
     *
     * @param fileUrl URL pliku obrazu rośliny. Jeśli jest pusty lub null, zwraca null.
     *                Jeśli URL jest równy domyślnej nazwie obrazu rośliny, odczytuje plik z domyślnej ścieżki.
     * @return Tablica bajtów reprezentująca zawartość pliku obrazu rośliny.
     */
    // public  byte[] readRoslinaObrazFile(String fileUrl) {
    //     if (StringUtils.isBlank(fileUrl)) {
    //         return null;
    //     }

    //     if (fileUrl.equals(defaultRoslinaObrazName)) {
    //         Path imagePath = new File(defaultRoslinaObrazPath).toPath();
    //         return readFileFromLocation(imagePath);
    //     }
    //     Path imagePath = Paths.get(fileUrl);
    //     return readFileFromLocation(imagePath);
    // }

    /**
     * Odczytuje plik z podanego URL, uwzględniając domyślne ścieżki.
     *
     * @param fileUrl URL pliku. Jeśli jest pusty lub null, zwraca null.
     *                Jeśli URL jest równy domyślnej nazwie obrazu rośliny, odczytuje plik z domyślnej ścieżki.
     * @return Tablica bajtów reprezentująca zawartość pliku.
     */
    public byte[] readFile(String fileUrl, DefaultImage defaultImage) {
        if (StringUtils.isBlank(fileUrl)) {
            return readDefaultImage(defaultImage);
        }
        Path imagePath = null;

        Map<String, String> fileMap = Map.of(
        defaultRoslinaObrazName, defaultRoslinaObrazPath,
        defaultAvatarObrazName, defaultAvatarObrazPath,
        powiadomieniaAvatarObrazName, powiadomieniaAvatarObrazPath
        );

        if (fileMap.containsKey(fileUrl)) {
            imagePath = new File(fileMap.get(fileUrl)).toPath();
        } else {
            imagePath = Paths.get(fileUrl);
        }

        return readFileFromLocation(imagePath, defaultImage);
    }

    /**
     * Odczytuje plik z podanej ścieżki.
     *
     * @param path Ścieżka pliku. Jeśli jest null, zwraca null.
     * @return Tablica bajtów reprezentująca zawartość pliku.
     */
    public byte[] readFileFromLocation(Path path, DefaultImage defaultImage) {
        if(path == null || path.toString().isEmpty()) {
            System.out.println("No jest null");
            return readDefaultImage(defaultImage);
        }
        
        try {
            File imageFile = new File(path.toString());
            if(imageFile.exists()) {
                return Files.readAllBytes(path);
            } else {
                System.out.println("No jest null2");
                return readDefaultImage(defaultImage);
            }
        } catch (IOException e) {
            log.warn("Nie znaleziono pliku w ścieżce {}", path.toString());
        }
        return null;
    }

    private byte[] readDefaultImage(DefaultImage defaultImage) {
        try {
            if (defaultImage == null) {
                return null;
            }
            switch (defaultImage) {
                case AVATAR:
                    return Files.readAllBytes(Paths.get(defaultAvatarObrazPath));
                case POWIADOMIENIA:
                    return Files.readAllBytes(Paths.get(powiadomieniaAvatarObrazPath));
                case ROSLINA:
                    return Files.readAllBytes(Paths.get(defaultRoslinaObrazPath));
                default:
                    return null;
            }
        } catch (IOException e) {
            log.warn("Nie znaleziono pliku domyślnego obrazu");
            return null;
        }
    }


    /**
     * Ładuje plik obrazu jako MultipartFile.
     *
     * @param path Ścieżka pliku obrazu. Jeśli jest null, zwraca null.
     * @return Plik obrazu jako MultipartFile.
     */
    public MultipartFile loadImageFile(Path path) {
        File imageFile = new File(path.toString());

        if (imageFile.exists()) {
            try {
                String fileName = imageFile.getName();
                String baseName = fileName;

                if (fileName.endsWith(".jpg")) {
                    baseName = fileName.substring(0, fileName.length() - 4);
                } else if (fileName.endsWith(".png")) {
                    baseName = fileName.substring(0, fileName.length() - 4);
                }

                FileInputStream input = new FileInputStream(imageFile);
                return new MockMultipartFile(baseName, baseName, "image/jpeg", input);
            } catch (IOException e) {
                log.error("Nie udało się załadować pliku obrazu: " + path, e);
            }
        } else {
            log.warn("Plik obrazu nie znaleziony: " + path);
        }
    return null;
    }

    /**
     * Usuwa katalog wraz z jego zawartością.
     *
     * @param path Ścieżka katalogu do usunięcia. Jeśli jest null, zwraca false.
     * @return true, jeśli katalog został usunięty; w przeciwnym razie false.
     */
    public boolean deleteDirectory(Path path) {
        if (path == null) {
            log.warn("Ścieżka usuwania jest null");
            return false;
        }
        try {
            File directory = new File(path.toString());
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDirectory(file.toPath());
                        } else {
                            file.delete();
                        }
                    }
                }
                return directory.delete();
            } else {
                log.warn("Folder nie istnieje lub nie jest folderem");
                return false;
            }
        } catch (Exception e) {
            log.error("Wystąpił błąd podczas usuwania folderu: " + e.getMessage());
            return false;
        }
    }

    /**
     * Usuwa plik obrazu na podstawie podanego URL.
     *
     * @param fileUrl URL pliku obrazu. Jeśli jest pusty lub null, zwraca false.
     *                Nie można usunąć domyślnych obrazów.
     * @return true, jeśli plik został usunięty; w przeciwnym razie false.
     */
    public boolean deleteObraz(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return false;
        }
        if (fileUrl.equals(defaultAvatarObrazPath) 
            || fileUrl.equals(defaultRoslinaObrazPath) 
            || fileUrl.equals(powiadomieniaAvatarObrazPath)) {
            log.error("Nie można usunąć domyślnego obrazu");
            return false;
        }

        Path imagePath = Paths.get(fileUrl);
        return deleteFile(imagePath);
    }

    /**
     * Usuwa plik z podanej ścieżki.
     *
     * @param path Ścieżka pliku. Jeśli jest null, zwraca false.
     * @return true, jeśli plik został usunięty; w przeciwnym razie false.
     */
    private boolean deleteFile(Path path) {
        if (path == null) {
            log.warn("Ścieżka usuwania jest null");
            return false;
        }
        try {
            File file = new File(path.toString());
            if (file.exists()) {
                if (file.delete()) {
                    log.info("Plik został usunięty: " + path);
                    return true;
                } else {
                    log.warn("Nie udało się usunąć pliku: " + path);
                    return false;
                }
            } else {
                log.warn("Plik nie istnieje: " + path);
                return false;
            }
        } catch (Exception e) {
            log.error("Wystąpił błąd podczas usuwania pliku: " + e.getMessage());
            return false;
        }
    }
}
