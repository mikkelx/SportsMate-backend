package com.sportevents.common;

import com.sportevents.event.Event;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    private String name;
    private Float lnt;
    private Float lng;

    @OneToMany(mappedBy = "location")
    private List<Event> events = new ArrayList<>();
}
