package com.sportevents.common;

public enum SportLevel {
    Low("Low"), Medium("Medium"), High("High");

    private final String level;

    SportLevel(String level) {
        this.level = level;
    }
}
