package com.example.bakalar.canvas;

import java.util.ArrayList;

public class Node {
    private String name;
    private ArrayList<Arrow> arrows;
    private double xCord;
    private double yCord;
    private double radius;

    public Node(double xCord, double yCord, double radius) {
        this.name = "Node";
        this.xCord = xCord;
        this.yCord = yCord;
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

    public double getxCord() {
        return xCord;
    }

    public void setxCord(double xCord) {
        this.xCord = xCord;
    }

    public double getyCord() {
        return yCord;
    }

    public void setyCord(double yCord) {
        this.yCord = yCord;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
