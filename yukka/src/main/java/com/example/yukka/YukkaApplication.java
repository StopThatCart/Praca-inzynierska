package com.example.yukka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YukkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YukkaApplication.class, args);
	}

}
