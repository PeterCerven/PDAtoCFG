package com.example.bakalar.canvas.node;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class EndNode extends Circle {

    public EndNode(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius - 4, Color.WHITE);
        super.setStroke(Color.BLACK);
        super.setStrokeWidth(2);
    }
}
