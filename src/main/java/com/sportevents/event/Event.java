package com.sportevents.event;

import com.google.firebase.database.annotations.NotNull;
import com.sportevents.common.Location;
import com.sportevents.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @NotNull
    private String title;
    @NotNull
    private Date date;
    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    private Location location;
    private String description;
    @ManyToOne
    @JoinColumn(name = "organizer", nullable = false)
    private User organizer;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_events",
            joinColumns = {@JoinColumn(name = "eventId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")}
    )
    List<User> participants = new ArrayList<>();
}
