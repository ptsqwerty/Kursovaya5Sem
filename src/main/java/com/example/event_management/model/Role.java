package com.example.event_management.model;

import jakarta.persistence.*;

/**
 * Представляет роль пользователя в системе.
 * <p>
 * Роли используются для управления доступом и авторизацией.
 * </p>
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    /**
     * Конструктор по умолчанию.
     */
    public Role() {}

    /**
     * Создает новую роль с указанным именем.
     *
     * @param name название роли
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Получает ID роли.
     *
     * @return ID роли как {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает ID роли.
     *
     * @param id ID роли для установки
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает название роли.
     *
     * @return название роли как {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название роли.
     *
     * @param name название роли для установки
     */
    public void setName(String name) {
        this.name = name;
    }
}
