package com.example.event_management.dto;

import java.util.Set;

/**
 * Объект передачи данных для обновления ролей пользователя.
 * <p>
 * Содержит ID пользователя и набор названий ролей для обновления.
 * </p>
 */
public class UserUpdateDto {
    private Long id;
    private Set<String> roles;

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
     * Получает набор ролей.
     *
     * @return {@link Set} названий ролей
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Устанавливает набор ролей.
     *
     * @param roles {@link Set} названий ролей для установки
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
