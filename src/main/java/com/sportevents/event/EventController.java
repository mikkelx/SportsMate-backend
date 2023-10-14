package com.sportevents.event;

import com.google.gson.Gson;
import com.sportevents.location.Location;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.sport.subclasses.Football;
import com.sportevents.sport.subclasses.Running;
import com.sportevents.sport.Sport;
import com.sportevents.sport.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private SportRepository sportRepository;

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
        Gson defaultGson = new Gson();
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(defaultGson.toJson(event));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Event>> getEventsByRange(@RequestBody Location myLocation, @RequestParam Float range) {
        return ResponseEntity.ok(eventService.getEventsByRange(myLocation, range));
    }



}
