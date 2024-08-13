package com.example.yukka;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.yukka.model.post.Komentarz;
import com.example.yukka.model.post.Post;
import com.example.yukka.model.post.controller.KomentarzRepository;
import com.example.yukka.model.post.controller.PostRepository;
import com.example.yukka.model.uzytkownik.MaUstawienia;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class YukkaApplication {

	
	private final UzytkownikRepository uzytkownikRepository;
	private final UzytkownikService uzytkownikService;

	private final PostRepository postRepository;
	private final KomentarzRepository komentarzRepository;


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
		postRepository.clearPosts();
		komentarzRepository.clearKomentarze();
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

		String piotrEmail = "piotr@email.pl";
		Uzytkownik usPiotr = Uzytkownik.builder()
        .nazwa("Piotr Wiśniewski").email(piotrEmail)
        .haslo("piotr12345678")
        .build();

		String katarzynaEmail = "katarzyna@email.pl";
		Uzytkownik usKatarzyna = Uzytkownik.builder()
		.nazwa("Katarzyna Mazur").email(katarzynaEmail)
        .haslo("katarzyna12345678")
        .build();

		String michalEmail = "michal@email.pl";
		Uzytkownik usMichal = Uzytkownik.builder()
        .nazwa("Michał Zieliński").email(michalEmail)
        .haslo("michal12345678")
        .build();

		uzytkownikService.addPracownik(usJan);
		uzytkownikService.addPracownik(usPrac);	

		uzytkownikService.addUzytkownik(usPiotr);
		uzytkownikService.addUzytkownik(usKatarzyna);
		uzytkownikService.addUzytkownik(usMichal);
		
		

		// TODO: Sprawdź w serwice, czy id nie istnieje w bazie
		String postId1 = UUID.randomUUID().toString();
		Post p1 = Post.builder().postId(postId1).tytul("Jakiś tytuł").opis("Jakiś opis").build();

		String postId2 = UUID.randomUUID().toString();
		Post p2 = Post.builder().postId(postId2).tytul("Jakiś tytuł").opis("Jakiś opis").build();

		String postId3= UUID.randomUUID().toString();
		Post p3 = Post.builder().postId(postId3).tytul("Jakiś tytuł").opis("Jakiś opis").build();

		postRepository.addPost(michalEmail, p1);
		postRepository.addPost(michalEmail, p2);
		postRepository.addPost(michalEmail, p3);
		
		postRepository.addOcenaToPost(katarzynaEmail, postId1, false);
		postRepository.addOcenaToPost(piotrEmail, postId1, false);

		postRepository.addOcenaToPost(katarzynaEmail, postId2, true);
		postRepository.addOcenaToPost(piotrEmail, postId2, true);

		postRepository.addOcenaToPost(katarzynaEmail, postId3, true);
		postRepository.addOcenaToPost(piotrEmail, postId3, false);

		String komId1 = UUID.randomUUID().toString();
		String komId2 = UUID.randomUUID().toString();
		String komId3 = UUID.randomUUID().toString();
		String komId4 = UUID.randomUUID().toString();
		String komId5 = UUID.randomUUID().toString();

		Komentarz k1 = Komentarz.builder().komentarzId(komId1).opis("Jakiś opis").build();
		Komentarz k2 = Komentarz.builder().komentarzId(komId2).opis("Jakiś opis").build();
		Komentarz k3 = Komentarz.builder().komentarzId(komId3).opis("Jakiś opis").build();
		Komentarz k4 = Komentarz.builder().komentarzId(komId4).opis("Jakiś opis").build();
		Komentarz k5 = Komentarz.builder().komentarzId(komId5).opis("Jakiś opis").build();

		komentarzRepository.addKomentarzToPost(piotrEmail, postId3, k1);
		komentarzRepository.addKomentarzToPost(katarzynaEmail, postId3, k2);

		komentarzRepository.addKomentarzToKomentarz(piotrEmail, k3, komId2);
		komentarzRepository.addKomentarzToKomentarz(katarzynaEmail, k4, komId2);
		komentarzRepository.addKomentarzToKomentarz(katarzynaEmail, k5, komId3);

		// To się da do service potem
		komentarzRepository.updateKomentarzeCountInPost(postId3);


	}

}
