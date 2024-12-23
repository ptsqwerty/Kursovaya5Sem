package com.example.event_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Представляет пользователя в системе.
 * <p>
 * Пользователи имеют учетные данные и связанные с ними роли для аутентификации и авторизации.
 * </p>
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Конструктор по умолчанию.
     */
    public User() {}

    /**
     * Создает нового пользователя с указанными именем и паролем.
     *
     * @param username имя пользователя
     * @param password пароль пользователя (будет захеширован)
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Получает ID пользователя.
     *
     * @return ID пользователя как {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает ID пользователя.
     *
     * @param id ID пользователя для установки
     */
    public void setId(Long id) {
        this.id = id;
    }

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
     * Получает пароль пользователя.
     * <p>
     * Примечание: Пароль является только для записи и не должен отображаться при сериализации JSON.
     * </p>
     *
     * @return пароль как {@link String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password пароль для установки (должен быть захеширован)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Получает роли, связанные с пользователем.
     *
     * @return {@link Set} объектов {@link Role}
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Устанавливает роли, связанные с пользователем.
     *
     * @param roles {@link Set} объектов {@link Role} для установки
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
