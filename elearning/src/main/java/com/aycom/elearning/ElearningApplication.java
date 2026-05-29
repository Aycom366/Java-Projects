package com.aycom.elearning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.aycom.elearning.models.Author;
import com.aycom.elearning.repositories.AuthorRepository;
import com.github.javafaker.Faker;

@SpringBootApplication
public class ElearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElearningApplication.class, args);
	}

	/**
	 * Without bean, Spring doesn't know the method exists, so
	 * CommandLineRunner never gets picked up and never runs on startup.
	 * 
	 * commandLineRunner can also be used to run something on startup
	 */
	@Bean
	CommandLineRunner commandLineRunner(AuthorRepository repository) {
		return args -> {
			Faker faker = new Faker();
			for (int i = 0; i < 50; i++) {
				Author author = Author.builder()
						.firstName(faker.name().firstName()).lastName(faker.name().lastName())
						.age(faker.number().numberBetween(10, 50)).email(faker.name().username() + "@gmail.com")
						.build();
				repository.save(author);
			}

		};
	}

}
