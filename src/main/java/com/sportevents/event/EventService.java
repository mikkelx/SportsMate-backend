package com.sportevents.event;

import com.sportevents.auth.AuthService;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.location.LocationRepository;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import jakarta.servlet.ServletOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventService {

    private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Event createEvent(EventCreateRequest eventRequest) {
        Event event = convertEventRequestToEntity(eventRequest);

        event.setOrganizerId(AuthService.getCurrentUserId());
        event.setActive(true);
        event.increaseParticipantsNumber();

        locationRepository.save(event.getLocation());

        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.joinEvent(event);

        return eventRepository.save(event);
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
    }

    private Event convertEventRequestToEntity(EventCreateRequest eventCreateRequest) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(eventCreateRequest,Event.class);
    }


    public List<Event> getEventsByRange(Location myLocation, Float range) {
        List<Event> eventsList = eventRepository.findAllByActive(true);

        List<Event> nearbyEvents = eventsList.stream()
                .filter(event -> meters(myLocation, event.getLocation()) <= range)
                .collect(Collectors.toList());

        return nearbyEvents;
    }

    public ResponseEntity<?> joinEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));

        if(!event.isActive()) {
            return ResponseEntity.badRequest().body("Cannot join inactive event!");
        }

        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("Event with id: " + AuthService.getCurrentUserId() + " not found"));

        System.out.println(userRepository.existsByUserIdAndEventId(user.getUserId(), eventId));
        System.out.println(userRepository.findUsersByJoinedEvents_eventId(eventId).get(0));
        if(userRepository.existsByUserIdAndEventId(user.getUserId(), eventId)) {
            return ResponseEntity.badRequest().body("Cannot join same event twice");
        }

        event.increaseParticipantsNumber();

        user.joinEvent(event);
        userRepository.save(user);

        return ResponseEntity.ok().body("");
    }

    public List<User> getEventUsers(Long eventId) {
        List<User> eventUsers = userRepository.findUsersByJoinedEvents_eventId(eventId);
        return eventUsers;
    }

    public ResponseEntity<String> startEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId  + " not found"));

        if(AuthService.getCurrentUserId() != event.getOrganizerId()) {
            return ResponseEntity.badRequest().body("Cannot start somebody's event!");
        }

        eventRepository.save(event);
        event.setActive(false);
        return ResponseEntity.ok().body("");
    }

    private double meters(Location myLocation, Location objectLocation) {
        double lt1 = myLocation.getLat();
        double ln1 = myLocation.getLng();
        double lt2 = objectLocation.getLat();
        double ln2 = objectLocation.getLng();

        double x = lt1 * d2r;
        double y = lt2 * d2r;
        double distance = Math.acos( Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km;
        distance = distance / 1000;
        return distance;
    }

    public List<Event> getActiveEventsBySport(String sportName) {
        return eventRepository.findAllBySportNameAndActive(sportName, true);
    }

//    public List<Event> getHistoryEvents() {
//        return userRepository.findJoinedEventsByUserId(AuthService.getCurrentUserId());
//    }
}
