package com.example.bakalar.canvas.node;

import com.example.bakalar.canvas.arrow.Arrow;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MyNode extends Group {
    private static final Logger log = LogManager.getLogger(MyNode.class.getName());
    private Circle circle;
    private Text nameText;
    private String name;
    private ArrayList<Arrow> arrows;
    private ArrayList<Arrow> arrowsFrom;
    private boolean starting;
    private boolean ending;
    private EndNode endNode;
    private StartNodeArrow startNodeArrow;
    private boolean selected;

    public MyNode(double x, double y, double radius) {
        super();
        circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        endNode = new EndNode(x, y, radius);
        endNode.setVisible(false);

        startNodeArrow = new StartNodeArrow(x, y, radius);
        startNodeArrow.setVisible(false);

        name = "Node";
        nameText = new Text(name);
        nameText.setFont(javafx.scene.text.Font.font(20));
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);


        this.getChildren().addAll(circle, endNode, nameText, startNodeArrow);
        this.arrows = new ArrayList<>();
        this.arrowsFrom = new ArrayList<>();
    }

    public double getAbsoluteCentrePosX() {
        return this.circle.getCenterX() + this.getTranslateX();
    }

    public double getAbsoluteCentrePosY() {
        return this.circle.getCenterY() + this.getTranslateY();
    }

    public void setEnding(boolean ending) {
        this.ending = ending;
        this.endNode.setVisible(ending);
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
        this.startNodeArrow.setVisible(starting);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.circle.setFill(Color.BLUE);
        } else {
            this.circle.setFill(Color.WHITE);
        }
    }


    public void addArrow(Arrow arrow, boolean from) {
        this.arrows.add(arrow);
        if (from) {
            this.arrowsFrom.add(arrow);
        }
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
    }

    public void updateArrows(boolean toEdge) {
        for (Arrow arrow : arrows) {
            arrow.move(toEdge);
        }
    }

    public void removeArrow(Arrow arrow1) {
        arrows.remove(arrow1);
    }

    public List<NodeTransition> getTransitions() {
        List<NodeTransition> nodeTransitions = new ArrayList<>();
        for (Arrow arrow : arrows) {
            if (arrow.getFrom() == this) {
                nodeTransitions.add(arrow.getTransition());
            }
        }
        return nodeTransitions;
    }
}
