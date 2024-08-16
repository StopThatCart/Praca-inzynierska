package com.example.yukka.file;

import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStoreService {

    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;

    @Value("${roslina.obraz.default.jpg-file-path}")
    private String defaultRoslinaObrazPath;

    @Value("${roslina.obraz.default.name}")
    private String defaultRoslinaObrazName;

    

    // Jako iż seedowane rośliny już mają swoje ścieżki, to zwraca się tylko wygenerowane nazwy zamiast ścieżki do pliku
    public String saveSeededRoslina(@Nonnull MultipartFile sourceFile, @Nonnull String obraz) {
        String fileUploadSubPath = "defaults";
        if(!obraz.equals(defaultRoslinaObrazName)) {
            fileUploadSubPath = fileUploadSubPath + separator + "rosliny";
             //String fileName = generateFileName(obraz);
            uploadFile(sourceFile, fileUploadSubPath, obraz);
        }
        return obraz;
    }

    // Potem sie dorobi
    public String saveCustomRoslina(@Nonnull MultipartFile sourceFile, @Nonnull String nazwaLacinska, @Nonnull String uzytId) {
        throw new UnsupportedOperationException("Feature incomplete. Contact assistance.");
    }

    public String saveRoslina(@Nonnull MultipartFile sourceFile,
                              @Nonnull String obraz, @Nonnull String uzytId) {
        if(!obraz.equals(defaultRoslinaObrazName)) {
            String fileUploadSubPath = "users" + separator + uzytId + separator + "rosliny";
            String fileName = generateFileName(obraz);
            return uploadFile(sourceFile, fileUploadSubPath, fileName);
        }
        return obraz;
    }

    public String savePost(@Nonnull MultipartFile sourceFile,
                           @Nonnull String postId, @Nonnull String uzytId) {
        final String fileUploadSubPath = "users" + separator + uzytId + separator + "posty";
        String fileName = generateFileName(postId);
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String saveKomentarz(@Nonnull MultipartFile sourceFile,
                                @Nonnull String komentarzId, @Nonnull String uzytId) {
        final String fileUploadSubPath = "users" + separator + uzytId + separator + "komentarze";
        String fileName = generateFileName(komentarzId);
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }
    
    private String uploadFile(@Nonnull MultipartFile sourceFile, @Nonnull String fileUploadSubPath, @Nonnull String fileName) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            log.warn("Nie udało się stworzyć folderu: " + targetFolder);
            return null;
        }

        String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + fileName + "." + fileExtension;
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

    private String generateFileName(String baseName) {
        String normalized = baseName.replaceAll("[\\s\"'!,!@#$%^&*()-=+{}<>?~`]", "_") + "_";
        normalized = normalized.replaceAll("_+", "_");
        
        return normalized + System.currentTimeMillis();
    }


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
}

