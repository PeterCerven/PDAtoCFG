package com.example.bakalar.canvas;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MyNode extends Circle {
    private String name;
    private ArrayList<Arrow> arrows;
    private double x;
    private double y;
    private double radius;

    private boolean selected;

    public MyNode(double x, double y, double radius) {
        super(x, y, radius);
        this.setFill(Color.BLACK);
        this.name = "myNode";
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.arrows = new ArrayList<>();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public void setArrows(ArrayList<Arrow> arrows) {
        this.arrows = arrows;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
