package com.example.bakalar.canvas;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineCoordinates {
    double startX;
    double startY;
    double endX;
    double endY;

    public LineCoordinates(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

}
