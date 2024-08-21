package com.example.yukka;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.ocpsoft.prettytime.PrettyTime;
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
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.service.KomentarzService;
import com.example.yukka.model.social.service.PostService;
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
	private final PostService postService;
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
		Post p1 = Post.builder()
        .postId(postId1)
        .tytul("Jakiś post1")
        .opis("Jakiś postowy opis1")
        .build();

		String postId2 = UUID.randomUUID().toString();
		Post p2 = Post.builder()
        .postId(postId2)
        .tytul("Jakiś post2")
        .opis("Jakiś postowy opis2")
        .build();

		String postId3 = UUID.randomUUID().toString();
		Post p3 = Post.builder()
        .postId(postId3)
        .tytul("Jakiś post3")
        .opis("Jakiś postowy opis3")
        .build();

		// Dodawanie postów
		postRepository.addPost(michalEmail, p1);
		postRepository.addPost(michalEmail, p2);
		postRepository.addPost(michalEmail, p3);
	
		// Dodawanie ocen do postów
		postRepository.addOcenaToPost(katarzynaEmail, postId1, false);
		postRepository.addOcenaToPost(piotrEmail, postId1, false);
		postRepository.addOcenaToPost(katarzynaEmail, postId2, true);
		postRepository.addOcenaToPost(piotrEmail, postId2, true);
		postRepository.addOcenaToPost(katarzynaEmail, postId3, true);
		postRepository.addOcenaToPost(piotrEmail, postId3, false);
	
		// Komentarze
		String komId1 = UUID.randomUUID().toString();
		String komId2 = UUID.randomUUID().toString();
		String komId3 = UUID.randomUUID().toString();
		String komId4 = UUID.randomUUID().toString();
		String komId5 = UUID.randomUUID().toString();

		Komentarz k1 = Komentarz.builder().komentarzId(komId1).opis("Piotr opis").build();
		Komentarz k2 = Komentarz.builder().komentarzId(komId2).opis("Kata opis").build();
		Komentarz k3 = Komentarz.builder().komentarzId(komId2 + "dasd").opis("Kata opis innego posta").build();
		Komentarz k4 = Komentarz.builder().komentarzId(komId3).opis("Piotr2 opis").build();
		Komentarz k5 = Komentarz.builder().komentarzId(komId4).opis("Kata2 opis").build();
		Komentarz k6 = Komentarz.builder().komentarzId(komId5).opis("Kata3 opis").build();

		//komentarzRepository.addKomentarzToPost(piotrEmail, postId3, k1);
	///	komentarzRepository.addKomentarzToPost(katarzynaEmail, postId3, k2);

		//komentarzRepository.addKomentarzToKomentarz(piotrEmail, k4, komId2);
	//	komentarzRepository.addKomentarzToKomentarz(katarzynaEmail, k5, komId2);
	//	komentarzRepository.addKomentarzToKomentarz(katarzynaEmail, k6, komId3);

	//	komentarzRepository.addKomentarzToPost(katarzynaEmail, postId2, k3);
		// To się da do service potem
		//komentarzRepository.updateKomentarzeCountInPost(postId3);

		// komentarzRepository.addKomentarzToPost(postId1, k10);
		// komentarzRepository.addKomentarzToPost(postId2, k11);
		// komentarzRepository.addKomentarzToPost(postId3, k12);

		// To samo, tylko daje powiadomienia użytkownikom
		
		Komentarz kom1 = komentarzService.addKomentarzToPost(KomentarzRequest.builder().opis("Komentarz do posta 1 od Piotra").targetId(postId1).build(), usPiotr);
		Komentarz kom2 = komentarzService.addKomentarzToPost(KomentarzRequest.builder().opis("Komentarz do posta 3 od Katarzyny").targetId(postId3).build(), usKatarzyna);
		//System.out.println("Komentarz 2: " + kom2.toString());
		Komentarz kom3 = komentarzService.addKomentarzToPost(KomentarzRequest.builder().opis("Komentarz do posta 3 od Piotra").targetId(postId3).build(), usPiotr);

		Komentarz kom4 = komentarzService.addOdpowiedzToKomentarz(KomentarzRequest.builder().opis("Piotr2 opis").targetId(kom2.getKomentarzId()).build(), usPiotr);
		Komentarz kom5 = komentarzService.addOdpowiedzToKomentarz(KomentarzRequest.builder().opis("Kata2 opis").targetId(kom2.getKomentarzId()).build(), usKatarzyna);
		Komentarz kom6 = komentarzService.addOdpowiedzToKomentarz(KomentarzRequest.builder().opis("Kata3 opis").targetId(kom5.getKomentarzId()).build(), usKatarzyna);

		Komentarz kom7 = komentarzService.addOdpowiedzToKomentarz(KomentarzRequest.builder().opis("Michał opis").targetId(kom4.getKomentarzId()).build(), usMichal);

				


		// Ocenianie
	//	System.out.println("\n\n\nocenia1\n\n\n\n\n\n");
		//komentarzRepository.addOcenaToKomentarz(piotrEmail, komId2, false);
	//	System.out.println("ocenia2");
		komentarzRepository.addOcenaToKomentarz(katarzynaEmail, kom1.getKomentarzId(), true);
		komentarzRepository.addOcenaToKomentarz(katarzynaEmail, kom3.getKomentarzId(), false);
		komentarzRepository.addOcenaToKomentarz(katarzynaEmail, kom4.getKomentarzId(), false);
		komentarzRepository.addOcenaToKomentarz(katarzynaEmail, kom7.getKomentarzId(), false);

	//	System.out.println("ocenia3");
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom1.getKomentarzId(), true);
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom2.getKomentarzId(), false);
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom3.getKomentarzId(), true); 
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom4.getKomentarzId(), false); 
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom5.getKomentarzId(), true);
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom6.getKomentarzId(), true);

		komentarzRepository.addOcenaToKomentarz(piotrEmail, kom2.getKomentarzId(), false);
		komentarzRepository.addOcenaToKomentarz(piotrEmail, kom5.getKomentarzId(), false);
		 
		//komentarzRepository.addOcenaToKomentarz(piotrEmail, komId5, true); 

		// Usunięcie oceny
		komentarzRepository.addOcenaToKomentarz(michalEmail, kom3.getKomentarzId(), false);
		//komentarzRepository.removeOcenaFromKomentarz(michalEmail, kom3.getKomentarzId());

		// Rozmowy prywatne

		// Initialize UUIDs for Komentarz IDs
		String komId7 = UUID.randomUUID().toString();
		String komId8 = UUID.randomUUID().toString();
		String komId9 = UUID.randomUUID().toString();
		String komId10 = UUID.randomUUID().toString();

		// Do testów
		Uzytkownik piotr = uzytkownikService.findByEmail(piotrEmail);
		Uzytkownik katarzyna = uzytkownikService.findByEmail(katarzynaEmail);

		// Zaproszenie do rozmowy prywatnej
		RozmowaPrywatna rozmowa1 = rozmowaPrywatnaRepository.inviteToRozmowaPrywatna(usKatarzyna.getUzytId(), usPiotr.getUzytId());

		// Akceptacja rozmowy prywatnej
		RozmowaPrywatna meh = rozmowaPrywatnaRepository.acceptRozmowaPrywatna(piotr.getUzytId(), katarzyna.getUzytId());
		//System.out.println("Meh: " + meh.toString());
		//rozmowaPrywatnaService.acceptRozmowaPrywatnaNoPunjabi(katarzyna.getNazwa(), piotr);

		// Dodanie komentarzy do rozmowy prywatnej
		
		//Komentarz k7 = Komentarz.builder().komentarzId(komId7).opis("Wiadomość od Piotra").build();
	//	Komentarz k8 = Komentarz.builder().komentarzId(komId8).opis("Wiadomość od Katarzyny").build();
		//Komentarz k9 = Komentarz.builder().komentarzId(komId9).opis("Kolejna wiadomość od Piotra").build();

		// komentarzRepository.addKomentarzToRozmowaPrywatna(katarzyna.getNazwa(), piotr.getNazwa(), k7);
		// komentarzRepository.addKomentarzToRozmowaPrywatna(piotr.getNazwa(), katarzyna.getNazwa(), k8);
		// komentarzRepository.addKomentarzToRozmowaPrywatna(katarzyna.getNazwa(), piotr.getNazwa(), k9);

		// To samo, ale service wysyła powiadomienia

		System.out.println("Dodawanie komentarzy do rozmowy prywatnej");
		komentarzService.addKomentarzToWiadomoscPrywatna(katarzyna.getNazwa(), KomentarzRequest.builder().opis("Wiadomość od Piotra").targetId(komId7).build(), usPiotr);
		komentarzService.addKomentarzToWiadomoscPrywatna(piotr.getNazwa(), KomentarzRequest.builder().opis("Wiadomość od Katarzyny").targetId(komId8).build(), usKatarzyna);
		komentarzService.addKomentarzToWiadomoscPrywatna(katarzyna.getNazwa(), KomentarzRequest.builder().opis("Kolejna wiadomość od Piotra").targetId(komId9).build(), usPiotr);


		//log.info("\nąŚwiętość\n");
		// To wyrzuci IllegalArgumentException
		//OcenaRequest ocenaDoWiadomosciPrywatnej = OcenaRequest.builder().ocenialnyId(k3.getKomentarzId()).lubi(false).build();
		//komentarzService.addOcenaToKomentarzTest(ocenaDoWiadomosciPrywatnej, katarzyna);

		LocalDateTime what = LocalDateTime.now();

		//System.out.println("Czas: " + timeAgo(what));
		//testPrettyTime();

		// Testowanie usuwanka
		System.out.println("Usuwanie komentarzy");
		komentarzService.deleteKomentarzFromPost(postId3, kom4.getKomentarzId(), usJan);

		System.out.println("Usuwanie posta");
		postService.deletePost(postId3, usJan);
	}

	public static String timeAgo(LocalDateTime dateTime) {
        PrettyTime p = new PrettyTime(Locale.forLanguageTag("pl"));
        return p.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

	public void testPrettyTime() {
		PrettyTime prettyTime = new PrettyTime(Locale.forLanguageTag("pl"));

        LocalDateTime now = LocalDateTime.now();

        // Przykłady różnych dat
        System.out.println(prettyTime.format(Date.from(now.minusSeconds(30).atZone(ZoneId.systemDefault()).toInstant())));
        System.out.println(prettyTime.format(Date.from(now.minusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()))); 
        System.out.println(prettyTime.format(Date.from(now.minusHours(1).atZone(ZoneId.systemDefault()).toInstant())));  
        System.out.println(prettyTime.format(Date.from(now.minusDays(1).atZone(ZoneId.systemDefault()).toInstant())));  
        System.out.println(prettyTime.format(Date.from(now.minusDays(3).atZone(ZoneId.systemDefault()).toInstant()))); 
        System.out.println(prettyTime.format(Date.from(now.minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant()))); 
        System.out.println(prettyTime.format(Date.from(now.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant()))); 
        System.out.println(prettyTime.format(Date.from(now.minusYears(1).atZone(ZoneId.systemDefault()).toInstant()))); 
	}
}
