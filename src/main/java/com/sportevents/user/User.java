package com.sportevents.user;


import com.sportevents.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @OneToMany(mappedBy = "organizer")
    private Set<Event> events;
    @ManyToMany(mappedBy = "participants")
    private List<Event> myEvents = new ArrayList<>();
}
