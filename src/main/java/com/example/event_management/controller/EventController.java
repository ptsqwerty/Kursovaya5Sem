package com.example.event_management.controller;

import com.example.event_management.model.Event;
import com.example.event_management.model.User;
import com.example.event_management.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

/**
 * Контроллер для управления мероприятиями.
 */
@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * Добавляет общие атрибуты в модель.
     *
     * @param model   модель {@link Model}
     * @param session текущая сессия {@link HttpSession}
     */
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        String userRole = currentUser != null && currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()
                ? currentUser.getRoles().iterator().next().getName() : "";
        model.addAttribute("userRole", userRole);
    }

    /**
     * Проверяет, имеет ли пользователь заданную роль.
     *
     * @param user     пользователь {@link User}
     * @param roleName имя роли
     * @return true, если пользователь имеет роль, иначе false
     */
    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    @GetMapping
    public String viewHomePage(Model model, HttpSession session) {
        model.addAttribute("listEvents", eventService.getAllEvents());
        return "index";
    }

    @PostMapping("/add")
    public String addEvent(@ModelAttribute Event event, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !(hasRole(currentUser, "EDITOR") || hasRole(currentUser, "ADMIN"))) {
            model.addAttribute("error", "У вас нет прав на это действие");
            return "index"; // или другая страница для отображения ошибки
        }
        try {
            eventService.saveEvent(event);
            return "redirect:/events"; // перенаправляем на страницу со списком мероприятий
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении мероприятия: " + e.getMessage());
            return "index"; // или другая страница для отображения ошибки
        }
    }

    @PostMapping(value = "/saveEvent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> saveEventJson(@RequestBody Event event, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !(hasRole(currentUser, "EDITOR") || hasRole(currentUser, "ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "У вас нет прав на это действие"));
        }
        try {
            eventService.saveEvent(event);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Ошибка при сохранении мероприятия: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> editEventJson(@RequestBody Event event, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !(hasRole(currentUser, "EDITOR") || hasRole(currentUser, "ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "У вас нет прав на это действие"));
        }
        try {
            eventService.saveEvent(event);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Ошибка при редактировании мероприятия: " + e.getMessage()));
        }
    }

    @GetMapping("/deleteEvent/{id}")
    public String deleteEvent(@PathVariable(value = "id") Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !(hasRole(currentUser, "EDITOR") || hasRole(currentUser, "ADMIN"))) {
            return "redirect:/events?error=accessDenied";
        }
        eventService.deleteEvent(id);
        return "redirect:/events";
    }

    @GetMapping("/search")
    public String searchEvents(@RequestParam("keyword") String keyword, Model model) {
        List<Event> events = eventService.searchEvents(keyword);
        model.addAttribute("listEvents", events);
        return "index";
    }

    @GetMapping("/sortByDate")
    public String sortByDate(Model model) {
        List<Event> events = eventService.sortByDate();
        model.addAttribute("listEvents", events);
        return "index";
    }

    @GetMapping("/stats")
    @ResponseBody
    public List<Object[]> getEventStats() {
        return eventService.getEventStats();
    }

    @GetMapping("/countByDate")
    @ResponseBody
    public ResponseEntity<?> countByArrivalDate(@RequestParam(value = "date", required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Дата не выбрана."));
        }
        Long count = eventService.countByDate(date);
        return ResponseEntity.ok(count);
    }
}
