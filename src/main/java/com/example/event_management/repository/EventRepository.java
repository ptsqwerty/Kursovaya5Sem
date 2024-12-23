package com.example.event_management.repository;

import com.example.event_management.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для сущности {@link Event}.
 * <p>
 * Предоставляет методы для выполнения CRUD операций и пользовательских запросов на данные о грузах.
 * </p>
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByNameContaining(String name);

    List<Event> findByDate(LocalDate arrivalDate);

    @Query("SELECT c.date, COUNT(c) FROM Event c GROUP BY c.date")
    List<Object[]> countEventsByDate();

    Long countByDate(LocalDate date);

    List<Event> findAll(Sort sort);
}
