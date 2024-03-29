package com.sportevents.user;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sportevents.common.UserRole;
import com.sportevents.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private boolean locked = false;
    private float lastLat;
    private float lastLng;
    private float rangePreference;

    @JsonManagedReference
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "users_events",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "eventId")}
    )
    private List<Event> joinedEvents = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="user_preferences", joinColumns=@JoinColumn(name="userId"))
    private List<Long> sportPreferences = new ArrayList<>();

    public void joinEvent(Event event) {
        this.joinedEvents.add(event);
    }

    public void leaveEvent(Event event) {
        this.joinedEvents.remove(event);
    }

    public boolean addSportPreference(Long sportId) {
        if(!this.sportPreferences.contains(sportId)) {
            this.sportPreferences.add(sportId);
            return true;
        } else return false;
    }

    public void resetLocation() {
        this.lastLat = 0;
        this.lastLng = 0;
    }

    public void deleteSportPreference(Long sportId) {
        this.sportPreferences.remove(sportId);
    }

    public void wipeSportPreferences() {
        this.sportPreferences.clear();
    }


}
