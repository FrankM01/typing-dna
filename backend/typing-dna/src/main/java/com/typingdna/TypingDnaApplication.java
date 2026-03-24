package com.typingdna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TypingDnaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TypingDnaApplication.class, args);
	}

}
