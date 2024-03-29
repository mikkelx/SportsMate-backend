package com.sportevents.event;

import com.sportevents.request.EventRequest;
import com.sportevents.request.FilterCriteria;
import com.sportevents.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@Slf4j
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Event> getEvent(@RequestParam Long eventId) {
        Event event = eventService.getEvent(eventId);
        return ResponseEntity.ok().body(event);
    }

    //TODO - add new fields to event creation
    @PostMapping
    @Transactional
    public ResponseEntity<?> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/filter")
    @Transactional
    public ResponseEntity<List<Event>> getEventsByFilter(@RequestBody FilterCriteria filterCriteria) {
        return ResponseEntity.ok(eventService.filterEvents(filterCriteria));
    }

    @PutMapping("/join")
    @Transactional
    public ResponseEntity<?> joinEventById(@RequestParam Long eventId) {
        return eventService.joinEvent(eventId);
    }

    @PutMapping("/leave")
    @Transactional
    public ResponseEntity<?> leaveEventById(@RequestParam Long eventId) {
        return eventService.leaveEvent(eventId);
    }

    @GetMapping("/users")
    @Transactional
    public ResponseEntity<List<User>> getEventUsersByRange(@RequestParam Long eventId) {
        return ResponseEntity.ok(eventService.getEventUsers(eventId));
    }

    @PostMapping("/start")
    @Transactional
    public ResponseEntity<?> startEventById(@RequestParam Long eventId) {
        return eventService.startEvent(eventId);
    }

    @PostMapping("/update")
    @Transactional
    public ResponseEntity<?> updateEvent(@RequestParam Long eventId, @RequestBody EventRequest eventRequest) {
        return eventService.updateEvent(eventId, eventRequest);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteEventById(Long eventId) {
        return eventService.deleteEvent(eventId);
    }
}
