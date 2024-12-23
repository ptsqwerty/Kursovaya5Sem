package com.example.event_management.controller;

import com.example.event_management.dto.UserUpdateDto;
import com.example.event_management.model.Role;
import com.example.event_management.model.User;
import com.example.event_management.repository.RoleRepository;
import com.example.event_management.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Контроллер для управления пользователями администратора.
 */
@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Отображает страницу управления пользователями.
     *
     * @param model   модель {@link Model}
     * @param session текущая сессия {@link HttpSession}
     * @return имя представления "manage_users"
     */
    @GetMapping
    public String manageUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        String userRole = currentUser.getRoles().iterator().next().getName();
        model.addAttribute("userRole", userRole);
        return "manage_users";
    }

    /**
     * Редактирует пользователя по данным из {@link UserUpdateDto}.
     *
     * @param userDto объект {@link UserUpdateDto} из тела запроса
     * @return {@link ResponseEntity} с результатом операции
     */
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editUser(@RequestBody UserUpdateDto userDto) {
        try {
            User existingUser = userService.getUserById(userDto.getId());
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Пользователь не найден"));
            }

            Set<Role> newRoles = new HashSet<>();
            for (String roleName : userDto.getRoles()) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    newRoles.add(role);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Collections.singletonMap("error", "Роль " + roleName + " не найдена"));
                }
            }

            existingUser.setRoles(newRoles);
            userService.updateUser(existingUser);
            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Ошибка при редактировании пользователя: " + e.getMessage()));
        }
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя {@link User#getId()}
     * @return {@link ResponseEntity} с результатом операции
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Пользователь успешно удалён"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Ошибка при удалении пользователя: " + e.getMessage()));
        }
    }
}
