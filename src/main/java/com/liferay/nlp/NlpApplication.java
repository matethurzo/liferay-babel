package com.liferay.nlp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NlpApplication {

	public static void main(String[] args) {
		System.out.println("### STARTED ###");

		SpringApplication.run(NlpApplication.class, args);
	}

}
