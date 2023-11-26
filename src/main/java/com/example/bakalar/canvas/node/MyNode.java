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
    private boolean selected;
    private boolean starting;
    private boolean ending;
    private CheckBox startingCheckBox;
    private CheckBox endingCheckBox;
    private Board board;
    private StartNodeArrow startNodeArrow;
    private EndNode endNode;

    public MyNode(double x, double y, double radius, Board board) {
        circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        endNode = new EndNode(x, y, radius);
        endNode.setVisible(false);

        this.startingCheckBox = new CheckBox("Starting Node");
        this.endingCheckBox = new CheckBox("End Node");

        name = "Node";
        nameText = new Text(name);
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);


        this.getChildren().addAll(circle, endNode, nameText);
        this.arrows = new ArrayList<>();
        this.board = board;
    }

    public void selectNode() {
        this.selected = true;
        this.circle.setFill(Color.BLUE);
    }

    public void unselectNode() {
        this.selected = false;
        this.circle.setFill(Color.WHITE);
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

    private void setStarting() {
        this.setStarting(true);
        this.startingCheckBox.setSelected(true);
        this.startNodeArrow.moveStartArrow(getAbsoluteCentrePosX(), getAbsoluteCentrePosY(), this.circle.getRadius());
    }

    public void unSetStarting() {
        this.setStarting(false);
        this.startNodeArrow = null;

    }

    private void setEnding() {
        setEnding(true);
        endNode.moveEndNode(this.circle.getCenterX(), this.circle.getCenterY());
        endNode.setVisible(true);
    }

    private void unSetEnding() {
        this.setEnding(false);
        endNode.setVisible(false);
    }

    public void showDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Node");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(this.getName());

        gridPane.add(startingCheckBox, 0, 0);
        gridPane.add(endingCheckBox, 0, 1);
        gridPane.add(nameField, 0, 2);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                this.setNodeName(nameField.getText());
                if (startingCheckBox.isSelected()) {
                    this.startNodeArrow = board.removeStartingFromOtherNodes();
                    setStarting();
                } else {
                    unSetStarting();
                }
                if (endingCheckBox.isSelected()) {
                    setEnding();
                } else {
                    unSetEnding();
                }
            }
        });
    }

    private void setNodeName(String text) {
        this.name = text;
        this.getNameText().setText(text);
    }

    public void move(double x, double y, double prevX, double prevY ) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        if (startNodeArrow != null && starting) {
            startNodeArrow.moveStartArrow(getAbsoluteCentrePosX(), getAbsoluteCentrePosY(), this.getCircle().getRadius());
        }
        if (endNode != null && ending) {
            endNode.moveEndNode(this.circle.getCenterX(), this.circle.getCenterY());
        }
        log.info("Node moved: X:{} Y:{}", x, y);

    }

    public void updateArrows() {
        for (Arrow arrow : arrows) {
            arrow.move();
        }
    }
}
