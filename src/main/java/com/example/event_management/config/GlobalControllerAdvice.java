package com.example.event_management.config;

import com.example.event_management.model.User;
import com.example.event_management.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

/**
 * Глобальный контроллер советов, предоставляющий общие атрибуты для всех моделей.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;
    @ModelAttribute
    public void preventCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }
    /**
     * Добавляет текущего пользователя и его роль в модель для использования в представлениях.
     *
     * @param session текущая сессия {@link HttpSession}
     * @param model   модель {@link org.springframework.ui.Model}
     */
    @ModelAttribute
    public void addAttributes(HttpSession session, org.springframework.ui.Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        User currentUser = (User) session.getAttribute("currentUser");

        if (isLoggedIn == null || !isLoggedIn || currentUser == null) {
            // Если пользователь не авторизован
            model.addAttribute("currentUser", null);
            model.addAttribute("userRole", "VIEWER"); // Роль по умолчанию
            model.addAttribute("isLoggedIn", false);
        } else {
            model.addAttribute("currentUser", currentUser);
            String userRole = currentUser.getRoles().stream()
                    .filter(role -> role.getName().equalsIgnoreCase("ADMIN") ||
                            role.getName().equalsIgnoreCase("EDITOR") ||
                            role.getName().equalsIgnoreCase("VIEWER"))
                    .map(role -> role.getName().toUpperCase())
                    .findFirst()
                    .orElse("VIEWER");
            model.addAttribute("userRole", userRole);
            model.addAttribute("isLoggedIn", true);
        }
    }
}
