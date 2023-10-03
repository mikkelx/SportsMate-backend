package com.sportevents.common;


import com.google.gson.annotations.SerializedName;

public enum EventType {

    @SerializedName(value = "Piłka nożna")
    FOOTBALL("Piłka nożna"),
    @SerializedName(value = "Jazda na rowerze")
    CYCLING("Jazda na rowerze"),
    @SerializedName(value = "Bieg")
    RUNNING("Bieg");

    private String name;

    EventType(String name) {
        this.name = name;
    }

}
