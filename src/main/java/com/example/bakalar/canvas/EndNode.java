package com.example.bakalar.canvas;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class EndNode extends Circle {

    public EndNode(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius - 4, Color.WHITE);
        super.setStroke(Color.BLACK);
        super.setStrokeWidth(2);
    }

    public void moveEndNode(double centerX, double centerY) {
        this.setCenterX(centerX);
        this.setCenterY(centerY);
    }
}
