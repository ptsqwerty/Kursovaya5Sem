package com.example.event_management.service;

import com.example.event_management.model.Post;
import com.example.event_management.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

/**
 * Сервисный класс для управления сущностями {@link Post}.
 * <p>
 * Предоставляет бизнес-логику для операций с публикациями.
 * </p>
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    /**
     * Получает все публикации.
     *
     * @return список всех объектов {@link Post}
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * Сохраняет сущность публикации.
     *
     * @param post объект {@link Post} для сохранения
     * @return сохраненный объект {@link Post}
     */
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    /**
     * Получает публикацию по ее ID.
     *
     * @param id ID публикации
     * @return объект {@link Post}, или {@code null}, если не найден
     */
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    /**
     * Удаляет публикацию по ее ID.
     *
     * @param id ID публикации для удаления
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    /**
     * Обновляет сущность публикации.
     *
     * @param post объект {@link Post} для обновления
     * @return обновленный объект {@link Post}
     */
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    /**
     * Ищет публикации по заголовку, содержанию и дате публикации.
     * <p>
     * Поддерживает различные комбинации критериев поиска.
     * </p>
     *
     * @param title   ключевое слово для поиска в заголовке
     * @param content ключевое слово для поиска в содержании
     * @param date    дата публикации для поиска
     * @return список подходящих объектов {@link Post}
     */
    public List<Post> searchPosts(String title, String content, LocalDate date) {
        title = (title != null && !title.trim().isEmpty()) ? title.trim() : null;
        content = (content != null && !content.trim().isEmpty()) ? content.trim() : null;

        if (title == null && content == null && date == null) {
            return postRepository.findAll();
        }
        if (title != null && content == null && date == null) {
            return postRepository.findByTitleContainingIgnoreCase(title);
        } else if (title == null && content != null && date == null) {
            return postRepository.findByContentContainingIgnoreCase(content);
        } else if (title == null && content == null && date != null) {
            return postRepository.findByPublicationDate(date);
        } else if (title != null && content != null && date == null) {
            return postRepository.findByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(title, content);
        } else if (title != null && content == null && date != null) {
            return postRepository.findByTitleContainingIgnoreCaseAndPublicationDate(title, date);
        } else if (title == null && content != null && date != null) {
            return postRepository.findByContentContainingIgnoreCaseAndPublicationDate(content, date);
        } else if (title != null && content != null && date != null) {
            return postRepository.findByTitleContainingIgnoreCaseAndContentContainingIgnoreCaseAndPublicationDate(title, content, date);
        } else {
            return postRepository.findAll();
        }
    }

}
