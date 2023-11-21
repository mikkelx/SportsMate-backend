package com.sportevents.request;

import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    private String title;
    private Date date;
    private String description;
    private Location location;
    private String sportName;
    private Sport sport;
    private Map<String, String> values;
}

