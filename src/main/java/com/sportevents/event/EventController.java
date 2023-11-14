package com.sportevents.event;

import com.sportevents.dto.Message;
import com.sportevents.location.Location;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/event")
public class EventController {

    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Event> getEvent(@RequestParam Long eventId) {
        Event event = eventService.getEvent(eventId);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping("/bySport")
    public ResponseEntity<List<Event>> getEventsBySport(@RequestBody Message message) {
        return ResponseEntity.ok(eventService.getActiveEventsBySport(message.getMessage()));
    }

//    @GetMapping("/history")
//    public ResponseEntity<List<Event>> getHistoryEvents() {
//        return ResponseEntity.ok(eventService.getHistoryEvents());
//    }

//    @GetMapping("/x")
//    public ResponseEntity<Event> getEvent2(@RequestParam String sportName) {
//        Event event = new Event();
//        Sport sport = sportRepository.findBySportName(sportName);
//        sport.addAttribute("Długość gry", "60");
//        event.setSport(sport);
//        eventRepository.save(event);
//        return ResponseEntity.ok().body(event);
//    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/nearby")
    public ResponseEntity<List<Event>> getEventsByRange(@RequestBody Location myLocation, @RequestParam Float range) {
        return ResponseEntity.ok(eventService.getEventsByRange(myLocation, range));
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
        return eventService.startEvent(eventId);
    }

}
