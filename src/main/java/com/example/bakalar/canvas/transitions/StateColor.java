package com.example.bakalar.canvas.transitions;

public enum StateColor {
    NEUTRAL("white"),
    ACCEPTED("green"),
    REJECTED("red");

    private final String color;

    StateColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
