package com.example.event_management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Представляет статью или новость в системе.
 * <p>
 * Этот класс содержит информацию о публикациях, созданных пользователями.
 * </p>
 */
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate publicationDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    /**
     * Конструктор по умолчанию.
     */
    public Post() {}

    /**
     * Создает новую публикацию с указанными данными.
     *
     * @param title   заголовок публикации
     * @param content содержание публикации
     * @param author  автор публикации
     */
    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.publicationDate = LocalDate.now();
        this.author = author;
    }

    /**
     * Получает ID публикации.
     *
     * @return ID публикации как {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает ID публикации.
     *
     * @param id ID публикации для установки
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает заголовок публикации.
     *
     * @return заголовок как {@link String}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок публикации.
     *
     * @param title заголовок для установки
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получает дату публикации.
     *
     * @return дата публикации как {@link LocalDate}
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Устанавливает дату публикации.
     *
     * @param publicationDate дата публикации для установки
     */
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Получает содержание публикации.
     *
     * @return содержание как {@link String}
     */
    public String getContent() {
        return content;
    }

    /**
     * Устанавливает содержание публикации.
     *
     * @param content содержание для установки
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Получает автора публикации.
     *
     * @return автор как {@link User}
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Устанавливает автора публикации.
     *
     * @param author автор для установки
     */
    public void setAuthor(User author) {
        this.author = author;
    }
}
