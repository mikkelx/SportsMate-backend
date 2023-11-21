package com.sportevents.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.firebase.database.annotations.NotNull;
import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import com.sportevents.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @NotNull
    private String title;
    @NotNull
    private Date date;
    private String description;
    private boolean active;
    private Long organizerId;
    private int participantsNumber;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="event_values", joinColumns=@JoinColumn(name="eventId"))
    private Map<String, String> values = new HashMap<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sport sport;

    @JsonBackReference
    @ManyToMany(mappedBy = "joinedEvents", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public void increaseParticipantsNumber() {
        this.participantsNumber++;
    }

    public void addValue(String key, String value) {
        this.values.put(key, value);
    }

}
