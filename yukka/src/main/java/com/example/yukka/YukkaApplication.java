package com.example.yukka;

import java.util.Locale;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.yukka.model.uzytkownik.UzytkownikRepository;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class YukkaApplication {

	
	private final UzytkownikRepository uzytkownikRepository;


	Faker faker = new Faker(new Locale.Builder().setLanguage("pl").setRegion("PL").build());
	
	private static final Logger LOGGER = Logger.getLogger(YukkaApplication.class.getName());

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(YukkaApplication.class, args);
       
		// PythonPlantSeeder scriptRunner = context.getBean(PythonPlantSeeder.class);
       // String scriptOutput = scriptRunner.runPythonScript();
       // System.out.println(scriptOutput);
	   
	}

	@Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
           // seed();
        };
    }

	void seed() {

		// Użytkownicy na razie bez relacji

		uzytkownikRepository.addNewAdmin("Jan Kowalski", "jan@email.pl", "jan12345678");
		uzytkownikRepository.addNewPracownik("Anna Nowak", "anna@email.pl", "anna12345678");
		uzytkownikRepository.addNewUzytkownik("Piotr Wiśniewski", "piotr@email.pl", "piotr12345678");
		uzytkownikRepository.addNewUzytkownik("Katarzyna Mazur", "katarzyna@email.pl", "katarzyna12345678");
		uzytkownikRepository.addNewUzytkownik("Michał Zieliński", "michal@email.pl", "michal12345678");
		uzytkownikRepository.addNewUzytkownik("Aleksandra Kwiatkowska", "aleksandra@email.pl", "aleksandra12345678");
		
	}

}
