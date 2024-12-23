package com.example.event_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Cargo Transport.
 * <p>
 * Этот класс запускает Spring Boot приложение.
 * </p>
 */
@SpringBootApplication
public class EventManagementApplication {

	/**
	 * Точка входа в приложение Cargo Transport.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(EventManagementApplication.class, args);
	}
}
