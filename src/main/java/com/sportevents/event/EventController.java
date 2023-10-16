package com.sportevents.event;

import com.sportevents.location.Location;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.sport.Sport;
import com.sportevents.sport.SportRepository;
import com.sportevents.sport.subclasses.Football;
import com.sportevents.sport.subclasses.Running;
import com.sportevents.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private EventService eventService;

    private SportRepository sportRepository;

    @Autowired
    public EventController(EventService eventService, SportRepository sportRepository) {
        this.eventService = eventService;
        this.sportRepository = sportRepository;
    }

    @GetMapping
    public ResponseEntity<Event> getEvent(@RequestParam Long eventId) {
        Event event = eventService.getEvent(eventId);
        return ResponseEntity.ok().body(event);
    }

    @GetMapping("/x")
    public ResponseEntity<Event> getEvent2(@RequestParam Long eventId) {
        Event event = new Event();
        Sport sport = new Football(10);
        Sport sport2 = new Running(10.0);
        event.setSport(sport);
        sportRepository.save(sport);
        sportRepository.save(sport2);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Event>> getEventsByRange(@RequestBody Location myLocation, @RequestParam Float range) {
        return ResponseEntity.ok(eventService.getEventsByRange(myLocation, range));
    }

    @GetMapping("/bySport")
    public ResponseEntity<List<Event>> getEventsBySport(@RequestBody Sport sport) {
        return ResponseEntity.ok(eventService.getEventsBySport(sport));

    }

    @PostMapping("/join")
    public ResponseEntity<?> joinEventById(Long eventId) {
        return eventService.joinEvent(eventId);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getEventsByRange(@RequestParam Long eventId) {
        return ResponseEntity.ok(eventService.getEventUsers(eventId));
    }

    @PostMapping("/start")
    public ResponseEntity<?> startEventById(Long eventId) {
        eventService.startEvent(eventId);
        return ResponseEntity.ok().body("");
    }

}
