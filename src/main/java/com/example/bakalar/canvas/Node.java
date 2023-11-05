package com.example.bakalar.canvas;

import java.util.ArrayList;

public class Node {
    private String name;
    private ArrayList<Arrow> arrows;
    private double x;
    private double y;
    private double radius;

    private boolean selected;

    public Node(double x, double y, double radius) {
        this.name = "Node";
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.arrows = new ArrayList<>();
    }

    public boolean isIn(double x, double y) {
        return x >= this.x && x - radius <= this.x && y >= this.y && y - radius <= this.y;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
