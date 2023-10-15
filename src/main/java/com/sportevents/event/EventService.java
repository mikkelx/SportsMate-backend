package com.sportevents.event;

import com.sportevents.auth.AuthService;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.location.LocationRepository;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.sport.SportRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;

    private LocationRepository locationRepository;

    private EventRepository eventRepository;

    private SportRepository sportRepository;

    private ModelMapper modelMapper;

    @Autowired
    public EventService(LocationRepository locationRepository, EventRepository eventRepository, ModelMapper modelMapper,
                        SportRepository sportRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.sportRepository = sportRepository;
    }

    public Event createEvent(EventCreateRequest eventRequest) {
        Location location = eventRequest.getLocation();
        Event event = convertEventRequestToEntity(eventRequest);

        event.setOrganizerId(AuthService.getCurrentUserId());
        event.setActive(true);


        locationRepository.save(location);
        sportRepository.save(event.getSport());
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

    public double meters(Location myLocation, Location objectLocation) {
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
