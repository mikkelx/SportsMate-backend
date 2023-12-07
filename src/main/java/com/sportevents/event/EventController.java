package com.sportevents.event;

import com.sportevents.location.Location;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.request.FilterCriteria;
import com.sportevents.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping
    @Transactional
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/nearby")
    @Transactional
    public ResponseEntity<List<Event>> getEventsByRange(@RequestBody Location myLocation, @RequestParam Float range) {
        return ResponseEntity.ok(eventService.getEventsByRange(myLocation, range));
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
    public ResponseEntity<?> startEventById(Long eventId) {
        return eventService.startEvent(eventId);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteEventById(Long eventId) {
        return eventService.deleteEvent(eventId);
    }
}
