package com.sportevents.location;

import jakarta.persistence.*;
import lombok.*;

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
    private Long id;
    private String name;
    private Float lat;
    private Float lng;

    public Location(Float lat, Float lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
