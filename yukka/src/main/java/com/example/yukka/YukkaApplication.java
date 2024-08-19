package com.example.yukka;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.service.KomentarzService;
import com.example.yukka.model.social.service.RozmowaPrywatnaService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.example.yukka.seeder.RoslinaImporterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class YukkaApplication {

	private final PasswordEncoder passwordEncoder;
	private final UzytkownikRepository uzytkownikRepository;
	private final UzytkownikService uzytkownikService;

	private final PostRepository postRepository;
	private final KomentarzRepository komentarzRepository;
	private final RozmowaPrywatnaService rozmowaPrywatnaService;
	private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;
	private final KomentarzService komentarzService;



	private final RoslinaImporterService roslinaImporterService;


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
			roslinaImporterService.seedRosliny();
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

		//Ustawienia ust = Ustawienia.builder().build();

		//MaUstawienia maUst = MaUstawienia.builder().ustawienia(ust).build();

		Uzytkownik usJan = Uzytkownik.builder()
		.uzytId("jasiuId")
        .nazwa("Jan Kowalski").email("jan@email.pl")
        .haslo(passwordEncoder.encode("jan12345678"))
		.labels(List.of("Admin"))
        .build();

		Uzytkownik usPrac = Uzytkownik.builder()
		.uzytId("annaJakasId")
		.labels(List.of("Pracownik")).nazwa("Anna Nowak")
		.email("anna@email.pl")
		.haslo(passwordEncoder.encode("anna12345678"))
		.build();

		String piotrEmail = "piotr@email.pl";
		Uzytkownik usPiotr = Uzytkownik.builder()
		.uzytId("piotrekId")
        .nazwa("Piotr Wiśniewski").email(piotrEmail)
        .haslo(passwordEncoder.encode("piotr12345678"))
        .build();

		String katarzynaEmail = "katarzyna@email.pl";
		Uzytkownik usKatarzyna = Uzytkownik.builder()
		.uzytId("jakasKatarzynaId")
		.nazwa("Katarzyna Mazur").email(katarzynaEmail)
        .haslo(passwordEncoder.encode("katarzyna12345678"))
        .build();

		String michalEmail = "michal@email.pl";
		Uzytkownik usMichal = Uzytkownik.builder()
		.uzytId("michalekId")
        .nazwa("Michał Zieliński").email(michalEmail)
        .haslo(passwordEncoder.encode("michal12345678"))
        .build();

		uzytkownikService.addPracownik(usJan);
		uzytkownikService.addPracownik(usPrac);	

		uzytkownikService.addUzytkownik(usPiotr);
		uzytkownikService.addUzytkownik(usKatarzyna);
		uzytkownikService.addUzytkownik(usMichal);
		
		
		String postId1 = UUID.randomUUID().toString();
		Post p1 = Post.builder().postId(postId1).tytul("Jakiś post1").opis("Jakiś postowy opis1").build();

		String postId2 = UUID.randomUUID().toString();
		Post p2 = Post.builder().postId(postId2).tytul("Jakiś post2").opis("Jakiś postowy opis2").build();

		String postId3= UUID.randomUUID().toString();
		Post p3 = Post.builder().postId(postId3).tytul("Jakiś post3").opis("Jakiś postowy opis3").build();

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

		Komentarz k1 = Komentarz.builder().komentarzId(komId1).opis("Piotr opis").build();
		Komentarz k2 = Komentarz.builder().komentarzId(komId2).opis("Kata opis").build();
		Komentarz k22 = Komentarz.builder().komentarzId(komId2).opis("Kata opis innego posta").build();
		Komentarz k3 = Komentarz.builder().komentarzId(komId3).opis("Piotr2 opis").build();
		Komentarz k4 = Komentarz.builder().komentarzId(komId4).opis("Kata2 opis").build();
		Komentarz k5 = Komentarz.builder().komentarzId(komId5).opis("Kata3 opis").build();

		komentarzRepository.addKomentarzToPost(piotrEmail, postId3, k1);
		komentarzRepository.addKomentarzToPost(katarzynaEmail, postId3, k2);
		komentarzRepository.addKomentarzToKomentarz(piotrEmail, k3, komId2);
		komentarzRepository.addKomentarzToKomentarz(katarzynaEmail, k4, komId2);
		komentarzRepository.addKomentarzToKomentarz(katarzynaEmail, k5, komId3);

		komentarzRepository.addKomentarzToPost(katarzynaEmail, postId2, k22);
		// To się da do service potem
		komentarzRepository.updateKomentarzeCountInPost(postId3);


		// Rozmowy prywatne

		// Initialize UUIDs for Komentarz IDs
		String komId6 = UUID.randomUUID().toString();
		String komId7 = UUID.randomUUID().toString();
		String komId8 = UUID.randomUUID().toString();
		String komId9 = UUID.randomUUID().toString();

		// Retrieve Uzytkownik objects
		Uzytkownik piotr = uzytkownikService.findByEmail(piotrEmail);
		Uzytkownik katarzyna = uzytkownikService.findByEmail(katarzynaEmail);

		// Add Private Conversation
		RozmowaPrywatna rozmowa1 = rozmowaPrywatnaService.inviteToRozmowaPrywatnaNoPunjabi(katarzyna.getUzytId(), piotr);
		//rozmowaPrywatnaRepository.saveRozmowaPrywatna(katarzyna.getNazwa(), piotr.getNazwa());
		//RozmowaPrywatna rozmowa2 = rozmowaPrywatnaService.inviteToRozmowaPrywatnaNoPunjabi(piotr.getNazwa(), katarzyna);

		// Accept Private Conversation
		RozmowaPrywatna meh = rozmowaPrywatnaService.acceptRozmowaPrywatnaNoPunjabi(piotr.getUzytId(), katarzyna);
		System.out.println("Meh: " + meh.toString());
		//rozmowaPrywatnaService.acceptRozmowaPrywatnaNoPunjabi(katarzyna.getNazwa(), piotr);

		// Add Komentarz to Private Conversation
		
		Komentarz k6 = Komentarz.builder().komentarzId(komId6).komentarzId(komId6).opis("Wiadomość od Piotra").build();
		Komentarz k7 = Komentarz.builder().komentarzId(komId7).komentarzId(komId7).opis("Wiadomość od Katarzyny").build();

		komentarzRepository.addKomentarzToRozmowaPrywatna(katarzyna.getNazwa(), piotr.getNazwa(), k6);
		komentarzRepository.addKomentarzToRozmowaPrywatna(piotr.getNazwa(), katarzyna.getNazwa(), k7);

	}

}
