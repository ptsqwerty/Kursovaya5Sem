package com.example.event_management.repository;

import com.example.event_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для сущности {@link User}.
 * <p>
 * Предоставляет методы для выполнения CRUD операций на данные о пользователях.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска
     * @return объект {@link User}
     */
    User findByUsername(String username);
}
