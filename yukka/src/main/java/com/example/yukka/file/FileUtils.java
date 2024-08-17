package com.example.yukka.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    

    public  byte[] readRoslinaObrazFile(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }

        if (fileUrl.equals(defaultRoslinaObrazName)) {
            Path imagePath = new File(defaultRoslinaObrazPath).toPath();
            return readFileFromLocation(imagePath);
        }
     //   System.out.println("Seed: " + avatarDefaultObrazPath);
      //  System.out.println("Url: " + fileUrl);
        //System.out.println(": " + avatarDefaultObrazPath);
        Path imagePath = Paths.get(seedRoslinaObrazyPath, fileUrl);
        return readFileFromLocation(imagePath);
    }

    // TODO: implementacja posta czy kiego czegos
    public byte[] readPostObrazFile(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }

        if (fileUrl.equals(defaultRoslinaObrazName)) {
            Path imagePath = Paths.get(defaultRoslinaObrazPath);
            return readFileFromLocation(imagePath);
        }

        Path imagePath = Paths.get(seedRoslinaObrazyPath, fileUrl);
        return readFileFromLocation(imagePath);
    }

    public byte[] readKomentarzObrazFile(String fileUrl) {
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
     //   System.out.println("Seed: " + avatarDefaultObrazPath);
      //  System.out.println("Url: " + fileUrl);
        //System.out.println(": " + avatarDefaultObrazPath);
        Path imagePath = Paths.get(seedRoslinaObrazyPath, fileUrl);
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
            }
           
            else {
                System.out.println("No jest null2");
                return Files.readAllBytes(Paths.get(defaultRoslinaObrazPath));
            }
        } catch (IOException e) {
            log.warn("Nie znaleziono pliku w ścieżce {}", path.toString());
        }
        return null;
    }
}
