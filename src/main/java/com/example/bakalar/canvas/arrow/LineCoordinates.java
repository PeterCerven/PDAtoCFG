package com.example.bakalar.canvas.arrow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineCoordinates {
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public LineCoordinates(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

}
