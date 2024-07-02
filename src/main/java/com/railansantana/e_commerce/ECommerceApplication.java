package com.railansantana.e_commerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		// Carrega as variáveis do arquivo .env
		Dotenv dotenv = Dotenv.configure().load();

		// Define as variáveis de ambiente manualmente
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ECommerceApplication.class, args);
	}

}
