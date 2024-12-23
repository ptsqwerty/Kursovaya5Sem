// BlogAdminController.java
package com.example.event_management.controller;

import com.example.event_management.model.Post;
import com.example.event_management.model.User;
import com.example.event_management.service.PostService;
import com.example.event_management.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для административных операций блога.
 */
@Controller
@RequestMapping("/blog/admin")
public class BlogAdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * Отображает административную панель блога.
     *
     * @param model модель {@link Model}
     * @return имя представления "blog_admin"
     */
    @GetMapping
    public String blogAdmin(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "blog_admin";
    }

    /**
     * Обрабатывает добавление новой записи блога.
     *
     * @param post    объект {@link Post} из тела запроса
     * @param session текущая сессия {@link HttpSession}
     * @return {@link ResponseEntity} с результатом операции
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addPost(@RequestBody Post post, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(403).body("Необходимо войти в систему");
            }
            post.setAuthor(currentUser);
            post.setPublicationDate(LocalDate.now());
            Post newPost = postService.savePost(post);
            return ResponseEntity.ok(newPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при добавлении записи: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает редактирование существующей записи блога.
     *
     * @param post    объект {@link Post} из тела запроса
     * @param session текущая сессия {@link HttpSession}
     * @return {@link ResponseEntity} с результатом операции
     */
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editPost(@RequestBody Post post, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(403).body("Необходимо войти в систему");
            }
            Post existingPost = postService.getPostById(post.getId());
            if (existingPost == null) {
                return ResponseEntity.badRequest().body("Запись не найдена");
            }
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            existingPost.setPublicationDate(post.getPublicationDate());
            Post updatedPost = postService.updatePost(existingPost);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при редактировании записи: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает удаление записи блога по идентификатору.
     *
     * @param id идентификатор записи {@link Post#getId()}
     * @return перенаправление на "/blog/admin"
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("Запись успешно удалена");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при удалении записи: " + e.getMessage());
        }
    }
}
