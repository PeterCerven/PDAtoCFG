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
import java.util.stream.Collectors;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;

@Getter
@Setter
public class MyNode extends Group implements Cloneable {
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
        this.arrowsFrom = new ArrayList<>();
        this.arrowsTo = new ArrayList<>();
    }

    @Override
    public MyNode clone() {
        try {
            MyNode cloned = (MyNode) super.clone();

            // Clone mutable fields
            cloned.circle = new Circle(this.circle.getCenterX(), this.circle.getCenterY(), this.circle.getRadius());
            cloned.circle.setFill(this.circle.getFill());
            cloned.circle.setStroke(this.circle.getStroke());

            cloned.nameText = new Text(this.nameText.getText());
            cloned.nameText.setFont(this.nameText.getFont());
            cloned.nameText.setX(this.nameText.getX());
            cloned.nameText.setY(this.nameText.getY());

            // Clone other components if they are mutable
            cloned.endNode = new EndNode(this.endNode.getCenterX(), this.endNode.getCenterY(), this.endNode.getRadius());
            cloned.endNode.setVisible(this.endNode.isVisible());

            cloned.startNodeArrow = new StartNodeArrow(this.circle.getCenterX(), this.circle.getCenterY(), this.circle.getRadius());
            cloned.startNodeArrow.setVisible(this.startNodeArrow.isVisible());

            // Clone collections
            // This assumes Arrow implements Cloneable and has a properly overridden clone method.
            cloned.arrowsFrom = this.arrowsFrom.stream()
                    .map(Arrow::clone)
                    .collect(Collectors.toCollection(ArrayList::new));
            cloned.arrowsTo = this.arrowsTo.stream()
                    .map(Arrow::clone)
                    .collect(Collectors.toCollection(ArrayList::new));

            // Add cloned children
            cloned.getChildren().clear();
            cloned.getChildren().addAll(cloned.circle, cloned.endNode, cloned.nameText, cloned.startNodeArrow);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can never happen if Cloneable is implemented
        }
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
}
