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

    
    //@Value("${roslina.obraz.default.jpg-file-path}")
    //private String defaultRoslinaObrazPath;

    @Value("${roslina.obraz.default.name}")
    private String defaultRoslinaObrazName;

  //  @Value("${uzytkownik.obraz.default.png-file-path}")
 //   private  String avatarDefaultObrazPath;
    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    // Jako iż seedowane rośliny już mają swoje ścieżki, to zwraca się tylko wygenerowane nazwy zamiast ścieżki do pliku
    public String saveSeededRoslina(@Nonnull MultipartFile sourceFile, @Nonnull String obraz) {
        String fileUploadSubPath = "defaults";
        if(!obraz.equals(defaultRoslinaObrazName)) {
            fileUploadSubPath = fileUploadSubPath + separator + "rosliny";
             //String fileName = generateFileName(obraz);
            return uploadFile(sourceFile, fileUploadSubPath, obraz);
        }
        return obraz;
    }

    // Potem sie dorobi
    public String saveUzytkownikRoslinaObraz(@Nonnull MultipartFile sourceFile, @Nonnull String roslinaId, @Nonnull String uzytId) {
            String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "rosliny" + separator + roslinaId;
            String fileName = roslinaId;
            return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String saveRoslina(@Nonnull MultipartFile sourceFile,
                              @Nonnull String obraz, @Nonnull String uzytId) {
        if(!obraz.equals(defaultRoslinaObrazName)) {
            String fileUploadSubPath = "uzytkownicy" + separator + "rosliny";
            String fileName = generateFileName(obraz) + "_" + System.currentTimeMillis();
            return uploadFile(sourceFile, fileUploadSubPath, fileName);
        }
        return obraz;
    }

    public String saveRoslinaObrazInDzialka(@Nonnull MultipartFile sourceFile, @Nonnull String uzytId) {
        String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "dzialki" + separator + "rosliny";
        String fileName = generateFileName(uzytId) + "_" + System.currentTimeMillis();
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String savePost(@Nonnull MultipartFile sourceFile,
                           @Nonnull String postId, @Nonnull String uzytId) {
        final String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "posty";
        String fileName = generateFileName(postId) + "_" + System.currentTimeMillis();
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String saveKomentarz(@Nonnull MultipartFile sourceFile,
                                @Nonnull String komentarzId, @Nonnull String uzytId) {
        final String fileUploadSubPath = "uzytkownicy" + separator + uzytId + separator + "komentarze";
        String fileName = generateFileName(komentarzId) + "_" + System.currentTimeMillis();
        return uploadFile(sourceFile, fileUploadSubPath, fileName);
    }

    public String saveAvatar(@Nonnull MultipartFile sourceFile, @Nonnull String uzytId) {
        String fileUploadSubPath = "uzytkownicy" + separator + uzytId;
        String fileName = uzytId;
        String avatar = uploadFile(sourceFile, fileUploadSubPath, fileName);
        if(avatar == null) {
            return defaultAvatarObrazName;
        }else return avatar;
    }
    
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

    private String generateFileName(String baseName) {
        String normalized = baseName.replaceAll("[\\s\"'!,!@#$%^&*()-=+{}<>?~`]", "_") + "_";
        normalized = normalized.replaceAll("_+", "_");
        
        return normalized;
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

