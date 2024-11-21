package com.example.challenge.controllers.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Sport {
    FOOTBALL(1),
    BASKETBALL(2),
    TENNIS(3);

    private final int code;

    public int getCode() {
        return code;
    }

    public static Sport fromCode(int code) {
        for (Sport sport : Sport.values()) {
            if (sport.code == code) {
                return sport;
            }
        }
        throw new IllegalArgumentException("Invalid Sport code: " + code);
    }
}

