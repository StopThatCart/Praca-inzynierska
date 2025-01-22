package com.example.yukka.seeder.rosliny;

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
import com.example.yukka.model.roslina.cecha.CechaWithRelations;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
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

    @Value("${seed.database}")
    private boolean seedDatabase;
    
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
    
    private final Neo4jSeeder neo4jSeeder;

    private final String emptyCsvValue = "Brak";

    @Value("${roslina.seed.amount}")
    private int limit;

  
    /**
     * Metoda seedRosliny służy do inicjalizacji bazy danych roślin.
     * Metoda wykonuje następujące kroki:
     * <ul>
     *   <li>Jeśli tryb to "create", tworzy nową bazę danych i importuje dane z pliku CSV</li>
     *   <li>Jeśli baza danych jest dostępna, importuje dane roślin do bazy</li>
     *   <li>W przypadku błędów odczytu pliku CSV, loguje odpowiedni komunikat błędu</li>
     * </ul>
     * 
     * @throws IOException w przypadku błędów odczytu pliku CSV
     * @throws CsvException w przypadku błędów parsowania pliku CSV
     */
    public void seedRosliny() {
        try {
            if(seedDatabase){
                log.info("Tworzenie bazy danych...");
                neo4jSeeder.dropAndCreateDatabase(dbName);
                List<RoslinaRequest> bep = parseCsvToRoslinaRequests(CSV_FILE, limit);
                if (neo4jSeeder.isItActuallyAvailable()) {
                    neo4jSeeder.installConstraints();
                    importRoslinyIntoDatabase(bep);
                }
            }
        } catch (IOException | CsvException e) {
            log.error("Błąd podczas odczytywania pliku " + CSV_FILE + ".csv", e);
        }
       
    }

    /**
     * Importuje listę roślin do bazy danych.
     *
     * @param requests lista obiektów typu <strong>RoslinaRequest</strong> zawierających dane roślin do zaimportowania
     *
     * <ul>
     *   <li><strong>StopWatch stopWatch</strong> - mierzy czas trwania importu</li>
     *   <li><strong>log.info("Importowanie roślin...")</strong> - loguje rozpoczęcie procesu importu</li>
     *   <li><strong>int number</strong> - licznik przetworzonych roślin</li>
     *   <li><strong>if(number % 500 == 0)</strong> - loguje co 500 przetworzonych roślin</li>
     *   <li><strong>MultipartFile file</strong> - plik obrazu rośliny</li>
     *   <li><strong>if(file == null)</strong> - loguje błąd, jeśli obraz nie został znaleziony</li>
     *   <li><strong>roslinaService.saveSeededRoslina(request, file)</strong> - zapisuje roślinę do bazy danych</li>
     *   <li><strong>log.info("Zakończono importowanie roślin.")</strong> - loguje zakończenie procesu importu</li>
     *   <li><strong>log.info("Czas trwania importu: " + stopWatch.getTotalTimeSeconds() + " ms")</strong> - loguje czas trwania importu</li>
     * </ul>
     */
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
            number++;
        }

        log.info("Zakończono importowanie roślin.");
        stopWatch.stop();
        log.info("Czas trwania importu: " + stopWatch.getTotalTimeSeconds() + " ms");
    }

    /**
     * Ładuje plik obrazu na podstawie nazwy pliku.
     *
     * @param obraz nazwa pliku obrazu
     * @return <ul>
     *             <li><strong>MultipartFile</strong> - załadowany plik obrazu jako MultipartFile, jeśli plik istnieje</li>
     *             <li><strong>null</strong> - jeśli plik obrazu nie został znaleziony lub wystąpił błąd podczas ładowania</li>
     *         </ul>
     */
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

                if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
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

    /**
     * Parsuje plik CSV do listy obiektów RoslinaRequest.
     *
     * @param filePath <strong>Ścieżka</strong> do pliku CSV.
     * @param limit <strong>Limit</strong> liczby wierszy do przetworzenia. Jeśli wartość jest mniejsza lub równa 0, przetwarzane są wszystkie wiersze.
     * @return <ul>
     *             <li>Lista obiektów <strong>RoslinaRequest</strong> utworzonych na podstawie danych z pliku CSV.</li>
     *         </ul>
     * @throws IOException <strong>Wyjątek</strong> zgłaszany w przypadku problemów z odczytem pliku.
     * @throws CsvException <strong>Wyjątek</strong> zgłaszany w przypadku problemów z przetwarzaniem pliku CSV.
     */
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
                    .cechy(parseCechy(row, headerMap))
                    .build();

                roslinaRequests.add(roslinaRequest);
            }
        }

        return roslinaRequests;
    }

    /**
     * Parsuje cechy rośliny z wiersza danych wejściowych.
     *
     * @param row       Tablica Stringów reprezentująca wiersz danych wejściowych.
     * @param headerMap Mapa nagłówków, gdzie kluczem jest nazwa kolumny, a wartością indeks kolumny w tablicy row.
     * @return Lista obiektów CechaWithRelations zawierająca cechy rośliny.
     *
     * Właściwości rośliny:
     * <ul>
     *   <li><strong>Grupa</strong>: grupa_roslin</li>
     *   <li><strong>Podgrupa</strong>: grupa_uzytkowa</li>
     *   <li><strong>Forma</strong>: forma</li>
     *   <li><strong>Siła wzrostu</strong>: sila_wzrostu</li>
     *   <li><strong>Pokrój</strong>: pokroj</li>
     *   <li><strong>Kolor liści</strong>: barwa_lisci_(igiel)</li>
     *   <li><strong>Zimozieloność</strong>: zimozielonosc_lisci_(igiel)</li>
     *   <li><strong>Kwiat</strong>: rodzaj_kwiatow</li>
     *   <li><strong>Okres kwitnienia</strong>: pora_kwitnienia</li>
     *   <li><strong>Owoc</strong>: owoce</li>
     *   <li><strong>Okres owocowania</strong>: pora_owocowania</li>
     *   <li><strong>Stanowisko</strong>: naslonecznienie</li>
     *   <li><strong>Wilgotność</strong>: wilgotnosc</li>
     *   <li><strong>Odczyn</strong>: ph_podloza</li>
     *   <li><strong>Gleba</strong>: rodzaj_gleby</li>
     *   <li><strong>Walor</strong>: walory</li>
     *   <li><strong>Zastosowanie</strong>: zastosowanie</li>
     *   <li><strong>Kolor kwiatów</strong>: barwa_kwiatow</li>
     * </ul>
     */
    private List<CechaWithRelations> parseCechy(String[] row, Map<String, Integer> headerMap) {
        List<CechaWithRelations> cechy = new ArrayList<>();

        addCecha(cechy, RoslinaEtykietyFrontend.GRUPA.getBackendValue(), row[headerMap.get("grupa_roslin")]);
        addCecha(cechy, RoslinaEtykietyFrontend.PODGRUPA.getBackendValue(), row[headerMap.get("grupa_uzytkowa")]);
        addCecha(cechy, RoslinaEtykietyFrontend.FORMA.getBackendValue(), row[headerMap.get("forma")]);
        addCecha(cechy, RoslinaEtykietyFrontend.SILA_WZROSTU.getBackendValue(), row[headerMap.get("sila_wzrostu")]);
        addCecha(cechy, RoslinaEtykietyFrontend.POKROJ.getBackendValue(), row[headerMap.get("pokroj")]);
        addCecha(cechy, "KolorLisci", row[headerMap.get("barwa_lisci_(igiel)")]);
        addCecha(cechy, RoslinaEtykietyFrontend.ZIMOZIELONOSC.getBackendValue(), row[headerMap.get("zimozielonosc_lisci_(igiel)")]);
        addCecha(cechy, RoslinaEtykietyFrontend.KWIAT.getBackendValue(), row[headerMap.get("rodzaj_kwiatow")]);
        addCecha(cechy, "OkresKwitnienia", row[headerMap.get("pora_kwitnienia")]);
        addCecha(cechy, RoslinaEtykietyFrontend.OWOC.getBackendValue(), row[headerMap.get("owoce")]);
        addCecha(cechy, "OkresOwocowania", row[headerMap.get("pora_owocowania")]);
        addCecha(cechy, RoslinaEtykietyFrontend.STANOWISKO.getBackendValue(), row[headerMap.get("naslonecznienie")]);
        addCecha(cechy, RoslinaEtykietyFrontend.WILGOTNOSC.getBackendValue(), row[headerMap.get("wilgotnosc")]);
        addCecha(cechy, RoslinaEtykietyFrontend.ODCZYN.getBackendValue(), row[headerMap.get("ph_podloza")]);
        addCecha(cechy, RoslinaEtykietyFrontend.GLEBA.getBackendValue(), row[headerMap.get("rodzaj_gleby")]);
        addCecha(cechy, RoslinaEtykietyFrontend.WALOR.getBackendValue(), row[headerMap.get("walory")]);
        addCecha(cechy, RoslinaEtykietyFrontend.ZASTOSOWANIE.getBackendValue(), row[headerMap.get("zastosowanie")]);
        addCecha(cechy, RoslinaEtykietyFrontend.ZASTOSOWANIE.getBackendValue(), row[headerMap.get("zastosowanie")]);
        addCecha(cechy, "KolorKwiatow", row[headerMap.get("barwa_kwiatow")]);
    
        return cechy;
    }

    /**
     * Dodaje właściwość do listy cech, jeśli podana etykieta i wartość są prawidłowe.
     *
     * @param cechy lista cech, do której dodawana jest nowa właściwość
     * @param label etykieta cechy
     * @param value wartość cechy
     */
    private void addCecha(List<CechaWithRelations> cechy, String label, String value) {
        if (value != null && !value.isEmpty() && !value.toLowerCase().equals(emptyCsvValue.toLowerCase()) 
        && label != null && !label.isEmpty() &&  !label.toLowerCase().equals(emptyCsvValue.toLowerCase())) {
           
            String meh = label;
            if(meh.contains("Okres")) {
                label = RoslinaEtykietyFrontend.OKRES_KWITNIENIA.toString();
            }else if(meh.contains("Kolor")) {
                label = RoslinaEtykietyFrontend.KOLOR_KWIATOW.toString();
            }

            // Regex to match values with commas inside parentheses
            Pattern pattern = Pattern.compile("([^,]+\\([^\\)]+\\))|([^,]+)");
            Matcher matcher = pattern.matcher(value);

            while (matcher.find()) {
                String matchedValue = matcher.group();
                cechy.add(new CechaWithRelations(label, matchedValue.trim(), determineRelacja(meh)));
            }
        }
    }

    // 15

    /**
     * Metoda determineRelacja przyjmuje etykietę jako parametr i zwraca odpowiadającą jej relację.
     * 
     * @param label etykieta, dla której ma zostać określona relacja
     * @return odpowiadająca relacja lub pusty string, jeśli etykieta nie jest rozpoznana
     * 
     * Możliwe wartości etykiet i odpowiadające im relacje:
     * <ul>
     *   <li><strong>Grupa</strong> -> MA_GRUPE</li>
     *   <li><strong>Podgrupa</strong> -> MA_PODGRUPE</li>
     *   <li><strong>Forma</strong> -> MA_FORME</li>
     *   <li><strong>Kwiat</strong> -> MA_KWIAT</li>
     *   <li><strong>KolorKwiatow</strong> -> MA_KOLOR_KWIATOW</li>
     *   <li><strong>SilaWzrostu</strong> -> MA_SILE_WZROSTU</li>
     *   <li><strong>Pokroj</strong> -> MA_POKROJ</li>
     *   <li><strong>KolorLisci</strong> -> MA_KOLOR_LISCI</li>
     *   <li><strong>Zimozielonosc</strong> -> MA_ZIMOZIELONOSC_LISCI</li>
     *   <li><strong>Stanowisko</strong> -> MA_STANOWISKO</li>
     *   <li><strong>Wilgotnosc</strong> -> MA_WILGOTNOSC</li>
     *   <li><strong>Odczyn</strong> -> MA_ODCZYN</li>
     *   <li><strong>Gleba</strong> -> MA_GLEBE</li>
     *   <li><strong>OkresKwitnienia</strong> -> MA_OKRES_KWITNIENIA</li>
     *   <li><strong>Owoc</strong> -> MA_OWOC</li>
     *   <li><strong>OkresOwocowania</strong> -> MA_OKRES_OWOCOWANIA</li>
     *   <li><strong>Walor</strong> -> MA_WALOR</li>
     *   <li><strong>Zastosowanie</strong> -> MA_ZASTOSOWANIE</li>
     * </ul>
     */
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
            case "Odczyn" -> "MA_ODCZYN";
            case "Gleba" -> "MA_GLEBE";
            //
            case "OkresKwitnienia" -> "MA_OKRES_KWITNIENIA";
            case "Owoc" -> "MA_OWOC";
            //
            case "OkresOwocowania" -> "MA_OKRES_OWOCOWANIA";
            case "Walor" -> "MA_WALOR";
            case "Zastosowanie" -> "MA_ZASTOSOWANIE";
            default -> "";
        };
    }

}
