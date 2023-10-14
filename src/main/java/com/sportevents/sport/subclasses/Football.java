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
public class Football extends Sport {
    private String sportName;
    private int durationInMinutes;

    public Football(int durationInMinutes) {
        this.sportName = "Piłka nożna";
        this.durationInMinutes = durationInMinutes;
    }
}
