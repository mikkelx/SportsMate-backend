package com.sportevents.request;

import com.sportevents.common.SportLevel;
import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterCriteria {
    private Location userLocation;
    private float range;
    private Date date;
    private SportLevel sportLevel;
    private Sport sport;
    private int participantsNumberStart;
    private int participantsNumberEnd;
}
