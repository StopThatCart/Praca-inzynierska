package com.example.yukka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.yukka.seeder.GeneralSeederService;
import com.example.yukka.seeder.RoslinaImporterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Główna klasa aplikacji Yukka.
 * 
 * 
 * <strong>Metody:</strong>
 * <ul>
 * <li><strong>main</strong>: Główna metoda uruchamiająca aplikację.</li>
 * <li><strong>isApplicationReady</strong>: Sprawdza, czy aplikacja jest gotowa.</li>
 * <li><strong>seedDatabase</strong>: Bean do seedowania bazy danych.</li>
 * <li><strong>unseed</strong>: Usuwa dane z bazy danych.</li>
 * <li><strong>seed</strong>: Seeduje bazę danych.</li>
 * <li><strong>seedUzytkownicy</strong>: Seeduje użytkowników.</li>
 * <li><strong>seedZgloszenia</strong>: Seeduje zgłoszenia.</li>
 * <li><strong>seedDzialka</strong>: Seeduje działki.</li>
 * <li><strong>seedRozmowy</strong>: Seeduje rozmowy prywatne.</li>
 * <li><strong>addPostyWithKomentarze</strong>: Dodaje posty z komentarzami.</li>
 * <li><strong>addOdpowiedziRekursywne</strong>: Dodaje odpowiedzi do komentarzy rekurencyjnie.</li>
 * <li><strong>addOcenyToKomentarz</strong>: Dodaje oceny do komentarzy.</li>
 * <li><strong>roslinaSearchTest</strong>: Testuje wyszukiwanie roślin.</li>
 * <li><strong>timeAgo</strong>: Zwraca czas w formacie "time ago".</li>
 * <li><strong>testPrettyTime</strong>: Testuje PrettyTime.</li>
 * </ul>
 */
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class YukkaApplication {
	private final RoslinaImporterService roslinaImporterService;
	private final GeneralSeederService generalSeeder;
	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(YukkaApplication.class, args);
	}


    @Value("${seed.database}")
    private boolean seedDatabase;

	/**
	 * Metoda seedDatabase tworzy i zwraca CommandLineRunner, który jest uruchamiany przy starcie aplikacji.
	 * 
	 * @return CommandLineRunner, który loguje tryb seedowania i, jeśli jest włączone seedowanie bazy danych,
	 *         wykonuje operacje seedowania i unseedowania. Na razie pomija rośliny, gdyż jest ich dużo.
	 */
	@Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
			if (seedDatabase) {
                log.info("Seedowanie bazy danych...");
				//roslinaImporterService.seedRosliny();
				// generalSeeder.unseed();
				// generalSeeder.seed();
				
            }
        };
    }
}
