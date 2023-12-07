package com.sportevents.request;

import com.sportevents.common.SportLevel;
import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterCriteria {
    private Location userLocation;
    private float range;
    private Date dateStart;
    private Date dateEnd;
    private boolean isCyclical;
    private SportLevel sportLevel;
    private List<Sport> sport;
    private int participantsNumberStart;
    private int participantsNumberEnd;
}
