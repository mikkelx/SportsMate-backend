package com.sportevents.event;

import com.google.firebase.database.annotations.NotNull;
import com.sportevents.common.Location;
import com.sportevents.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    private Location location;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer", nullable = false)
    private User organizer;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_event",
            joinColumns = {@JoinColumn(name = "eventId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")}
    )
    List<User> participants = new ArrayList<>();
}
