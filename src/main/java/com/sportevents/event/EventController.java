package com.sportevents.event;

import com.google.gson.Gson;
import com.sportevents.request.EventCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;;


    @GetMapping
    public ResponseEntity<Event> getEvent(@RequestParam Long eventId) {
        Event event = eventService.getEvent(eventId);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest eventRequest) {
        Gson defaultGson = new Gson();
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(defaultGson.toJson(event));
    }



}
