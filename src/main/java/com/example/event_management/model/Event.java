package com.example.event_management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Представляет информацию о грузе в системе.
 * <p>
 * Этот класс сопоставлен с таблицей базы данных и содержит информацию о грузоперевозках.
 * </p>
 */
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String location;
    private LocalDate date;
    private int peopleAmmount;


    /**
     * Конструктор по умолчанию.
     */
    public Event() {
    }

    public Event(String name, String location, LocalDate date, int peopleAmmount) {
        this.name = name;
        this.location=location;
        this.date=date;
        this.peopleAmmount=peopleAmmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPeopleAmmount() {
        return peopleAmmount;
    }

    public void setPeopleAmmount(int peopleAmmount) {
        this.peopleAmmount = peopleAmmount;
    }
}
