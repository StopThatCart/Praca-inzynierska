package com.example.yukka;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;

@SpringBootApplication
public class YukkaApplication {


	Faker faker = new Faker(new Locale.Builder().setLanguage("pl").setRegion("PL").build());

	public static void main(String[] args) {
		SpringApplication.run(YukkaApplication.class, args);
	}

	void seed() {
		// Kingdom come
	}

}
