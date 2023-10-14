package com.sportevents.event;

import com.google.firebase.database.annotations.NotNull;
import com.sportevents.common.EventType;
import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    @ManyToOne(fetch = FetchType.EAGER)
    private Sport sport;
    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

}
