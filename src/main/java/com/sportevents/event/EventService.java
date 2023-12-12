package com.sportevents.event;

import com.sportevents.auth.AuthService;
import com.sportevents.comment.CommentService;
import com.sportevents.exception.RequestException;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.location.LocationRepository;
import com.sportevents.notification.NotificationService;
import com.sportevents.request.EventRequest;
import com.sportevents.request.FilterCriteria;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sportevents.specifications.EventSpecs.mainFilter;

@Service
@Slf4j
public class EventService {

    private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    @Autowired
    public EventService(LocationRepository locationRepository, EventRepository eventRepository,
                        UserRepository userRepository, CommentService commentService, ModelMapper modelMapper,
                        NotificationService notificationService) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
    }

    public Event createEvent(EventRequest eventRequest) {
        Event event = convertEventRequestToEntity(eventRequest);

        event.setOrganizerId(AuthService.getCurrentUserId());
        event.setActive(true);
        event.setParticipantsNumber(1);

        if(new Date().after(event.getDate())) {
            log.info("You cannot create event in the past!");
            throw new RequestException("You cannot create event in the past!");
        }

        locationRepository.save(event.getLocation());


        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.joinEvent(event);

        //save event and notify interested users of new event
        event = eventRepository.save(event);
        notificationService.notifyUsersOfNewEvent(event);

        return event;
    }

    public Event getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
        event.setJoined(userRepository.existsByUserIdAndEventId(AuthService.getCurrentUserId(), eventId));

        event.setJoined(event.getUsers()
                .stream()
                .filter(user -> Objects.equals(user.getUserId(), AuthService.getCurrentUserId())).count() > 0);

        return event;
    }

    private Event convertEventRequestToEntity(EventRequest eventCreateRequest) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(eventCreateRequest,Event.class);
    }


    public List<Event> getEventsByRange(Location myLocation, Float range) {
        List<Event> eventsList = eventRepository.findAllByActive(true);

        return eventsList.stream()
                .filter(event-> calculateDistance(myLocation, event.getLocation()) <= range)
                .peek(event -> {
                    event.setDistance(calculateDistance(myLocation, event.getLocation()));
                    event.setJoined(event.getUsers()
                            .stream()
                            .filter(user -> Objects.equals(user.getUserId(), AuthService.getCurrentUserId())).count() > 0);
                })
                .collect(Collectors.toList());
    }

    public List<Event> filterEvents(FilterCriteria filterCriteria) {
        List<Event> eventsList = eventRepository.findAll(mainFilter(filterCriteria));

        return eventsList.stream()
                .filter(event -> {
                    double distance = calculateDistance(filterCriteria.getUserLocation(), event.getLocation());
                    event.setDistance(distance);
                    return distance <= filterCriteria.getRange();
                })
                .peek(event -> {
                    event.setJoined(event.getUsers()
                            .stream()
                            .anyMatch(user -> Objects.equals(user.getUserId(), AuthService.getCurrentUserId())));
                })
                .collect(Collectors.toList());

    }

    public ResponseEntity<?> joinEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));

        if(!event.isActive()) {
            log.info("Cannot join inactive event!");
            return ResponseEntity.badRequest().body("Cannot join inactive event!");
        }

        if(event.getParticipantsNumber() >= event.getMaxParticipantsNumber()) {
            log.info("Event is full!");
            return ResponseEntity.badRequest().body("Event is full!");
        }

        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("Event with id: " + AuthService.getCurrentUserId() + " not found"));

        if(userRepository.existsByUserIdAndEventId(user.getUserId(), eventId)) {
            log.info("Cannot join same event twice");
            return ResponseEntity.badRequest().body("Cannot join same event twice");
        }

        event.increaseParticipantsNumber();

        user.joinEvent(event);
        userRepository.save(user);

        return ResponseEntity.ok().body("");
    }

    public List<User> getEventUsers(Long eventId) {
        return userRepository.findUsersByJoinedEvents_eventId(eventId);
    }

    public ResponseEntity<String> startEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId  + " not found"));

        if(!AuthService.getCurrentUserId().equals(event.getOrganizerId())) {
            return ResponseEntity.badRequest().body("Cannot start somebody's event!");
        }

        eventRepository.save(event);
        event.setActive(false);
        return ResponseEntity.ok().body("");
    }

    public ResponseEntity<?> deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId  + " not found"));

        if(!AuthService.getCurrentUserId().equals(event.getOrganizerId())) {
            return ResponseEntity.badRequest().body("Cannot delete somebody's event!");
        }

        event.getUsers().forEach(user -> user.leaveEvent(event));
        eventRepository.save(event);

        commentService.deleteAllCommentsByEventId(eventId);
        eventRepository.delete(event);

        return ResponseEntity.ok().body("");
    }

    public ResponseEntity<?> leaveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));

        if(!event.isActive()) {
            return ResponseEntity.badRequest().body("Cannot leave inactive event!");
        }

        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("Event with id: " + AuthService.getCurrentUserId() + " not found"));


        event.decreaseParticipantsNumber();

        user.leaveEvent(event);
        userRepository.save(user);

        return ResponseEntity.ok().body("");
    }

    public static double calculateDistance(Location myLocation, Location objectLocation) {
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


    public ResponseEntity<?> updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));

        if(!AuthService.getCurrentUserId().equals(event.getOrganizerId())) {
            return ResponseEntity.badRequest().body("Cannot update somebody's event!");
        }

        event.setTitle(eventRequest.getTitle());
        event.setDate(eventRequest.getDate());
        event.setDescription(eventRequest.getDescription());
        event.setMaxParticipantsNumber(eventRequest.getMaxParticipantsNumber());
        event.setCyclical(eventRequest.isCyclical());
        event.setCyclicalPeriodInDays(eventRequest.getCyclicalPeriodInDays());
        event.setSportLevel(eventRequest.getSportLevel());
        event.setLocation(eventRequest.getLocation());
        event.setSport(eventRequest.getSport());
        event.setValues(eventRequest.getValues());


        locationRepository.save(event.getLocation());
        eventRepository.save(event);

        return ResponseEntity.ok().body("");
    }
}
