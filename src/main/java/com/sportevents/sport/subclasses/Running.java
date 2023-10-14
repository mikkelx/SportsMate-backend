package com.sportevents.sport.subclasses;

import com.sportevents.sport.Sport;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Running extends Sport {
    private String sportName;
    private double distanceInKm;

    public Running(double distanceInKm) {
        this.sportName = "Bieganie";
        this.distanceInKm = distanceInKm;
    }
}
