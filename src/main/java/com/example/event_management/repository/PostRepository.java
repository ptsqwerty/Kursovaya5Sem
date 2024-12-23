package com.example.event_management.repository;

import com.example.event_management.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для сущности {@link Post}.
 * <p>
 * Предоставляет методы для выполнения CRUD операций и сложных запросов на данные о публикациях.
 * </p>
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Находит публикации, заголовки которых содержат указанную строку (без учета регистра).
     *
     * @param title строка для поиска в заголовках
     * @return список объектов {@link Post}
     */
    List<Post> findByTitleContainingIgnoreCase(String title);

    /**
     * Находит публикации, содержание которых содержит указанную строку (без учета регистра).
     *
     * @param content строка для поиска в содержании
     * @return список объектов {@link Post}
     */
    List<Post> findByContentContainingIgnoreCase(String content);

    /**
     * Находит публикации, опубликованные в указанную дату.
     *
     * @param publicationDate дата публикации для поиска
     * @return список объектов {@link Post}
     */
    List<Post> findByPublicationDate(LocalDate publicationDate);

    /**
     * Находит публикации по заголовку и дате публикации.
     *
     * @param title           строка для поиска в заголовках
     * @param publicationDate дата публикации для поиска
     * @return список объектов {@link Post}
     */
    List<Post> findByTitleContainingIgnoreCaseAndPublicationDate(String title, LocalDate publicationDate);

    /**
     * Находит публикации по содержанию и дате публикации.
     *
     * @param content         строка для поиска в содержании
     * @param publicationDate дата публикации для поиска
     * @return список объектов {@link Post}
     */
    List<Post> findByContentContainingIgnoreCaseAndPublicationDate(String content, LocalDate publicationDate);

    /**
     * Находит публикации по заголовку и содержанию.
     *
     * @param title   строка для поиска в заголовках
     * @param content строка для поиска в содержании
     * @return список объектов {@link Post}
     */
    List<Post> findByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(String title, String content);

    /**
     * Находит публикации по заголовку, содержанию и дате публикации.
     *
     * @param title           строка для поиска в заголовках
     * @param content         строка для поиска в содержании
     * @param publicationDate дата публикации для поиска
     * @return список объектов {@link Post}
     */
    List<Post> findByTitleContainingIgnoreCaseAndContentContainingIgnoreCaseAndPublicationDate(String title, String content, LocalDate publicationDate);
}
