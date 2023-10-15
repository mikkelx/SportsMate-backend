package com.sportevents.sport.subclasses;

import com.sportevents.sport.Sport;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
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
    @Transient
    private final String sportName = "Piłka nożna";
    private int durationInMinutes;

}
