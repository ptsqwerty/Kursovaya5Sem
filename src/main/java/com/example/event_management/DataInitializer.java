package com.example.event_management;

import com.example.event_management.model.Role;
import com.example.event_management.model.User;
import com.example.event_management.repository.RoleRepository;
import com.example.event_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Инициализатор данных для приложения Cargo Transport.
 * <p>
 * Заполняет начальные данные, такие как роли по умолчанию и администраторский пользователь.
 * </p>
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Выполняется при запуске приложения для инициализации данных.
     *
     * @param args аргументы командной строки
     * @throws Exception если произошла ошибка при инициализации
     */
    @Override
    public void run(String... args) throws Exception {
        // Создаем роли, если они не существуют
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }

        Role viewerRole = roleRepository.findByName("VIEWER");
        if (viewerRole == null) {
            viewerRole = new Role("VIEWER");
            roleRepository.save(viewerRole);
        }

        Role editorRole = roleRepository.findByName("EDITOR");
        if (editorRole == null) {
            editorRole = new Role("EDITOR");
            roleRepository.save(editorRole);
        }

        // Проверяем, существует ли администраторский пользователь
        if (userService.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("1234"); // Пароль будет закодирован в saveNewUser

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole); // Назначаем роль ADMIN
            adminUser.setRoles(roles);

            userService.saveNewUser(adminUser);
            System.out.println("Администратор 'admin' создан.");
        }
    }
}
