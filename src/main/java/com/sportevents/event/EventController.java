package com.sportevents.event;

import com.sportevents.common.Location;
import com.sportevents.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<Event> getEvent() {
        Event event = new Event(
                1L,
                "Tytul",
                new Date(),
                new Location(1L, "Lokalizacja", Float.valueOf(2), Float.valueOf(2), null),
                "Desc",
                new User(),
                null

        );
        System.out.println(event);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
//        Event event = new Event(
//                1L,
//                "Tytul",
//                new Date(),
//                new Location(1L, "Lokalizacja", Float.valueOf(2), Float.valueOf(2), null),
//                "Desc",
//                new User(),
//                null
//
//        );
        System.out.println(event);
        System.out.println(event.getLocation().toString());
        locationRepository.save(event.getLocation());
        event.setOrganizer(new User());
        eventRepository.save(event);
        return ResponseEntity.ok().body(event);
    }



}
