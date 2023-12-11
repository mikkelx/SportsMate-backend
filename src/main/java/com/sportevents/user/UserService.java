package com.sportevents.user;

import com.sportevents.auth.AuthService;
import com.sportevents.event.EventRepository;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.sport.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SportRepository sportRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserService(UserRepository userRepository, SportRepository sportRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.sportRepository = sportRepository;
        this.eventRepository = eventRepository;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    public User blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        user.setLocked(true);
        return userRepository.save(user);
    }

    public User unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        user.setLocked(false);
        return userRepository.save(user);
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
}
