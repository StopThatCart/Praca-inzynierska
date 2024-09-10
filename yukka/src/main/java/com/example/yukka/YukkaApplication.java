package com.example.yukka;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.repository.PostRepository;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.request.KomentarzRequest;
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
		uzytkownikService.seedRemoveUzytkownicyObrazy();
		uzytkownikRepository.clearUzytkowicy();

		//komentarzService.seedRemoveKomentarzeObrazy();
		komentarzRepository.clearKomentarze();
		//postService.seedRemovePostyObrazy();
		postRepository.clearPosts();
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


		Path adachiPath = Paths.get(obrazSeedPath, "adachi.jpg");
		MockMultipartFile obrazAvatar1 = new MockMultipartFile("tempFileName", "adachi.jpg", 
		"image/png", fileUtils.readFileFromLocation(adachiPath));

		uzytkownikService.updateUzytkownikAvatar(obrazAvatar1, usPiotr);
		
		
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

		PostRequest postReq1 = PostRequest.builder().tytul("Jakiś post3").opis("Jakiś postowy opis3").build();

		Path cheezyPath = Paths.get(obrazSeedPath, "cheezy.jpg");
		MockMultipartFile obrazPost1 = new MockMultipartFile("tempFileName", "cheezy.jpg", 
		"image/png", fileUtils.readFileFromLocation(cheezyPath));

		// Dodawanie postów
		postRepository.addPost(michalEmail, p1);
		postRepository.addPost(michalEmail, p2);

		//
		try {
			p3 = postService.save(postReq1, obrazPost1, usMichal);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		//postRepository.addPost(michalEmail, p3);

		// Dodawanie parę zwykłych postów do sprawdzania paginacji
		System.out.println("Dodawanie postów testowych...");
		for(int i = 0; i < 2; i++) {
			Post post = Post.builder()
			.postId(postService.createPostId())
			.tytul("Jakiś post testowy " + i)
			.opis("Jakiś postowy testowy opis " + i)
			.build();
			int rand = (int)(Math.random() * 3);
			switch(rand) {
				case 0:
					postRepository.addPost(piotrEmail, post);
					break;
				case 1:
					postRepository.addPost(katarzynaEmail, post);
					break;
				case 2:
					postRepository.addPost(michalEmail, post);
					break;
			}
		}
		System.out.println("Dodano posty testowe. Może lepiej dać je potem w batchu.");

	
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


		
		System.out.println("Dodawanie komentarzy do postów");

		Komentarz kom1 = komentarzService.addKomentarzToPost(KomentarzRequest.builder().opis("Komentarz do posta 1 od Piotra").targetId(p1.getPostId()).build(), usPiotr);
		Komentarz kom2 = komentarzService.addKomentarzToPost(KomentarzRequest.builder().opis("Komentarz do posta 3 od Katarzyny").targetId(p3.getPostId()).build(), usKatarzyna);
		//System.out.println("Komentarz 2: " + kom2.toString());
		Komentarz kom3 = komentarzService.addKomentarzToPost(KomentarzRequest.builder().opis("Komentarz do posta 3 od Piotra").targetId(p3.getPostId()).build(), usPiotr);

		System.out.println("Dodawanie odpowiedzi do komentarzy");

		Path kotPath = Paths.get(obrazSeedPath, "kot.png");
		MockMultipartFile obraz1 = new MockMultipartFile("tempFileName", "kot.png", 
		"image/png", fileUtils.readFileFromLocation(kotPath));

		Path peppinoPath = Paths.get(obrazSeedPath, "peppino.png");
		MockMultipartFile obraz2 = new MockMultipartFile("tempFileName", "peppino.png", 
		"image/png", fileUtils.readFileFromLocation(peppinoPath));

	//	System.out.println("Obraz: " + obraz.length);
	//	System.out.println("Multipart: " + obraz1.getContentType());

		KomentarzRequest komReq1 = KomentarzRequest.builder().opis("Piotr2 opis").targetId(kom2.getKomentarzId()).build();
		Komentarz kom4 = null;
		KomentarzRequest komReq2 = KomentarzRequest.builder().opis("Odpowiedź katarzyny głębokość 1").targetId(kom2.getKomentarzId()).build();
		Komentarz kom5 = null;
		try {
			kom4 = komentarzService.addOdpowiedzToKomentarz(komReq1, obraz1, usPiotr);
			kom5 = komentarzService.addOdpowiedzToKomentarz(komReq2, obraz2, usKatarzyna);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		
		KomentarzRequest komReq3 = KomentarzRequest.builder().opis("Odpowiedź katarzyny głębokość 2").targetId(kom5.getKomentarzId()).build();
		Komentarz kom6 = komentarzService.addOdpowiedzToKomentarz(komReq3, usKatarzyna);

		KomentarzRequest komReq3_1 = KomentarzRequest.builder().opis("Odpowiedź piotra głębokość 3").targetId(kom6.getKomentarzId()).build();
		Komentarz kom6_1 = komentarzService.addOdpowiedzToKomentarz(komReq3_1, usPiotr);

		KomentarzRequest komReq3_2 = KomentarzRequest.builder().opis("Odpowiedź michała głębokość 4").targetId(kom6_1.getKomentarzId()).build();
		Komentarz kom6_2 = komentarzService.addOdpowiedzToKomentarz(komReq3_2, usMichal);

		KomentarzRequest komReq3_3 = KomentarzRequest.builder().opis("Odpowiedź katarzyny głębokość 5").targetId(kom6_2.getKomentarzId()).build();
		Komentarz kom6_3 = komentarzService.addOdpowiedzToKomentarz(komReq3_3, usKatarzyna);

		KomentarzRequest komReq3_4 = KomentarzRequest.builder().opis("Odpowiedź piotra głębokość 6").targetId(kom6_3.getKomentarzId()).build();
		Komentarz kom6_4 = komentarzService.addOdpowiedzToKomentarz(komReq3_4, usPiotr);

		KomentarzRequest komReq3_5 = KomentarzRequest.builder().opis("Odpowiedź piotra głębokość 7").targetId(kom6_4.getKomentarzId()).build();
		Komentarz kom6_5 = komentarzService.addOdpowiedzToKomentarz(komReq3_5, usPiotr);

		KomentarzRequest komReq3_6 = KomentarzRequest.builder().opis("Odpowiedź piotra głębokość 4-1").targetId(kom6.getKomentarzId()).build();
		Komentarz kom6_6 = komentarzService.addOdpowiedzToKomentarz(komReq3_6, usPiotr);



		KomentarzRequest komReq4 = KomentarzRequest.builder().opis("Michał opis").targetId(kom4.getKomentarzId()).build();
		Komentarz kom7 = komentarzService.addOdpowiedzToKomentarz(komReq4, usMichal);

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
		Uzytkownik piotr = uzytkownikRepository.findByEmail(piotrEmail).get();
		Uzytkownik katarzyna = uzytkownikRepository.findByEmail(katarzynaEmail).get();

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
		//System.out.println("Usuwanie komentarzy");
		//komentarzService.deleteKomentarzFromPost(postId3, kom4.getKomentarzId(), usJan);

		//System.out.println("Usuwanie posta");
		//postService.deletePost(postId3, usJan);

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
