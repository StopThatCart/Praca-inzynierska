package com.example.yukka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.yukka.seeder.GeneralSeederService;
import com.example.yukka.seeder.rosliny.RoslinaImporterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Główna klasa aplikacji Yukka.
 * 
 * 
 * <strong>Metody:</strong>
 * <ul>
 * <li><strong>main</strong>: Główna metoda uruchamiająca aplikację.</li>
 * <li><strong>seedDatabase</strong>: Bean do seedowania bazy danych.</li>
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
