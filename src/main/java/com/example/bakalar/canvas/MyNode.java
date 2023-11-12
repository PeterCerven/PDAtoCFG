package com.example.bakalar.canvas;

import com.example.bakalar.utils.State;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MyNode extends Group {
    private Circle circle;
    private Text nameText;
    private String name;
    private ArrayList<Arrow> arrows;
    private boolean selected;
    private State state;

    public MyNode(double x, double y, double radius) {
        circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        name = "Node";
        nameText = new Text(name);
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);

        this.getChildren().addAll(circle, nameText);
        this.state = State.DEFAULT;
        this.arrows = new ArrayList<>();
    }


    public double getAbsoluteCentrePosX() {
        return this.circle.getCenterX() + this.getTranslateX();
    }

    public double getAbsoluteCentrePosY() {
        return this.circle.getCenterY() + this.getTranslateY();
    }

    public void moveAllArrows() {
        for (Arrow arrow : arrows) {
            arrow.move();
        }
    }

    public void addArrow(Arrow arrow) {
        this.arrows.add(arrow);
    }

    public void setName(String newName) {
        this.name = newName;
        this.nameText.setText(newName);
        // Realign the text in case the new name changes its size
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);
    }
}
