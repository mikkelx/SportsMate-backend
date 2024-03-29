package com.sportevents.request;

import com.sportevents.common.SportLevel;
import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private String title;
    private Date date;
    private String description;
    private int maxParticipantsNumber;
    private boolean cyclical;
    private int cyclicalPeriodInDays;
    private SportLevel sportLevel;
    private Location location;
    private Sport sport;
    private Map<String, String> values;
}

