package com.example.bakalar.canvas;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MyNode extends Circle {
    private String name;
    private ArrayList<Arrow> arrows;
    private double radius;

    private boolean selected;

    public MyNode(double x, double y, double radius) {
        super(x, y, radius);
        this.setFill(Color.BLACK);
        this.name = "myNode";
        this.radius = radius;
        this.arrows = new ArrayList<>();
    }


    public double getAbsoluteCentrePosX() {
        return this.getCenterX() + this.getTranslateX();
    }

    public double getAbsoluteCentrePosY() {
        return this.getCenterY() + this.getTranslateY();
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


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
