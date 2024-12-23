package com.example.event_management.controller;

import com.example.event_management.dto.UserLoginDto;
import com.example.event_management.model.Role;
import com.example.event_management.model.User;
import com.example.event_management.repository.RoleRepository;
import com.example.event_management.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Arrays;

/**
 * Контроллер для управления действиями пользователей.
 * <p>
 * Этот контроллер обрабатывает запросы, связанные с регистрацией, входом и выходом пользователей.
 * </p>
 */
@Controller
@SessionAttributes("currentUser")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Обрабатывает запрос на главную страницу.
     * <p>
     * Если пользователь уже вошел в систему, перенаправляет его на страницу грузов.
     * Иначе отображает приветственную страницу.
     * </p>
     *
     * @param session текущая сессия пользователя
     * @return имя представления для отображения
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return "redirect:/events";
        } else {
            return "welcome";
        }
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     *
     * @param userForm объект {@link User}, содержащий данные для регистрации
     * @return {@link ResponseEntity} с сообщением о результате регистрации
     */
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody User userForm) {
        if (userService.findByUsername(userForm.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }

        // Назначаем роль VIEWER по умолчанию
        Role viewerRole = roleRepository.findByName("VIEWER");
        if (viewerRole == null) {
            viewerRole = new Role("VIEWER");
            roleRepository.save(viewerRole);
        }

        userForm.setRoles(new HashSet<>(Arrays.asList(viewerRole)));
        userService.saveNewUser(userForm); // Используем метод для сохранения нового пользователя
        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    /**
     * Обрабатывает вход пользователя в систему.
     *
     * @param loginForm объект {@link UserLoginDto}, содержащий данные для входа
     * @param session   текущая сессия пользователя
     * @return {@link ResponseEntity} с сообщением о результате входа
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginForm, HttpSession session) {
        if (userService.checkCredentials(loginForm.getUsername(), loginForm.getPassword())) {
            User user = userService.findByUsername(loginForm.getUsername());
            session.setAttribute("currentUser", user);
            session.setAttribute("isLoggedIn", true);
            return ResponseEntity.ok("Вход выполнен успешно");
        } else {
            return ResponseEntity.status(401).body("Неверное имя пользователя или пароль");
        }
    }

    /**
     * Обрабатывает выход пользователя из системы.
     *
     * @param session текущая сессия пользователя
     * @return перенаправление на главную страницу
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("isLoggedIn", false); // Установить флаг, что пользователь вышел
        session.setAttribute("currentUser", null); // Очищаем текущего пользователя
        session.setAttribute("userRole", "");
        session.invalidate();
        return "redirect:/"; // Перенаправление на главную страницу
    }


    /**
     * Отображает форму входа пользователя.
     * <p>
     * В текущей реализации перенаправляет на главную страницу.
     * </p>
     *
     * @return перенаправление на главную страницу
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "redirect:/"; // Перенаправляем на главную страницу
    }
}
