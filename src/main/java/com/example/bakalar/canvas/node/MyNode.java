package com.example.bakalar.canvas.node;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.arrow.Arrow;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class MyNode extends Group {
    private static final Logger log = LogManager.getLogger(MyNode.class.getName());
    private Circle circle;
    private Text nameText;
    private String name;
    private ArrayList<Arrow> arrows;
    private boolean starting;
    private boolean ending;
    private EndNode endNode;
    private boolean selected;

    public MyNode(double x, double y, double radius) {
        super();
        circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        endNode = new EndNode(x, y, radius);
        endNode.setVisible(false);

        name = "Node";
        nameText = new Text(name);
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);


        this.getChildren().addAll(circle, endNode, nameText);
        this.arrows = new ArrayList<>();
    }

    public double getAbsoluteCentrePosX() {
        return this.circle.getCenterX() + this.getTranslateX();
    }

    public double getAbsoluteCentrePosY() {
        return this.circle.getCenterY() + this.getTranslateY();
    }


    public void addArrow(Arrow arrow) {
        this.arrows.add(arrow);
    }

    public void setName(String newName) {
        this.name = newName;
        this.nameText.setText(newName);
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);
    }

    public void move(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        if (ending) {
            endNode.moveEndNode(this.circle.getCenterX(), this.circle.getCenterY());
        }
        for (Arrow arrow : arrows) {
            arrow.move();
        }
    }



}
