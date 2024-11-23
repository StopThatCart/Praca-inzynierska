package com.example.yukka.seeder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoslinaImporterService {
    @Value("${spring.data.neo4j.database}")
    private String dbName;

    @Value("${spring.data.neo4j.seed-mode}")
    private String seedMode;
    
    @Value("${application.seeder.csv-file-path}")
    private String CSV_FILE;

    
    @Value("${roslina.seed.obrazy-path}")
    private String seedObrazyPath;

    @Value("${roslina.obraz.default.name}")
    private String defaultObrazName;

    @Value("${roslina.obraz.default.jpg-file-path}")
    private String defaultObrazPath;

    //private final RoslinaMapper roslinaMapper;
    private final RoslinaService roslinaService;
    
    private final Neo4jHealthCheck healthCheck;

    private final String emptyCsvValue = "Brak";

    @Value("${roslina.seed.amount}")
    private int limit;

    @SuppressWarnings("deprecation")
    public void seedRosliny() {
        try {
            if(seedMode.equals("create")){
                System.out.println("Creating database");
                healthCheck.dropAndCreateDatabase(dbName);
                List<RoslinaRequest> bep = parseCsvToRoslinaRequests(CSV_FILE, limit);
                if (healthCheck.isItActuallyAvailable()) {
                 //   System.out.println("Installing trigger");
                  // healthCheck.installTriggers();
                    System.out.println("Importing rosliny");
                    importRoslinyIntoDatabase(bep);
                }
            }else{
              //  if(!healthCheck.checkIfDatabaseIsPopulated()) {
                //    healthCheck.dropAndCreateDatabase(dbName);
               //     if (healthCheck.isItActuallyAvailable()) {
               //         importRoslinyIntoDatabase(bep);
               //     }
             //   }
            }
        } catch (IOException | CsvException e) {
            log.error("Error processing CSV file", e);
        }
       
    }

    public void importRoslinyIntoDatabase(List<RoslinaRequest> requests) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Importowanie roślin...");
        int number = 1;
        for(RoslinaRequest request : requests) {
            if(number % 500 == 0) {
                log.info("Aktualna roślina: ["+number+"]");
            }

            MultipartFile file = loadImageFile(request.getObraz());
            if(file == null) {
                log.error("Nie można było znaleźć obrazu " + request.getObraz());
                return;
            }
            roslinaService.saveSeededRoslina(request, file);
           // roslinaService.save(request);
            number++;
        }

        log.info("Zakończono importowanie roślin.");
        stopWatch.stop();
        log.info("Czas trwania importu: " + stopWatch.getTotalTimeSeconds() + " ms");
    }

    private MultipartFile loadImageFile(String obraz) {
        // Zbudowanie ścieżki do pliku obrazu na podstawie nazwy łacińskiej rośliny
        String imagePath;
        if(obraz.equals(defaultObrazName)) {
            imagePath = Paths.get(defaultObrazPath).toString();
        }else {
            imagePath = Paths.get(seedObrazyPath, obraz).toString();
        }
        
        File imageFile = new File(imagePath);

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
                log.error("Nie udało się załadować pliku obrazu: " + imagePath, e);
            }
        } else {
            log.warn("Plik obrazu nie znaleziony: " + imagePath);
        }
    return null;
    }

    public List<RoslinaRequest> parseCsvToRoslinaRequests(String filePath, int limit) throws IOException, CsvException {
        List<RoslinaRequest> roslinaRequests = new ArrayList<>();
        if(limit <= 0) {
            limit = Integer.MAX_VALUE;
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();

            if (rows.isEmpty()) {
                return roslinaRequests;
            }

            // Przeczytaj pierwszy wiersz, aby uzyskać nagłówki
            String[] headers = rows.get(0);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i], i);
            }

            // Pomijanie pierwszego wiersza z nagłówkami
            for (int i = 1; i < rows.size(); i++) {
                if(i > limit){
                    break;
                }
                String[] row = rows.get(i);

                Wysokosc heights = HeightProcessor.getWysokosc(row[headerMap.get("docelowa_wysokosc")]);

            RoslinaRequest roslinaRequest = RoslinaRequest.builder()
                    .nazwa(row[headerMap.get("name")])
                    .nazwaLacinska(row[headerMap.get("latin_name")])
                    .opis(row[headerMap.get("opis")])
                    .wysokoscMin(heights.getMin())
                    .wysokoscMax(heights.getMax())
                    .obraz(row[headerMap.get("image_filename")])
                    .wlasciwosci(parseWlasciwosci(row, headerMap))
                    .build();

                roslinaRequests.add(roslinaRequest);
            }
        }

        return roslinaRequests;
    }

    private List<WlasciwoscWithRelations> parseWlasciwosci(String[] row, Map<String, Integer> headerMap) {
        List<WlasciwoscWithRelations> wlasciwosci = new ArrayList<>();

        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.GRUPA.getBackendValue(), row[headerMap.get("grupa_roslin")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.PODGRUPA.getBackendValue(), row[headerMap.get("grupa_uzytkowa")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.FORMA.getBackendValue(), row[headerMap.get("forma")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.SILA_WZROSTU.getBackendValue(), row[headerMap.get("sila_wzrostu")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.POKROJ.getBackendValue(), row[headerMap.get("pokroj")]);
        addWlasciwosc(wlasciwosci, "KolorLisci", row[headerMap.get("barwa_lisci_(igiel)")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.ZIMOZIELONOSC.getBackendValue(), row[headerMap.get("zimozielonosc_lisci_(igiel)")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.KWIAT.getBackendValue(), row[headerMap.get("rodzaj_kwiatow")]);
        addWlasciwosc(wlasciwosci, "OkresKwitnienia", row[headerMap.get("pora_kwitnienia")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.OWOC.getBackendValue(), row[headerMap.get("owoce")]);
        addWlasciwosc(wlasciwosci, "OkresOwocowania", row[headerMap.get("pora_owocowania")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.STANOWISKO.getBackendValue(), row[headerMap.get("naslonecznienie")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.WILGOTNOSC.getBackendValue(), row[headerMap.get("wilgotnosc")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.ODCZYN.getBackendValue(), row[headerMap.get("ph_podloza")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.GLEBA.getBackendValue(), row[headerMap.get("rodzaj_gleby")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.WALOR.getBackendValue(), row[headerMap.get("walory")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.ZASTOSOWANIE.getBackendValue(), row[headerMap.get("zastosowanie")]);
        addWlasciwosc(wlasciwosci, RoslinaEtykietyFrontend.ZASTOSOWANIE.getBackendValue(), row[headerMap.get("zastosowanie")]);
        addWlasciwosc(wlasciwosci, "KolorKwiatow", row[headerMap.get("barwa_kwiatow")]);
         //  addWlasciwosc(wlasciwosci, "Nagroda", row[23]);
    
        return wlasciwosci;
    }

    private void addWlasciwosc(List<WlasciwoscWithRelations> wlasciwosci, String label, String value) {
        if (value != null && !value.isEmpty() && !value.toLowerCase().equals(emptyCsvValue.toLowerCase()) 
        && label != null && !label.isEmpty() &&  !label.toLowerCase().equals(emptyCsvValue.toLowerCase())) {
           
            String meh = label;
            if(meh.contains("Okres")) {
                label = "Okres";
            }else if(meh.contains("Kolor")) {
                label = "Kolor";
            }

            // Regex to match values with commas inside parentheses
            Pattern pattern = Pattern.compile("([^,]+\\([^\\)]+\\))|([^,]+)");
            Matcher matcher = pattern.matcher(value);

            while (matcher.find()) {
                String matchedValue = matcher.group();
                // Add the matched value to the list
                wlasciwosci.add(new WlasciwoscWithRelations(label, matchedValue.trim(), determineRelacja(meh)));
            }

            // String[] parts = value.split(",");
            // for (String part : parts) {
            //     part = part.trim();
            //     if (!part.isEmpty()) {
            //         WlasciwoscWithRelations wlasciwosc = new WlasciwoscWithRelations(label, part, determineRelacja(meh));

            //    //     wlasciwosc.put("labels", label);
            //    //     wlasciwosc.put("nazwa",  part);
            //    //     wlasciwosc.put("relacja", determineRelacja(meh));
            //         wlasciwosci.add(wlasciwosc);
            //        }
            // }

        }
    }

    // 15
    private String determineRelacja(String label) {
        return switch (label) {
            case "Grupa" -> "MA_GRUPE";
            case "Podgrupa" -> "MA_PODGRUPE";
            case "Forma" -> "MA_FORME";
            case "Kwiat" -> "MA_KWIAT";
            //
            case "KolorKwiatow" -> "MA_KOLOR_KWIATOW";
            case "SilaWzrostu" -> "MA_SILE_WZROSTU";
            case "Pokroj" -> "MA_POKROJ";
            //
            case "KolorLisci" -> "MA_KOLOR_LISCI";
            case "Zimozielonosc" -> "MA_ZIMOZIELONOSC_LISCI";
            case "Stanowisko" -> "MA_STANOWISKO";
            case "Wilgotnosc" -> "MA_WILGOTNOSC";
            case "Odczyn" -> "MA_ODCZYNY";
            case "Gleba" -> "MA_GLEBE";
            //
            case "OkresKwitnienia" -> "MA_OKRES_KWITNIENIA";
            case "Owoc" -> "MA_OWOC";
            //
            case "OkresOwocowania" -> "MA_OKRES_OWOCOWANIA";
            case "Walor" -> "MA_WALOR";
            case "Zastosowanie" -> "MA_ZASTOSOWANIE";
           // case "Nagroda" -> "MA_NAGRODE";
            default -> "";
        };
    }

}
