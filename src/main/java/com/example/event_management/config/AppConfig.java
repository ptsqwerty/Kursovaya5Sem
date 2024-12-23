// AppConfig.java
package com.example.event_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс конфигурации приложения.
 * <p>
 * Предоставляет бины для контекста Spring.
 */
@Configuration
public class AppConfig {

    /**
     * Создает и возвращает экземпляр {@link PasswordEncoder} с использованием {@link BCryptPasswordEncoder}.
     *
     * @return экземпляр {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
