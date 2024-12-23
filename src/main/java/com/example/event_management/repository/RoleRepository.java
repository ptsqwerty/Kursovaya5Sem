package com.example.event_management.repository;

import com.example.event_management.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для сущности {@link Role}.
 * <p>
 * Предоставляет методы для выполнения CRUD операций на данные о ролях.
 * </p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по ее названию.
     *
     * @param name название роли
     * @return объект {@link Role}
     */
    Role findByName(String name);
}
