package com.example.yukka.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

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
    private  String avatarDefaultObrazPath;
    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    @Value("${powiadomienia.obraz.default.png-file-path}")
    private String powiadomieniaAvatarObrazPath;

    @Value("${powiadomienia.obraz.default.name}")
    private String powiadomieniaAvatarObrazName;
    

    public  byte[] readRoslinaObrazFile(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }

        if (fileUrl.equals(defaultRoslinaObrazName)) {
            Path imagePath = new File(defaultRoslinaObrazPath).toPath();
            return readFileFromLocation(imagePath);
        }
        Path imagePath = Paths.get(fileUrl);
        return readFileFromLocation(imagePath);
    }

    public byte[] readPostObrazFile(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        Path imagePath = Paths.get(fileUrl);
        return readFileFromLocation(imagePath);
    }

    public byte[] readKomentarzObrazFile(String fileUrl) {
        //System.out.println("Komentarz: " + fileUrl);
        return readPostObrazFile(fileUrl);
    }

    public  byte[] readAvatarFile(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }

        if (fileUrl.equals(defaultAvatarObrazName)) {
            Path imagePath = new File(avatarDefaultObrazPath).toPath();
            return readFileFromLocation(imagePath);
        }

        if (fileUrl.equals(powiadomieniaAvatarObrazName)) {
            Path imagePath = new File(powiadomieniaAvatarObrazPath).toPath();
            return readFileFromLocation(imagePath);
        }

        Path imagePath = Paths.get(fileUrl);
        return readFileFromLocation(imagePath);
    }

    public byte[] readFileFromLocation(Path path) {
        if(path == null) {
            System.out.println("No jest null");
            return null;
        }
        try {
            File imageFile = new File(path.toString());
            if(imageFile.exists()) {
                return Files.readAllBytes(path);
            } else {
                System.out.println("No jest null2");
                return Files.readAllBytes(Paths.get(defaultRoslinaObrazPath));
            }
        } catch (IOException e) {
            log.warn("Nie znaleziono pliku w ścieżce {}", path.toString());
        }
        return null;
    }

    public MultipartFile loadImageFile(Path path) {
        // Zbudowanie ścieżki do pliku obrazu na podstawie nazwy łacińskiej rośliny
        
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
              //  log.info("Nazwa pliku: " + baseName + (fileName.endsWith(".jpg") ? ".jpg" : (fileName.endsWith(".png") ? ".png" : "")));

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

    public boolean deleteDirectory(Path path) {
        if (path == null) {
            System.out.println("Ścieżka usuwania jest null");
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
                System.out.println("Folder nie istnieje lub nie jest folderem");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas usuwania folderu: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteObraz(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return false;
        }
        if (fileUrl.equals(defaultAvatarObrazName) 
            || fileUrl.equals(defaultRoslinaObrazName) 
            || fileUrl.equals(powiadomieniaAvatarObrazName)) {
            System.out.println("Nie można usunąć domyślnego obrazu");
            return false;
        }

        Path imagePath = Paths.get(fileUrl);
        return deleteFile(imagePath);
    }

    private boolean deleteFile(Path path) {
        if (path == null) {
            System.out.println("Ścieżka usuwania jest null");
            return false;
        }
        try {
            File file = new File(path.toString());
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Plik został usunięty");
                    return true;
                } else {
                    System.out.println("Nie udało się usunąć pliku");
                    return false;
                }
            } else {
                System.out.println("Plik nie istnieje");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas usuwania pliku: " + e.getMessage());
            return false;
        }
    }
}
