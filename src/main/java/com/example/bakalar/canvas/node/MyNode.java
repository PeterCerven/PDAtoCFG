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

import static com.example.bakalar.canvas.MainController.NODE_RADIUS;

@Getter
@Setter
public class MyNode extends Group {
    private static final Logger log = LogManager.getLogger(MyNode.class.getName());
    private Circle circle;
    private Text nameText;
    private String name;
    private ArrayList<Arrow> arrows;
    private ArrayList<Arrow> arrowsTo;
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
        nameText.setFont(javafx.scene.text.Font.font(NODE_RADIUS / 1.5));
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);


        this.getChildren().addAll(circle, endNode, nameText, startNodeArrow);
        this.arrows = new ArrayList<>();
        this.arrowsTo = new ArrayList<>();
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


    public void addArrow(Arrow arrow, String fromTo) {
        switch (fromTo) {
            case "to":
                this.arrowsTo.add(arrow);
                break;
            case "from":
                this.arrows.add(arrow);
                break;
            default:
                log.error("Wrong fromTo value");
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
        for (Arrow arrow : arrowsTo) {
            arrow.move(toEdge);
        }
    }

    public void removeArrow(Arrow arrow) {
        arrows.remove(arrow);
        arrowsTo.remove(arrow);
    }

    public List<Arrow> getAllArrows() {
        List<Arrow> allArrows = new ArrayList<>();
        allArrows.addAll(arrows);
        allArrows.addAll(arrowsTo);
        return allArrows;
    }

    public List<NodeTransition> getTransitions() {
        List<NodeTransition> nodeTransitions = new ArrayList<>();
        for (Arrow arrow : arrows) {
            nodeTransitions.add(arrow.getTransition());
        }
        return nodeTransitions;
    }
}
