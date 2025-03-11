package com.example.E_Commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
@EnableScheduling
public class EcommerceApiApplication {
 
	static {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApiApplication.class, args);
	}

}
