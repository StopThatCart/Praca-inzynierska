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
import java.util.Set;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;

import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.repository.DzialkaRepository;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.service.DzialkaService;
import com.example.yukka.model.enums.Wyswietlanie;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;

import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaRepository;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaService;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
import com.example.yukka.model.social.request.OcenaRequest;
import com.example.yukka.model.social.request.PostRequest;
import com.example.yukka.model.social.request.ZgloszenieRequest;
import com.example.yukka.model.social.service.KomentarzService;
import com.example.yukka.model.social.service.PostService;
import com.example.yukka.model.social.service.PowiadomienieService;
import com.example.yukka.model.social.service.RozmowaPrywatnaService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.example.yukka.seeder.RoslinaImporterService;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableAsync
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

	@SuppressWarnings("unused")
    private final UzytkownikRoslinaService uzytkownikRoslinaService;
	@SuppressWarnings("unused")
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
	//private final RoslinaRepository roslinaRepository;
	@SuppressWarnings("unused")
	private final RoslinaImporterService roslinaImporterService;

	private final FileUtils fileUtils;
	@SuppressWarnings("unused")
	private final FileStoreService fileStoreService;

	private final UzytkownikRoslinaSeeder uzytkownikRoslinaSeeder;

	@SuppressWarnings("unused")
	private final PowiadomienieService powiadomienieService;


	//Faker faker = new Faker(new Locale.Builder().setLanguage("pl").setRegion("PL").build());

	private final int MAX_POSTY = 5;
	private final int MAX_KOMENTARZE = 4;
	private final int MAX_ODPOWIEDZI_DEPTH = 2;
	private final int MAX_ODPOWIEDZI = 2;


	String michalEmail = "michal@email.pl";
	String piotrEmail = "piotr@email.pl";
	String katarzynaEmail = "katarzyna@email.pl";

	String niegrzecznyEmail = "bad@email.pl";

	Uzytkownik usJan;
	Uzytkownik usPrac;
	Uzytkownik usPiotr;
	Uzytkownik usKatarzyna;
	Uzytkownik usMichal;

	Uzytkownik usNiegrzeczny;

	List<Uzytkownik> uzytkownicy;

	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(YukkaApplication.class, args);
       
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
			// unseed();
			// seed();
		//roslinaSearchTest();
        };
    }
	
	void unseed() {
		log.info("Czyszczenie bazy danych(oprocz roslin)...");

		log.info("Usuwanie obrazow uzytkownika");
		uzytkownikService.seedRemoveUzytkownicyObrazy();

		log.info("Usuwanie rozmow prywatnych");
		rozmowaPrywatnaRepository.clearRozmowyPrywatne();

		log.info("Usuwanie komentarzy");
		komentarzRepository.clearKomentarze();

		log.info("Usuwanie postow");
		postRepository.clearPosts();

		log.info("Usuwanie powiadomien");
		uzytkownikRepository.clearPowiadomienia();

		log.info("Usuwanie roslin uzytkownikow");
		uzytkownikRepository.clearUzytkownikRoslina();

		log.info("Usuwanie uzytkownikow");
		uzytkownikRepository.clearUzytkowicy();

	}

	void seed() {
		log.info("Seedowanie bazy danych...");

		uzytkownicy = seedUzytkownicy();
		
		addPostyWithKomentarze(uzytkownicy);
		//System.out.println("Dodano posty testowe. Może lepiej dać je potem w batchu.");

		// Path kotPath = Paths.get(obrazSeedPath, "kot.png");
		// MockMultipartFile obraz1 = new MockMultipartFile("tempFileName", "kot.png", 
		// "image/png", fileUtils.readFileFromLocation(kotPath));

		seedRozmowy();

		seedZgloszenia();

		seedDzialka();

	}

	private List<Uzytkownik> seedUzytkownicy() {
		log.info("Seedowanie uzytkownikow...");

		Uzytkownik usJan = Uzytkownik.builder()
		.uzytId("jasiuId")
        .nazwa("Jan Kowalski").email("jan@email.pl")
        .haslo(passwordEncoder.encode("jan12345678"))
		.labels(List.of("Admin", "Pracownik"))
		.aktywowany(true)
        .build();

		Uzytkownik usPrac = Uzytkownik.builder()
		.uzytId("annaJakasId")
		.labels(List.of("Pracownik")).nazwa("Anna Nowak")
		.email("anna@email.pl")
		.haslo(passwordEncoder.encode("anna12345678"))
		.aktywowany(true)
		.build();


		Uzytkownik usPiotr = Uzytkownik.builder()
		.uzytId("piotrekId")
        .nazwa("Piotr Wiśniewski").email(piotrEmail)
        .haslo(passwordEncoder.encode("piotr12345678"))
		.aktywowany(true)
        .build();


		Uzytkownik usKatarzyna = Uzytkownik.builder()
		.uzytId("jakasKatarzynaId")
		.nazwa("Katarzyna Mazur").email(katarzynaEmail)
        .haslo(passwordEncoder.encode("katarzyna12345678"))
		.aktywowany(true)
        .build();


		Uzytkownik usMichal = Uzytkownik.builder()
		.uzytId("michalekId")
        .nazwa("Michał Zieliński").email(michalEmail)
        .haslo(passwordEncoder.encode("michal12345678"))
		.aktywowany(true)
        .build();

		Uzytkownik usNiegrzeczny = Uzytkownik.builder()
		.uzytId("niegrzecznyId")
		.nazwa("Niegrzeczny Użytkownik").email(niegrzecznyEmail)
		.haslo(passwordEncoder.encode("bad12345678"))
		.aktywowany(true)
		.build();

		uzytkownikService.addPracownik(usJan);
		uzytkownikService.addPracownik(usPrac);	
		uzytkownikService.addUzytkownik(usPiotr);
		uzytkownikService.addUzytkownik(usKatarzyna);
		uzytkownikService.addUzytkownik(usMichal);
		uzytkownikService.addUzytkownik(usNiegrzeczny);

		Path adachiPath = Paths.get(obrazSeedPath, "adachi.jpg");
		MockMultipartFile obrazAvatar1 = new MockMultipartFile("tempFileName", "adachi.jpg", 
		"image/png", fileUtils.readFileFromLocation(adachiPath));

		uzytkownikService.updateUzytkownikAvatar(obrazAvatar1, usPiotr);
		

		this.usJan = uzytkownikRepository.findByEmail(usJan.getEmail()).get();
		this.usPrac = uzytkownikRepository.findByEmail(usPrac.getEmail()).get();
		this.usPiotr = uzytkownikRepository.findByEmail(piotrEmail).get();
		this.usKatarzyna = uzytkownikRepository.findByEmail(katarzynaEmail).get();
		this.usMichal = uzytkownikRepository.findByEmail(michalEmail).get();
		this.usNiegrzeczny = uzytkownikRepository.findByEmail(niegrzecznyEmail).get();
		
		this.uzytkownicy = Arrays.asList(this.usJan, this.usPrac, this.usPiotr, this.usKatarzyna, this.usMichal, this.usNiegrzeczny);

		return this.uzytkownicy;
	}

	private void seedZgloszenia() {
		log.info("Seedowanie zgłoszeń...");
		Faker faker = new Faker(new Locale("pl"));
		
		ZgloszenieRequest request = ZgloszenieRequest.builder()
		.zglaszany(usNiegrzeczny.getNazwa())
		.opis(faker.witcher().character())
		.typPowiadomienia(TypPowiadomienia.ZGLOSZENIE.name())
		.build();
		

		powiadomienieService.sendZgloszenie(request, usMichal);
		request.setOpis(faker.witcher().quote());
		powiadomienieService.sendZgloszenie(request, usPiotr);
		request.setOpis(faker.witcher().quote());
		powiadomienieService.sendZgloszenie(request, usKatarzyna);


		log.info("Dodawanie specjalnego powiadomienia...");
		PowiadomienieDTO  pow1 = PowiadomienieDTO.builder()
		.tytul("""
				Uwaga, mam ważny komunikat. Mianowicie, chciałbym poinformować, że jestem bardzo ważny i mam ważne rzeczy do powiedzenia.
				Dodatkowo, zapomniałem, co dokładnie chciałem powiedzieć, ale to nie ma znaczenia, bo i tak jestem ważny.
				Ten komunikat został wygenerowany.
				""").build();
		powiadomienieService.addSpecjalnePowiadomienie(pow1);


	}

	private void seedDzialka() {
		log.info("Seedowanie dzialek...");

		RoslinaResponse ros = roslinaService.findByNazwaLacinska("symphytum grandiflorum'goldsmith'");

		Path peppinoPath = Paths.get(obrazSeedPath, "peppino.png");
		MockMultipartFile obraz2 = new MockMultipartFile("tempFileName", "peppino.png", 
		"image/png", fileUtils.readFileFromLocation(peppinoPath));
		
		DzialkaRoslinaRequest req = DzialkaRoslinaRequest.builder()
		.numerDzialki(1)
		.x(6).y(6)
		//.tabX(new int[] {6, 6, 7})
		//.tabY(new int[] {6, 7, 6})
		.pozycje(Set.of(
			Pozycja.builder().x(6).y(6).build(), 
			Pozycja.builder().x(6).y(7).build(), 
			Pozycja.builder().x(7).y(6).build()
			))
		.kolor("#6c6ef0")
		.wyswietlanie(Wyswietlanie.TEKSTURA_KOLOR.toString())
		.roslinaId(ros.getRoslinaId())
		.build();


		ros = roslinaService.findByNazwaLacinska("taxus baccata'adpressa'");
		DzialkaRoslinaRequest req2 = DzialkaRoslinaRequest.builder()
		.numerDzialki(2)
		.x(12).y(11)
		//.tabX(new int[] {12, 13, 14})
		//.tabY(new int[] {11, 11, 11})
		.pozycje(Set.of(
			Pozycja.builder().x(12).y(11).build(), 
			Pozycja.builder().x(13).y(11).build(), 
			Pozycja.builder().x(14).y(11).build()
			))
		.kolor("#f06ce7")
		.wyswietlanie(Wyswietlanie.KOLOR.toString())
		.roslinaId(ros.getRoslinaId())
		.build();
		
		log.info("Dodawanie rosliny 1 do dzialek");
		dzialkaService.saveRoslinaToDzialka(req, null, null, usPiotr);

		log.info("Dodawanie rosliny 2 do dzialek");
		dzialkaService.saveRoslinaToDzialka(req2, null, null, usPiotr);

		dzialkaRepository.getDzialkaByNumer(usPiotr.getEmail(), 2).get();

		// Rosliny uzytkownika 
		log.info("Seedowanie roslin uzytkownika[TESTOWE]...");

		
		// Wiem wiem, okropieństwo
		PageResponse<RoslinaResponse> roslinyUzytkownika = uzytkownikRoslinaSeeder.seedUzytkownikRosliny(usPiotr);
		if (roslinyUzytkownika.getSize() == 0) {
			throw new RuntimeException("Nie udało się załadować roślin użytkownika.");

		}
		RoslinaResponse roslinaUzyt = roslinyUzytkownika.getContent().get(0);
		


		
        DzialkaRoslinaRequest req3 = DzialkaRoslinaRequest.builder()
		.numerDzialki(2).x(9).y(9)
		.pozycje(Set.of(
			Pozycja.builder().x(9).y(9).build(), 
			Pozycja.builder().x(9).y(10).build(), 
			Pozycja.builder().x(10).y(9).build(),
			Pozycja.builder().x(10).y(10).build()
			))
		.kolor("#ebf06c")
		.wyswietlanie(Wyswietlanie.TEKSTURA_KOLOR.toString())
		.roslinaId(roslinaUzyt.getRoslinaId())
		.build();

		dzialkaService.saveRoslinaToDzialka(req3, null, null, usPiotr);

		log.info("Aktualizacja obrazu rosliny w dzialce");
		dzialkaService.updateRoslinaObrazInDzialka(req2, obraz2, null, usPiotr);

		MoveRoslinaRequest moveRequest1 = MoveRoslinaRequest.builder()
		.numerDzialki(2)
		.x(12).y(11)
		.xNowy(3).yNowy(4)
		//.tabX(new int[] {3, 4, 5})
		//.tabY(new int[] {4, 4, 4})
		.pozycje(Set.of(
			Pozycja.builder().x(3).y(4).build(), 
			Pozycja.builder().x(4).y(4).build(), 
			Pozycja.builder().x(5).y(4).build()
			))
		.build();


		// Na razie nie jest to kompatybilne z planowaną funkcjonalością
		// MoveRoslinaRequest moveRequest2 = MoveRoslinaRequest.builder()
		// .numerDzialki(2)
		// .numerDzialkiNowy(1)
		// .x(3).y(4)
		// .xNowy(7).yNowy(7)
		// .build();

		log.info("Zmienianie pozycji rosliny w dzialce");
		dzialkaService.updateRoslinaPozycjaInDzialka(moveRequest1, usPiotr);

		//System.out.println("Zmienianie pozycji rośliny w działce do nowej działki");
		//dzialkaService.updateRoslinaPositionInDzialka(moveRequest2, usPiotr);



		// To samo ale dla anny
		ros = roslinaService.findByNazwaLacinska("vaccinium corymbosum'alvar'");
		DzialkaRoslinaRequest reqAna = DzialkaRoslinaRequest.builder()
		.numerDzialki(2)
		.x(1).y(4)
		//.tabX(new int[] {12, 13, 14})
		//.tabY(new int[] {11, 11, 11})
		.pozycje(Set.of(
			Pozycja.builder().x(1).y(4).build(), 
			Pozycja.builder().x(2).y(4).build(), 
			Pozycja.builder().x(1).y(3).build()
			))
		.kolor("#1ba626")
		.wyswietlanie(Wyswietlanie.KOLOR.toString())
		.roslinaId(ros.getRoslinaId())
		.build();
		
		log.info("Dodawanie rosliny dla anny do dzialek");
		dzialkaService.saveRoslinaToDzialka(reqAna, null, null, usPrac);
	}

	private void seedRozmowy() {
		log.info("Seedowanie rozmow prywatnych...");
		// Zaproszenie do rozmowy prywatnej
		rozmowaPrywatnaRepository
		.inviteToRozmowaPrywatna(usKatarzyna.getUzytId(), usPiotr.getUzytId(), LocalDateTime.now());

		// Akceptacja rozmowy prywatnej
		rozmowaPrywatnaRepository.acceptRozmowaPrywatna(usPiotr.getUzytId(), usKatarzyna.getUzytId());

		log.info("Dodawanie komentarzy do rozmowy prywatnej");
		for (int i = 0; i < 10; i++) {
			KomentarzRequest piotrWiad = KomentarzRequest.builder().opis("Wiadomość od Piotra " + i).targetId(usKatarzyna.getNazwa()).build();
			KomentarzRequest katarzynaWiad = KomentarzRequest.builder().opis("Wiadomość od Katarzyny " + i).targetId(usPiotr.getNazwa()).build();
			komentarzService.addKomentarzToWiadomoscPrywatna(piotrWiad, null, usPiotr);
			komentarzService.addKomentarzToWiadomoscPrywatna(katarzynaWiad, null, usKatarzyna);
		}

		// Dodanie paru zaproszeń
		rozmowaPrywatnaService.inviteToRozmowaPrywatna(usKatarzyna.getNazwa(), usJan);
		rozmowaPrywatnaService.inviteToRozmowaPrywatna(usMichal.getNazwa(), usKatarzyna);
	}


	private void addPostyWithKomentarze(List<Uzytkownik> uzytkownicy) {
		log.info("Dodawanie postów testowych...");
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
				byte[] fileContent = fileUtils.readFileFromLocation(cheezyPath);
				if (fileContent != null) {
					MockMultipartFile obrazPost = new MockMultipartFile("tempFileName", "cheezy.jpg", 
					"image/png", fileUtils.readFileFromLocation(cheezyPath));
					post = postService.save(postReq, obrazPost, uzytkownicy.get(rand));
				} 
			} else {
				post = postService.save(postReq, null, uzytkownicy.get(rand));
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

					byte[] fileContent = fileUtils.readFileFromLocation(kotPath);
					if (fileContent == null) {
						kom = komentarzService.addKomentarzToPost(komReq, null, uzytkownicy.get(rand));
					} else {
						MockMultipartFile obrazKom = new MockMultipartFile("tempFileName", "kot.png", 
						"image/png", fileUtils.readFileFromLocation(kotPath));
						kom = komentarzService.addKomentarzToPost(komReq, obrazKom, uzytkownicy.get(rand));
					}
				} else {
					kom = komentarzService.addKomentarzToPost(komReq, null, uzytkownicy.get(rand));
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

					byte[] fileContent = fileUtils.readFileFromLocation(kotPath);
					if (fileContent == null) {
						nowaOdp = komentarzService.addOdpowiedzToKomentarz(odp, null, uzytkownicy.get(rand));
					} else {
						MockMultipartFile obrazKom = new MockMultipartFile("tempFileName", "kot.png", 
					"image/png", fileUtils.readFileFromLocation(kotPath));
						nowaOdp = komentarzService.addOdpowiedzToKomentarz(odp, obrazKom, uzytkownicy.get(rand));
					}
					
				} else {
					nowaOdp = komentarzService.addOdpowiedzToKomentarz(odp, null, uzytkownicy.get(rand));
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

	private void roslinaSearchTest() {
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
