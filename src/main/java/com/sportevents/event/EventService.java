package com.sportevents.event;

import com.sportevents.location.Location;
import com.sportevents.request.EventCreateRequest;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Event createEvent(EventCreateRequest eventRequest) {
        Location location = eventRequest.getLocation();
        User user = new User();  //get User from Auth
        Event event = convertEventRequestToEntity(eventRequest);

        event.setOrganizerId(userRepository.save(user).getUserId());

        locationRepository.save(location);
         //user exists already in db
        eventRepository.save(event);
        return event;
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }



    private Event convertEventRequestToEntity(EventCreateRequest eventCreateRequest) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Event event = modelMapper.map(eventCreateRequest,Event.class);
        return event;
    }



}
