package com.example.yukka;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.repository.DzialkaRepository;
import com.example.yukka.model.dzialka.service.DzialkaService;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaRepository;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaService;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.request.PostRequest;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.service.KomentarzService;
import com.example.yukka.model.social.service.PostService;
import com.example.yukka.model.social.service.PowiadomienieService;
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
	// Potrzebne żeby funkcje Scheduled nie działały przed załadowaniem aplikacji
	private static boolean isApplicationReady = false;

	@Value("${obraz.seed.path}")
    private String obrazSeedPath;

	private final PasswordEncoder passwordEncoder;
	private final UzytkownikRepository uzytkownikRepository;
	private final UzytkownikService uzytkownikService;

	private final UzytkownikRoslinaService uzytkownikRoslinaService;
	private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;

	private final PostRepository postRepository;
	private final PostService postService;
	private final KomentarzRepository komentarzRepository;
	private final RozmowaPrywatnaService rozmowaPrywatnaService;
	private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;
	private final KomentarzService komentarzService;

	private final DzialkaService dzialkaService;
	private final DzialkaRepository dzialkaRepository;

	private final RoslinaService roslinaService;
	private final RoslinaImporterService roslinaImporterService;

	private final FileUtils fileUtils;
	private final FileStoreService fileStoreService;

	private final UzytkownikRoslinaSeeder uzytkownikRoslinaSeeder;

	private final PowiadomienieService powiadomienieService;


	//Faker faker = new Faker(new Locale.Builder().setLanguage("pl").setRegion("PL").build());

	private final int MAX_POSTY = 30;
	private final int MAX_KOMENTARZE = 4;
	private final int MAX_ODPOWIEDZI_DEPTH = 2;
	private final int MAX_ODPOWIEDZI = 2;
	


	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(YukkaApplication.class, args);
       
		// PythonPlantSeeder scriptRunner = context.getBean(PythonPlantSeeder.class);
       // String scriptOutput = scriptRunner.runPythonScript();
       // System.out.println(scriptOutput);
	   isApplicationReady = true;


	}

	public static boolean isApplicationReady() {
        return isApplicationReady;
    }

	@Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
			//roslinaImporterService.seedRosliny();
			unseed();
        	seed();
		//roslinaSearchTest();
        };
    }
	
	void roslinaSearchTest() {
		String roslinaNazwa = "a";
		String roslinaNazwaLacinska = "";
        String roslinaOpis = "";
        Double wysokoscMin = 1.5;
        Double wysokoscMax = 12.0;


		WlasciwoscWithRelations kolorLisci = WlasciwoscWithRelations.builder()
		.etykieta("Kolor").relacja("MA_KOLOR_LISCI").nazwa("ciemnozielone")
		.build();
		WlasciwoscWithRelations okresOwocowania = WlasciwoscWithRelations.builder()
		.etykieta("Okres").relacja("MA_OKRES_OWOCOWANIA").nazwa("październik")
		.build();

		WlasciwoscWithRelations gleba1 = WlasciwoscWithRelations.builder()
		.etykieta("Gleba").relacja("MA_GLEBE").nazwa("przeciętna ogrodowa")
		.build();
		WlasciwoscWithRelations gleba2 = WlasciwoscWithRelations.builder()
		.etykieta("Gleba").relacja("MA_GLEBE").nazwa("próchniczna")
		.build();
		RoslinaRequest exampleRoslina = RoslinaRequest.builder()
            //.id(12345678L)
            .nazwa(roslinaNazwa)
			//.nazwaLacinska(roslinaNazwaLacinska)
           // .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
			.wlasciwosci(Arrays.asList(kolorLisci, okresOwocowania, gleba1, gleba2))
			.build();

		System.out.println("Testowanie wyszukiwania rośliny z parametrami");
		PageResponse<RoslinaResponse> res = roslinaService.findAllRoslinyWithParameters(0, 12, exampleRoslina);
		if(res.getContent().isEmpty()) {
			System.out.println("Nie znaleziono rośliny");
		} else {
			System.out.println("Znaleziono pierwszą roślinę: " + res.getContent().get(0).getNazwa());
			System.out.println("Liczba znalezionych roślin: " + res.getContent().size());
		}

	}

	void unseed() {
		System.out.println("Czyszczenie bazy danych...");

		System.out.println("Usuwanie obrazów użytkownika");
		uzytkownikService.seedRemoveUzytkownicyObrazy();

		System.out.println("Usuwanie rozmów prywatnych");
		rozmowaPrywatnaRepository.clearRozmowyPrywatne();

		System.out.println("Usuwanie komentarzy");
		komentarzRepository.clearKomentarze();

		System.out.println("Usuwanie postów");
		postRepository.clearPosts();

		System.out.println("Usuwanie powiadomień");
		uzytkownikRepository.clearPowiadomienia();

		System.out.println("Usuwanie roślin użytkowników");
		uzytkownikRepository.clearUzytkownikRoslina();

		System.out.println("Usuwanie użytkowników");
		uzytkownikRepository.clearUzytkowicy();





		

	}

	void seed() {
		System.out.println("Seedowanie bazy danych...");
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


		Path adachiPath = Paths.get(obrazSeedPath, "adachi.jpg");
		MockMultipartFile obrazAvatar1 = new MockMultipartFile("tempFileName", "adachi.jpg", 
		"image/png", fileUtils.readFileFromLocation(adachiPath));

		uzytkownikService.updateUzytkownikAvatar(obrazAvatar1, usPiotr);
		

		usJan = uzytkownikRepository.findByEmail(usJan.getEmail()).get();
		usPrac = uzytkownikRepository.findByEmail(usPrac.getEmail()).get();
		usPiotr = uzytkownikRepository.findByEmail(piotrEmail).get();
		usKatarzyna = uzytkownikRepository.findByEmail(katarzynaEmail).get();
		usMichal = uzytkownikRepository.findByEmail(michalEmail).get();
		
		List<Uzytkownik> uzytkownicy = Arrays.asList(usPiotr, usKatarzyna, usMichal, usJan, usPrac);
		

		// Dodawanie parę zwykłych postów do sprawdzania paginacji
		System.out.println("Dodawanie postów testowych...");
		//addPostyWithKomentarze(uzytkownicy);
		System.out.println("Dodano posty testowe. Może lepiej dać je potem w batchu.");


		Path kotPath = Paths.get(obrazSeedPath, "kot.png");
		MockMultipartFile obraz1 = new MockMultipartFile("tempFileName", "kot.png", 
		"image/png", fileUtils.readFileFromLocation(kotPath));

		Path peppinoPath = Paths.get(obrazSeedPath, "peppino.png");
		MockMultipartFile obraz2 = new MockMultipartFile("tempFileName", "peppino.png", 
		"image/png", fileUtils.readFileFromLocation(peppinoPath));

		// Zaproszenie do rozmowy prywatnej
		RozmowaPrywatna rozmowa1 = rozmowaPrywatnaRepository
		.inviteToRozmowaPrywatna(usKatarzyna.getUzytId(), usPiotr.getUzytId(), LocalDateTime.now());

		// Akceptacja rozmowy prywatnej
		RozmowaPrywatna meh = rozmowaPrywatnaRepository.acceptRozmowaPrywatna(usPiotr.getUzytId(), usKatarzyna.getUzytId());

		System.out.println("Dodawanie komentarzy do rozmowy prywatnej");
		for (int i = 0; i < 10; i++) {
			komentarzService.addKomentarzToWiadomoscPrywatna(KomentarzRequest.builder().opis("Wiadomość od Piotra " + i).targetId(usKatarzyna.getNazwa()).build(), usPiotr);
			komentarzService.addKomentarzToWiadomoscPrywatna(KomentarzRequest.builder().opis("Wiadomość od Katarzyny " + i).targetId(usPiotr.getNazwa()).build(), usKatarzyna);
		}

		// Dodanie paru zaproszeń
		rozmowaPrywatnaService.inviteToRozmowaPrywatna(usKatarzyna.getNazwa(), usJan);
		rozmowaPrywatnaService.inviteToRozmowaPrywatna(usMichal.getNazwa(), usKatarzyna);

		PowiadomienieDTO  pow1 = PowiadomienieDTO.builder()
		.tytul("""
				Uwaga, mam ważny komunikat. Mianowicie, chciałbym poinformować, że jestem bardzo ważny i mam ważne rzeczy do powiedzenia.
				Dodatkowo, zapomniałem, co dokładnie chciałem powiedzieć, ale to nie ma znaczenia, bo i tak jestem ważny.
				Ten komunikat został wygenerowany.
				""").build();

		powiadomienieService.addSpecjalnePowiadomienie(pow1);

		DzialkaRoslinaRequest req = DzialkaRoslinaRequest.builder()
		.numerDzialki(1).x(1).y(1)
		.nazwaLacinska("symphytum grandiflorum'goldsmith'")
		.build();

		DzialkaRoslinaRequest req2 = DzialkaRoslinaRequest.builder()
		.numerDzialki(2).x(1).y(1)
		.nazwaLacinska("taxus baccata'adpressa'")
		.build();

		
		System.out.println("Dodawanie rośliny 1 do działek");
		dzialkaService.saveRoslinaToDzialka(req, usPiotr);

		System.out.println("Dodawanie rośliny 2 do działek");
		dzialkaService.saveRoslinaToDzialka(req2, usPiotr);

		// Wywala exception i słusznie
		//dzialkaService.saveRoslinaToDzialka(req3, usPiotr);

		Dzialka piotrDzialka2 = dzialkaRepository.getDzialkaByNumer(usPiotr.getEmail(), 2).get();

		//System.out.println("Dzialka 2: " + piotrDzialka2.toString());

		// Rosliny uzytkownika 
		System.out.println("Seedowanie roślin użytkownika");
		uzytkownikRoslinaSeeder.seedUzytkownikRosliny(usPiotr);

		System.out.println("Aktualizacja obrazu rośliny w działce");
		dzialkaService.updateRoslinaObrazInDzialka(req2, obraz2, usPiotr);

		MoveRoslinaRequest moveRequest1 = MoveRoslinaRequest.builder()
		.numerDzialkiStary(2)
		.xStary(1).yStary(1)
		.xNowy(3).yNowy(4)
		.build();

		MoveRoslinaRequest moveRequest2 = MoveRoslinaRequest.builder()
		.numerDzialkiStary(2)
		.numerDzialkiNowy(1)
		.xStary(3).yStary(4)
		.xNowy(7).yNowy(7)
		.build();

		System.out.println("Zmienianie pozycji rośliny w działce");
		dzialkaService.updateRoslinaPositionInDzialka(moveRequest1, usPiotr);

		System.out.println("Zmienianie pozycji rośliny w działce do nowej działki");
		dzialkaService.updateRoslinaPositionInDzialka(moveRequest2, usPiotr);

	}

	private void addPostyWithKomentarze(List<Uzytkownik> uzytkownicy) {
		for(int i = 0; i < MAX_POSTY; i++) {
			Post post = null;
			PostRequest postReq = PostRequest.builder()
				.tytul("Jakiś post testowy" + i)
				.opis("Jakiś postowy testowy opis" + i)
				.build();

			int rand = (int)(Math.random() * uzytkownicy.size());

			// Losowe dodawanie obrazka z postem
			if (Math.random() < 0.5) {
				Path cheezyPath = Paths.get(obrazSeedPath, "cheezy.jpg");
				MockMultipartFile obrazPost = new MockMultipartFile("tempFileName", "cheezy.jpg", 
					"image/png", fileUtils.readFileFromLocation(cheezyPath));
				try {
					post = postService.save(postReq, obrazPost, uzytkownicy.get(rand));
				} catch (FileUploadException e) {}
			} else {
				post = postService.save(postReq, uzytkownicy.get(rand));
			}
			
			// Dodawanie ocen do postów
			Collections.shuffle(uzytkownicy);
			int liczbaOcen = (int) (Math.random() * uzytkownicy.size()) + 1;
			for (int j = 0; j < liczbaOcen; j++) {
				boolean ocena = Math.random() < 0.5;
				if (post != null) {
					postRepository.addOcenaToPost(uzytkownicy.get(j).getEmail(), post.getPostId(), ocena);
				}
			}

			// Dodawanie losowej ilości komentarzy do postu
			Random random = new Random();
			int liczbaKomentarzy = random.nextInt(MAX_KOMENTARZE) + 1; 

			for (int j = 0; j < liczbaKomentarzy; j++) {
				KomentarzRequest komReq = KomentarzRequest.builder()
					.targetId(post != null ? post.getPostId() : null)
					.opis("Jakiś komentarz " + j)
					.build();

				rand = (int)(Math.random() * uzytkownicy.size());

				Komentarz kom = null;
				if (Math.random() < 0.5) {
					Path kotPath = Paths.get(obrazSeedPath, "kot.png");
					MockMultipartFile obrazKom = new MockMultipartFile("tempFileName", "kot.png", 
					"image/png", fileUtils.readFileFromLocation(kotPath));
					try {
						kom = komentarzService.addKomentarzToPost(komReq, obrazKom, uzytkownicy.get(rand));
					} catch (FileUploadException e) {}
				} else {
					kom = komentarzService.addKomentarzToPost(komReq, uzytkownicy.get(rand));
				}

				// Dodawanie losowej ilości odpowiedzi do komentarza
				addOdpowiedziRekursywne(kom, uzytkownicy, random, 1);

				// Dodawanie ocen do komentarzy
				addOcenyToKomentarz(kom, uzytkownicy);
			}
		}
	}

	private void addOdpowiedziRekursywne(Komentarz kom, List<Uzytkownik> uzytkownicy, Random random, int depth) {
		if (depth > MAX_ODPOWIEDZI_DEPTH) return;
	
		int liczbaOdpowiedzi = random.nextInt(MAX_ODPOWIEDZI) + 1;
		Collections.shuffle(uzytkownicy);
	
		for (int k = 0; k < liczbaOdpowiedzi; k++) {
			KomentarzRequest odp = KomentarzRequest.builder()
				.targetId(kom.getKomentarzId())
				.opis("Jakaś odpowiedź " + k)
				.build();
	
			int rand = (int) (Math.random() * uzytkownicy.size());

			Komentarz nowaOdp = null;
				if (Math.random() < 0.5) {
					Path kotPath = Paths.get(obrazSeedPath, "kot.png");
					MockMultipartFile obrazKom = new MockMultipartFile("tempFileName", "kot.png", 
					"image/png", fileUtils.readFileFromLocation(kotPath));
					try {
						nowaOdp = komentarzService.addOdpowiedzToKomentarz(odp, obrazKom, uzytkownicy.get(rand));
					} catch (FileUploadException e) {}
				} else {
					nowaOdp = komentarzService.addOdpowiedzToKomentarz(odp, uzytkownicy.get(rand));
				}
	
			addOdpowiedziRekursywne(nowaOdp, uzytkownicy, random, depth + 1);
			addOcenyToKomentarz(nowaOdp, uzytkownicy);
		}
	}

	private void addOcenyToKomentarz(Komentarz kom, List<Uzytkownik> uzytkownicy) {
		int liczbaOcen = (int) (Math.random() * uzytkownicy.size()) + 1;
		Collections.shuffle(uzytkownicy);
	
		for (int j = 0; j < liczbaOcen; j++) {
			OcenaRequest ocenaReq = OcenaRequest.builder()
				.ocenialnyId(kom.getKomentarzId())
				.lubi(Math.random() < 0.5)
				.build();
	
			// Na wypadek, gdyby użytkownik próbował ocenić własny komentarz
			try {
				komentarzService.addOcenaToKomentarz(ocenaReq, uzytkownicy.get(j));
			} catch (IllegalArgumentException e) {}
		}
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
