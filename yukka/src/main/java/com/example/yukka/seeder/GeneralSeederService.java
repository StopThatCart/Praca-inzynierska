package com.example.yukka.seeder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.controller.DzialkaRepository;
import com.example.yukka.model.dzialka.controller.DzialkaService;
import com.example.yukka.model.dzialka.enums.Wyswietlanie;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.social.models.komentarz.controller.KomentarzRepository;
import com.example.yukka.model.social.models.post.controller.PostRepository;
import com.example.yukka.model.social.models.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.models.powiadomienie.controller.PowiadomienieService;
import com.example.yukka.model.social.models.rozmowaPrywatna.controller.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.requests.SpecjalnePowiadomienieRequest;
import com.example.yukka.model.social.requests.ZgloszenieRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralSeederService {
	@Value("${obraz.seed.path}")
    private String obrazSeedPath;

	private final PasswordEncoder passwordEncoder;
	private final UzytkownikRepository uzytkownikRepository;
	private final UzytkownikService uzytkownikService;

	private final PostRepository postRepository;

	private final KomentarzRepository komentarzRepository;
	private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;

	private final DzialkaService dzialkaService;
	private final DzialkaRepository dzialkaRepository;

	private final RoslinaService roslinaService;
	private final RoslinaRepository roslinaRepository;
    private final RoslinaMapper roslinaMapper;

	private final FileUtils fileUtils;

	private final RoslinaWlasnaSeeder roslinaWlasnaSeeder;
	private final PowiadomienieService powiadomienieService;

    private final SocialSeeder socialSeeder;

	//Faker faker = new Faker(new Locale.Builder().setLanguage("pl").setRegion("PL").build());

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

    Faker faker = new Faker(new Locale("pl"));

    
	public void unseed() {
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
		uzytkownikRepository.clearRoslinaWlasna();

		log.info("Usuwanie uzytkownikow");
		uzytkownikRepository.clearUzytkowicy();

	}

	public void seed() {
		log.info("Seedowanie bazy danych...");

		uzytkownicy = seedUzytkownicy();
		
		socialSeeder.addPostyWithKomentarze(uzytkownicy);

		socialSeeder.seedRozmowy(usKatarzyna, usPiotr, uzytkownicy);

		seedZgloszenia();

		seedDzialka();

	}

	private List<Uzytkownik> seedUzytkownicy() {
		log.info("Seedowanie uzytkownikow...");

		Uzytkownik usJan = Uzytkownik.builder()
		.uuid(uzytkownikService.createUzytkownikId())
		.nazwa("Jan Kowalski").email("jan@email.pl")
		.haslo(passwordEncoder.encode("jan12345678"))
		.labels(new ArrayList<>(Arrays.asList("Admin")))
		.aktywowany(true)
		.build();

		Uzytkownik usPrac = Uzytkownik.builder()
		.uuid(uzytkownikService.createUzytkownikId())
		.nazwa("Anna Nowak")
		.email("anna@email.pl")
		.haslo(passwordEncoder.encode("anna12345678"))
		.aktywowany(true)
		.build();


		Uzytkownik usPiotr = Uzytkownik.builder()
		.uuid(uzytkownikService.createUzytkownikId())
        .nazwa("Piotr Wiśniewski").email(piotrEmail)
        .haslo(passwordEncoder.encode("piotr12345678"))
		.aktywowany(true)
        .build();


		Uzytkownik usKatarzyna = Uzytkownik.builder()
		.uuid(uzytkownikService.createUzytkownikId())
		.nazwa("Katarzyna Mazur").email(katarzynaEmail)
        .haslo(passwordEncoder.encode("katarzyna12345678"))
		.aktywowany(true)
        .build();


		Uzytkownik usMichal = Uzytkownik.builder()
		.uuid(uzytkownikService.createUzytkownikId())
        .nazwa("Michał Zieliński").email(michalEmail)
        .haslo(passwordEncoder.encode("michal12345678"))
		.aktywowany(true)
        .build();

		Uzytkownik usNiegrzeczny = Uzytkownik.builder()
		.uuid(uzytkownikService.createUzytkownikId())
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
		"image/png", fileUtils.readFileFromLocation(adachiPath, null));

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
		
		ZgloszenieRequest request = ZgloszenieRequest.builder()
		.zglaszany(usNiegrzeczny.getNazwa())
		.opis(faker.lorem().sentence())
		.typPowiadomienia(TypPowiadomienia.ZGLOSZENIE.name())
		.build();
		
		powiadomienieService.sendZgloszenie(request, usMichal);
		request.setOpis(faker.lorem().sentence());
		powiadomienieService.sendZgloszenie(request, usPiotr);
		request.setOpis(faker.lorem().sentence());
		powiadomienieService.sendZgloszenie(request, usKatarzyna);

		log.info("Dodawanie specjalnego powiadomienia...");
		SpecjalnePowiadomienieRequest specjalnePowiadomienieRequest = SpecjalnePowiadomienieRequest.builder().opis(faker.lorem().paragraph()).build();
		powiadomienieService.addSpecjalnePowiadomienie(specjalnePowiadomienieRequest, usJan);
	}

	private void seedDzialka() {
		log.info("Seedowanie dzialek...");

		RoslinaResponse ros = roslinaMapper.toRoslinaResponse(roslinaRepository.getRandomRoslina().get());

		Path peppinoPath = Paths.get(obrazSeedPath, "peppino.png");
		MockMultipartFile obraz2 = new MockMultipartFile("tempFileName", "peppino.png", 
		"image/png", fileUtils.readFileFromLocation(peppinoPath, null));
		
		DzialkaRoslinaRequest req = DzialkaRoslinaRequest.builder()
		.numerDzialki(1)
		.x(6).y(6)
		.pozycje(Set.of(
			Pozycja.builder().x(6).y(6).build(), 
			Pozycja.builder().x(6).y(7).build(), 
			Pozycja.builder().x(7).y(6).build()
			))
		.kolor("#6c6ef0")
		.wyswietlanie(Wyswietlanie.TEKSTURA_KOLOR.toString())
		.roslinaUUID(ros.getUuid())
		.build();


		ros = roslinaMapper.toRoslinaResponse(roslinaRepository.getRandomRoslina().get());
		DzialkaRoslinaRequest req2 = DzialkaRoslinaRequest.builder()
		.numerDzialki(2)
		.x(12).y(11)
		.pozycje(Set.of(
			Pozycja.builder().x(12).y(11).build(), 
			Pozycja.builder().x(13).y(11).build(), 
			Pozycja.builder().x(14).y(11).build()
			))
		.kolor("#f06ce7")
		.wyswietlanie(Wyswietlanie.KOLOR.toString())
		.roslinaUUID(ros.getUuid())
		.build();
		
		log.info("Dodawanie rosliny 1 do dzialek");
		dzialkaService.saveRoslinaToDzialka(req, null, null, usPiotr);

		log.info("Dodawanie rosliny 2 do dzialek");
		dzialkaService.saveRoslinaToDzialka(req2, null, null, usPiotr);

		dzialkaRepository.getDzialkaByNumer(usPiotr.getEmail(), 2).get();

		// Rosliny uzytkownika 
		log.info("Seedowanie roslin uzytkownika[TESTOWE]...");
		
		// Wiem wiem, okropieństwo
		PageResponse<RoslinaResponse> roslinyUzytkownika = roslinaWlasnaSeeder.seedRoslinyWlasne(usPiotr);
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
		.roslinaUUID(roslinaUzyt.getUuid())
		.build();

		dzialkaService.saveRoslinaToDzialka(req3, null, null, usPiotr);

		log.info("Aktualizacja obrazu rosliny w dzialce");
		dzialkaService.updateRoslinaObrazInDzialka(req2, obraz2, null, usPiotr);

		MoveRoslinaRequest moveRequest1 = MoveRoslinaRequest.builder()
		.numerDzialki(2)
		.x(12).y(11)
		.xNowy(3).yNowy(4)
		.pozycje(Set.of(
			Pozycja.builder().x(3).y(4).build(), 
			Pozycja.builder().x(4).y(4).build(), 
			Pozycja.builder().x(5).y(4).build()
			))
		.build();

		log.info("Zmienianie pozycji rosliny w dzialce");
		dzialkaService.updateRoslinaPozycjaInDzialka(moveRequest1, usPiotr);

		// To samo ale dla anny
		ros =  roslinaMapper.toRoslinaResponse(roslinaRepository.getRandomRoslina().get());
		DzialkaRoslinaRequest reqAna = DzialkaRoslinaRequest.builder()
		.numerDzialki(2)
		.x(1).y(4)
		.pozycje(Set.of(
			Pozycja.builder().x(1).y(4).build(), 
			Pozycja.builder().x(2).y(4).build(), 
			Pozycja.builder().x(1).y(3).build()
			))
		.kolor("#1ba626")
		.wyswietlanie(Wyswietlanie.KOLOR.toString())
		.roslinaUUID(ros.getUuid())
		.build();
		
		log.info("Dodawanie rosliny dla anny do dzialek");
		dzialkaService.saveRoslinaToDzialka(reqAna, null, null, usPrac);
	}

	// private void roslinaSearchTest() {
	// 	String roslinaNazwa = "a";
	// 	String roslinaNazwaLacinska = "";
    //     String roslinaOpis = "";
    //     Double wysokoscMin = 1.5;
    //     Double wysokoscMax = 12.0;

	// 	CechaWithRelations kolorLisci = CechaWithRelations.builder()
	// 	.etykieta("Kolor").relacja("MA_KOLOR_LISCI").nazwa("ciemnozielone")
	// 	.build();
	// 	CechaWithRelations okresOwocowania = CechaWithRelations.builder()
	// 	.etykieta("Okres").relacja("MA_OKRES_OWOCOWANIA").nazwa("październik")
	// 	.build();

	// 	CechaWithRelations gleba1 = CechaWithRelations.builder()
	// 	.etykieta("Gleba").relacja("MA_GLEBE").nazwa("przeciętna ogrodowa")
	// 	.build();
	// 	CechaWithRelations gleba2 = CechaWithRelations.builder()
	// 	.etykieta("Gleba").relacja("MA_GLEBE").nazwa("próchniczna")
	// 	.build();
	// 	RoslinaRequest exampleRoslina = RoslinaRequest.builder()
    //         .nazwa(roslinaNazwa)
    //         .wysokoscMin(wysokoscMin)
    //         .wysokoscMax(wysokoscMax)
	// 		.cechy(Arrays.asList(kolorLisci, okresOwocowania, gleba1, gleba2))
	// 		.build();

	// 	System.out.println("Testowanie wyszukiwania rośliny z parametrami");
	// 	PageResponse<RoslinaResponse> res = roslinaService.findAllRoslinyWithParameters(0, 12, exampleRoslina);
	// 	if(res.getContent().isEmpty()) {
	// 		System.out.println("Nie znaleziono rośliny");
	// 	} else {
	// 		System.out.println("Znaleziono pierwszą roślinę: " + res.getContent().get(0).getNazwa());
	// 		System.out.println("Liczba znalezionych roślin: " + res.getContent().size());
	// 	}

	// }
}
