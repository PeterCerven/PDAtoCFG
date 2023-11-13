package com.example.bakalar.canvas;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class MyNode extends Group {
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

    public MyNode(double x, double y, double radius, Board board) {
        circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        this.startingCheckBox = new CheckBox("Starting Node");
        this.endingCheckBox = new CheckBox("End Node");

        name = "Node";
        nameText = new Text(name);
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);

        this.getChildren().addAll(circle, nameText);
        this.arrows = new ArrayList<>();
        this.board = board;
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
        nameText.setX(circle.getCenterX() - nameText.getBoundsInLocal().getWidth() / 2);
        nameText.setY(circle.getCenterY() + nameText.getBoundsInLocal().getHeight() / 4);
    }

    private void setStarting() {
        this.setStarting(true);
        this.startingCheckBox.setSelected(true);
        circle.setFill(Color.YELLOW);
        if (ending) {
            circle.setFill(Color.RED);
        }
    }

    public void unSetStarting() {
        this.setStarting(false);
        circle.setFill(Color.WHITE);
        if (ending) {
            circle.setFill(Color.BLUE);
        }
    }

    private void setEnding() {
        this.setEnding(true);
        circle.setFill(Color.BLUE);
        if (starting) {
            circle.setFill(Color.RED);
        }
    }

    private void unSetEnding() {
        this.setEnding(false);
        circle.setFill(Color.WHITE);
        if (starting) {
            circle.setFill(Color.YELLOW);
        }
    }

    public void showDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Circle");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        gridPane.add(startingCheckBox, 0, 0);
        gridPane.add(endingCheckBox, 0, 1);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (startingCheckBox.isSelected()) {
                    board.removeStartingFromOtherNodes();
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
}
