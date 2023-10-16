package com.sportevents.event;

import com.sportevents.auth.AuthService;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.location.LocationRepository;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.sport.Sport;
import com.sportevents.sport.SportRepository;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
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

    private LocationRepository locationRepository;

    private EventRepository eventRepository;

    private SportRepository sportRepository;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @Autowired
    public EventService(LocationRepository locationRepository, EventRepository eventRepository, ModelMapper modelMapper,
                        SportRepository sportRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.sportRepository = sportRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(EventCreateRequest eventRequest) {
        Event event = convertEventRequestToEntity(eventRequest);

        event.setOrganizerId(AuthService.getCurrentUserId());
        event.setActive(true);

        locationRepository.save(event.getLocation());
        sportRepository.save(event.getSport());

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

        user.joinEvent(event);
        userRepository.save(user);

        return ResponseEntity.ok().body("");
    }

    public List<User> getEventUsers(Long eventId) {
        List<User> eventUsers = userRepository.findUsersByJoinedEvents_eventId(eventId);
        return eventUsers;
    }

    public void startEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId  + " not found"));
        eventRepository.save(event);
        event.setActive(false);
    }

    public List<Event> getEventsBySport(Sport sport) {
        return eventRepository.findAllEventsBySport(sport.getClass());
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

}
