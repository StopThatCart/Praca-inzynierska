package com.example.yukka.file;

import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import static java.lang.System.currentTimeMillis;
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

    public String saveRoslina(@Nonnull MultipartFile sourceFile,
                              @Nonnull String roslinaNazwaLacinska, @Nonnull String uzytkownikNazwa) {
        final String fileUploadSubPath = "users" + separator + uzytkownikNazwa + separator + "rosliny";
        String fileName = generateFileName(roslinaNazwaLacinska);
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String savePost(@Nonnull MultipartFile sourceFile,
                           @Nonnull String postId, @Nonnull String uzytkownikNazwa) {
        final String fileUploadSubPath = "users" + separator + uzytkownikNazwa + separator + "posty";
        String fileName = generateFileName(postId);
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String saveKomentarz(@Nonnull MultipartFile sourceFile,
                                @Nonnull String komentarzId, @Nonnull String uzytkownikNazwa) {
        final String fileUploadSubPath = "users" + separator + uzytkownikNazwa + separator + "komentarze";
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
            log.info("Zapisano plik do: " + targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("Błąd podczas zapisu pliku do ścieżki: " + targetFilePath, e);
            return null;
        }
    }

    private String generateFileName(String baseName) {
        return baseName.replaceAll("\\s+", "_") + "_" + currentTimeMillis();
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

