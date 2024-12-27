package com.example.yukka.seeder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.komentarz.controller.KomentarzService;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.post.controller.PostRepository;
import com.example.yukka.model.social.models.post.controller.PostService;
import com.example.yukka.model.social.models.rozmowaPrywatna.controller.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.models.rozmowaPrywatna.controller.RozmowaPrywatnaService;
import com.example.yukka.model.social.requests.KomentarzRequest;
import com.example.yukka.model.social.requests.OcenaRequest;
import com.example.yukka.model.social.requests.PostRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostSeeder {
	@Value("${obraz.seed.path}")
    private String obrazSeedPath;

	private final PostRepository postRepository;
    private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;
    private final RozmowaPrywatnaService rozmowaPrywatnaService;
	private final PostService postService;
	private final KomentarzService komentarzService;
	private final FileUtils fileUtils;

    private final int MAX_POSTY = 5;
	private final int MAX_KOMENTARZE = 4;
	private final int MAX_ODPOWIEDZI_DEPTH = 2;
	private final int MAX_ODPOWIEDZI = 2;

    private final Faker faker = new Faker(new Locale("pl"));
    
    public void seedRozmowy(Uzytkownik nadawca, Uzytkownik odbiorca, List<Uzytkownik> uzytkownicy) {
		log.info("Seedowanie rozmow prywatnych...");
		// Zaproszenie do rozmowy prywatnej
		rozmowaPrywatnaRepository
		.inviteToRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId(), LocalDateTime.now());

		// Akceptacja rozmowy prywatnej
		rozmowaPrywatnaRepository.acceptRozmowaPrywatna(odbiorca.getUzytId(), nadawca.getUzytId());

		log.info("Dodawanie komentarzy do rozmowy prywatnej");
		for (int i = 0; i < 10; i++) {
			KomentarzRequest odbiorcaWiad = KomentarzRequest.builder().opis("Wiadomość od " + odbiorca.getNazwa() + i).targetId(nadawca.getNazwa()).build();
			KomentarzRequest nadawcaWiad = KomentarzRequest.builder().opis("Wiadomość od " + nadawca.getNazwa() + i).targetId(odbiorca.getNazwa()).build();
			komentarzService.addKomentarzToWiadomoscPrywatna(odbiorcaWiad, null, odbiorca);
			komentarzService.addKomentarzToWiadomoscPrywatna(nadawcaWiad, null, nadawca);
		}

        for (Uzytkownik uzytkownik : uzytkownicy) {
            if (!uzytkownik.equals(nadawca) && !uzytkownik.equals(odbiorca)) {
                rozmowaPrywatnaService.inviteToRozmowaPrywatna(uzytkownik.getNazwa(), odbiorca);
            }
        }
	}


    public void addPostyWithKomentarze(List<Uzytkownik> uzytkownicy) {
		log.info("Dodawanie postów testowych...");
		for(int i = 0; i < MAX_POSTY; i++) {
			Post post = null;
			PostRequest postReq = PostRequest.builder()
				.tytul(faker.lorem().sentence())
				.opis(faker.lorem().paragraph(3))
				.build();

			int rand = (int)(Math.random() * uzytkownicy.size());

			// Losowe dodawanie obrazka z postem
			if (Math.random() < 0.5) {
				Path cheezyPath = Paths.get(obrazSeedPath, "cheezy.jpg");
				byte[] fileContent = fileUtils.readFileFromLocation(cheezyPath, null);
				if (fileContent != null) {
					MockMultipartFile obrazPost = new MockMultipartFile("tempFileName", "cheezy.jpg", 
					"image/png", fileUtils.readFileFromLocation(cheezyPath, null));
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
					.opis(faker.lorem().sentence())
					.build();

				rand = (int)(Math.random() * uzytkownicy.size());

				Komentarz kom = null;
				if (Math.random() < 0.5) {
					Path kotPath = Paths.get(obrazSeedPath, "kot.png");

					byte[] fileContent = fileUtils.readFileFromLocation(kotPath, null);
					if (fileContent == null) {
						kom = komentarzService.addKomentarzToPost(komReq, null, uzytkownicy.get(rand));
					} else {
						MockMultipartFile obrazKom = new MockMultipartFile("tempFileName", "kot.png", 
						"image/png", fileUtils.readFileFromLocation(kotPath, null));
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
				.opis(faker.lorem().sentence())
				.build();
	
			int rand = (int) (Math.random() * uzytkownicy.size());

			Komentarz nowaOdp = null;
				if (Math.random() < 0.5) {
					Path kotPath = Paths.get(obrazSeedPath, "kot.png");

					byte[] fileContent = fileUtils.readFileFromLocation(kotPath, null);
					if (fileContent == null) {
						nowaOdp = komentarzService.addOdpowiedzToKomentarz(odp, null, uzytkownicy.get(rand));
					} else {
						MockMultipartFile obrazKom = new MockMultipartFile("tempFileName", "kot.png", 
					"image/png", fileUtils.readFileFromLocation(kotPath, null));
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

}
