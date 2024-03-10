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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;

@Getter
@Setter
public class MyNode extends Group implements Serializable {
    private static final Logger log = LogManager.getLogger(MyNode.class.getName());
    private Circle circle;
    private Text nameText;
    private String name;
    private ArrayList<Arrow> arrowsFrom;
    private ArrayList<Arrow> arrowsTo;
    private boolean starting;
    private boolean ending;
    private EndNode endNode;
    private StartNodeArrow startNodeArrow;
    private boolean selected;
    private int nodeId;

    public MyNode(double x, double y, double radius) {
        super();

        circle = createCircle(x, y, radius);
        nameText = createNameText();

        endNode = new EndNode(x, y, radius);
        endNode.setVisible(false);

        startNodeArrow = new StartNodeArrow(x, y, radius);
        startNodeArrow.setVisible(false);

        this.getChildren().addAll(circle, endNode, nameText, startNodeArrow);
        this.arrowsFrom = new ArrayList<>();
        this.arrowsTo = new ArrayList<>();
    }

    public MyNode(String name, double x, double y, int nodeId, boolean isStarting, boolean isEnding) {
        this(x, y, NODE_RADIUS);
        this.name = name;
        this.nodeId = nodeId;
        this.starting = isStarting;
        this.ending = isEnding;
        this.setName(name);
        if (isStarting) {
            startNodeArrow.setVisible(true);
        }
        if (isEnding) {
            endNode.setVisible(true);
        }
    }

    private Circle createCircle(double x, double y, double radius) {
        Circle c = new Circle(x, y, radius, Color.WHITE);
        c.setStroke(Color.BLACK);
        return c;
    }

    private Text createNameText() {
        Text text = new Text("Node");
        text.setFont(javafx.scene.text.Font.font(NODE_RADIUS / 1.5));
        text.setX(circle.getCenterX() - text.getBoundsInLocal().getWidth() / 2);
        text.setY(circle.getCenterY() + text.getBoundsInLocal().getHeight() / 4);
        return text;
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
            this.circle.setFill(new Color(0.4, 0.6, 0.4, 1));
        } else {
            this.circle.setFill(Color.WHITE);
        }
    }


    public void addArrow(Arrow arrow, String fromTo) {
        switch (fromTo) {
            case "to" -> this.arrowsTo.add(arrow);
            case "from" -> this.arrowsFrom.add(arrow);
            default -> log.error("Wrong fromTo value");
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

    // arrow updates

    public void updateArrows(boolean toEdge) {
        for (Arrow arrow : arrowsFrom) {
            arrow.move(toEdge);
        }
        for (Arrow arrow : arrowsTo) {
            arrow.move(toEdge);
        }
    }

    public void removeArrow(Arrow arrow) {
        arrowsFrom.remove(arrow);
        arrowsTo.remove(arrow);
    }

    public List<Arrow> getAllArrows() {
        List<Arrow> allArrows = new ArrayList<>();
        allArrows.addAll(arrowsFrom);
        allArrows.addAll(arrowsTo);
        return allArrows;
    }

    public void updatePosition() {
        this.setTranslateX(this.getTranslateX());
        this.setTranslateY(this.getTranslateY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyNode myNode)) return false;

        return getNodeId() == myNode.getNodeId();
    }

    @Override
    public int hashCode() {
        return getNodeId();
    }
}
