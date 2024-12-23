package com.example.event_management.service;

import com.example.event_management.model.Event;
import com.example.event_management.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> searchEvents(String keyword) {
        return eventRepository.findByNameContaining(keyword);
    }

    public List<Event> sortByDate() {
        return eventRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
    }


    public List<Object[]> getEventStats() {
        return eventRepository.countEventsByDate();
    }

    public Long countByDate(LocalDate date) {
        return eventRepository.countByDate(date);
    }
}
