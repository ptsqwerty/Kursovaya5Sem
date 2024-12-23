package com.example.event_management.dto;

/**
 * Объект передачи данных для входа пользователя.
 * <p>
 * Этот класс используется для передачи данных для входа между клиентом и сервером.
 * </p>
 */
public class UserLoginDto {
    private String username;
    private String password;

    /**
     * Конструктор по умолчанию.
     */
    public UserLoginDto() {}

    /**
     * Получает имя пользователя.
     *
     * @return имя пользователя как {@link String}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username имя пользователя для установки
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получает пароль.
     *
     * @return пароль как {@link String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль.
     *
     * @param password пароль для установки
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
