package com.example.yukka;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.yukka.model.uzytkownik.MaUstawienia;
import com.example.yukka.model.uzytkownik.UserDetailsServiceImpl;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class YukkaApplication {

	
	private final UzytkownikRepository uzytkownikRepository;
	private final UserDetailsServiceImpl uzytkownikService;


	//Faker faker = new Faker(new Locale.Builder().setLanguage("pl").setRegion("PL").build());
	


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(YukkaApplication.class, args);
       
		// PythonPlantSeeder scriptRunner = context.getBean(PythonPlantSeeder.class);
       // String scriptOutput = scriptRunner.runPythonScript();
       // System.out.println(scriptOutput);
	   
	}

	@Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
			unseed();
            seed();
        };
    }

	void unseed() {
		uzytkownikRepository.clearUzytkowicy();
	}

	void seed() {

		// Użytkownicy na razie bez relacji

		Ustawienia ust = Ustawienia.builder().build();

		MaUstawienia maUst = MaUstawienia.builder().ustawienia(ust).build();

		Uzytkownik usJan = Uzytkownik.builder()
        .nazwa("Jan Kowalski").email("jan@email.pl")
        .haslo("jan12345678").labels(List.of("Admin"))
        .build();

		Uzytkownik usPrac = Uzytkownik.builder()
		.labels(List.of("Pracownik")).nazwa("Anna Nowak")
		.email("anna@email.pl").haslo("anna12345678")
		.build();

		Uzytkownik usPiotr = Uzytkownik.builder()
        .nazwa("Piotr Wiśniewski").email("piotr@email.pl")
        .haslo("piotr12345678")
        .build();

		Uzytkownik usKatarzyna = Uzytkownik.builder()
		.nazwa("Katarzyna Mazur").email("katarzyna@email.pl")
        .haslo("katarzyna12345678")
        .build();

		Uzytkownik usMichal = Uzytkownik.builder()
        .nazwa("Michał Zieliński").email("michal@email.pl")
        .haslo("michal12345678")
        .build();

		uzytkownikService.addPracownik(usJan);
		uzytkownikService.addPracownik(usPrac);	

		uzytkownikService.addUzytkownik(usPiotr);
		uzytkownikService.addUzytkownik(usKatarzyna);
		uzytkownikService.addUzytkownik(usMichal);
		
	}

}
