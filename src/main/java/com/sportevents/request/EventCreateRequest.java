package com.sportevents.request;

import com.sportevents.common.EventType;
import com.sportevents.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    private String title;
    private Date date;
    private Location location;
    private String description;
    private EventType eventType;
}

