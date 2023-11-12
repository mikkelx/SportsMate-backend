package com.sportevents.common;

public enum SportType {
    FOOTBALL("Piłka nożna"),
    BASKETBALL("Koszykówka"),
    TENNIS("Tenis"),
    SWIMMING("Pływanie"),
    VOLLEYBALL("Siatkówka"),
    GOLF("Golf"),
    ATHLETICS("Lekkoatletyka"),
    TABLE_TENNIS("Tenis stołowy"),
    CYCLING("Kolarstwo"),
    BOXING("Boks"),
    GYMNASTICS("Gimnastyka"),
    RUNNING("Bieganie"),
    MARATHON("Maraton"),
    SPRINT("Sprinterka"),
    LONG_DISTANCE("Biegi długodystansowe");

    private final String polishName;

    SportType(String polishName) {
        this.polishName = polishName;
    }

    public String getPolishName() {
        return polishName;
    }
}

