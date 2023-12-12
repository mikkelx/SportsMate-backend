package com.sportevents.user;

import com.google.firebase.auth.FirebaseAuthException;
import com.sportevents.auth.AuthService;
import com.sportevents.event.EventRepository;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.notification.NotificationRepository;
import com.sportevents.notification.NotificationService;
import com.sportevents.sport.Sport;
import com.sportevents.sport.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SportRepository sportRepository;
    private final EventRepository eventRepository;
    private final NotificationService notificationService;

    @Autowired
    public UserService(UserRepository userRepository, SportRepository sportRepository,
                       EventRepository eventRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.sportRepository = sportRepository;
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    public User setSportPreference(Long sportId) {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!sportRepository.existsById(sportId))
            throw new NotFoundException("Sport does not exist");

        //TODO - change exception type
        if(!user.addSportPreference(sportId))
            throw new RuntimeException("Sport preference already exists");
        return userRepository.save(user);
    }

    public void deleteSportPreference(Long sportId) {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!sportRepository.existsById(sportId))
            throw new NotFoundException("Sport does not exist");

        user.deleteSportPreference(sportId);
        userRepository.save(user);
    }

    public void deleteAllSportPreferences() {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.wipeSportPreferences();
        userRepository.save(user);
    }

    public Object getSportPreference() {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return user.getSportPreferences();
    }

    public Object getEventsCreatedByUser() {
        return eventRepository.findAllByOrganizerId(AuthService.getCurrentUserId());
    }

    public void setLocation(Location location) {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setLastLat(location.getLat());
        user.setLastLng(location.getLng());
        userRepository.save(user);
    }

    public Object getLocation() {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return new Location(user.getLastLat(), user.getLastLng());
    }

    public void setRangePreference(float range) {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setRangePreference(range);
        userRepository.save(user);
    }

    public void setSportPreferences(List<Long> sportPreferences) {
        User user = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Sport> sports = sportRepository.findAll();
        for(Long sportId : sportPreferences) {
            if(!sports.stream().anyMatch(sport -> sport.getSportId().equals(sportId)))
                throw new NotFoundException("Sport with id " + sportId + " does not exist");
        }

        user.setSportPreferences(sportPreferences);
        userRepository.save(user);
    }

    public Object getAllUsers() {
        return userRepository.findAll().stream().filter(
                user -> !Objects.equals(user.getUserId(), AuthService.getCurrentUserId()));
    }

}
