package com.sportevents.common;

public enum EventType {
    FOOTBALL("Piłka nożna"),
    CYCLING("Jazda na rowerze"),
    RUNNING("Bieg");

    private String name;

    EventType(String name) {
        this.name = name;
    }
}
