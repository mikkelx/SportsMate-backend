package com.sportevents.request;

import com.sportevents.location.Location;
import com.sportevents.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    private String title;
    private Date date;
    private Location location;
    private String description;
    private String sportName;
    private List<Sport> attributes;
}

