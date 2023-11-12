package com.example.bakalar.canvas;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MyNode extends Circle {
    private String name;
    private ArrayList<Arrow> arrows;

    private boolean selected;

    public MyNode(double x, double y, double radius) {
        super(x, y, radius);
        this.setFill(Color.BLACK);
        this.name = "myNode";
        this.arrows = new ArrayList<>();
    }


    public double getAbsoluteCentrePosX() {
        return this.getCenterX() + this.getTranslateX();
    }

    public double getAbsoluteCentrePosY() {
        return this.getCenterY() + this.getTranslateY();
    }

    public void moveAllArrows(double x,  double y) {
        for (Arrow arrow : arrows) {
            arrow.move();
        }
    }

    public void addArrow(Arrow arrow) {
        this.arrows.add(arrow);
    }
}
