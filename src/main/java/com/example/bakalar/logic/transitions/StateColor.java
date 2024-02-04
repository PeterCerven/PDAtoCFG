package com.example.bakalar.logic.transitions;

import lombok.Getter;

@Getter
public enum StateColor {
    NEUTRAL("white"),
    ACCEPTED("green"),
    REJECTED("red");

    private final String color;

    StateColor(String color) {
        this.color = color;
    }

}
