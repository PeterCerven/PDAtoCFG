package com.example.bakalar.canvas.node;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.io.Serializable;

public class StartNodeArrow extends Path implements Serializable {


    public StartNodeArrow(double nodeX, double nodeY, double nodeRadius) {
        double startNode = nodeX - nodeRadius;
        this.getElements().addAll(
                new MoveTo(startNode, nodeY),
                new LineTo(startNode - (nodeRadius / 2.0), nodeY - (nodeRadius / 2.0)),
                new LineTo(startNode - (nodeRadius / 2.0), nodeY - (nodeRadius / 4.0)),
                new LineTo(startNode - nodeRadius, nodeY - (nodeRadius / 4.0)),
                new LineTo(startNode - nodeRadius, nodeY + (nodeRadius / 4.0)),
                new LineTo(startNode - (nodeRadius / 2.0), nodeY + (nodeRadius / 4.0)),
                new LineTo(startNode - (nodeRadius / 2.0), nodeY + (nodeRadius / 2.0)),
                new LineTo(startNode, nodeY)
        );
    }
}
