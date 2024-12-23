package com.example.event_management.controller;

import com.example.event_management.model.Post;
import com.example.event_management.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для отображения и поиска записей блога.
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private PostService postService;

    /**
     * Отображает главную страницу блога.
     *
     * @param model модель {@link Model}
     * @return имя представления "blog_main"
     */
    @GetMapping
    public String viewBlogMainPage(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "blog_main";
    }

    /**
     * Выполняет поиск записей блога по заданным критериям.
     *
     * @param title   заголовок для поиска
     * @param content содержание для поиска
     * @param date    дата публикации
     * @param model   модель {@link Model}
     * @return имя представления "blog_main" с результатами поиска
     */
    @GetMapping("/search")
    public String searchPosts(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String content,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {
        List<Post> posts = postService.searchPosts(title, content, date);
        model.addAttribute("posts", posts);
        return "blog_main";
    }
}
