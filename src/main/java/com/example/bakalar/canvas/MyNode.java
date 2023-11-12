package com.example.bakalar.canvas;

import com.example.bakalar.utils.State;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MyNode extends Circle {
    private String name;
    private ArrayList<Arrow> arrows;
    private boolean selected;
    private State state;

    public MyNode(double x, double y, double radius) {
        super(x, y, radius);
        this.setFill(Color.BLACK);
        this.name = "myNode";
        this.state = State.DEFAULT;
        this.arrows = new ArrayList<>();
    }


    public double getAbsoluteCentrePosX() {
        return this.getCenterX() + this.getTranslateX();
    }

    public double getAbsoluteCentrePosY() {
        return this.getCenterY() + this.getTranslateY();
    }

    public void moveAllArrows() {
        for (Arrow arrow : arrows) {
            arrow.move();
        }
    }

    public void addArrow(Arrow arrow) {
        this.arrows.add(arrow);
    }
}
