package com.example.bakalar.canvas.transitions;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.character.TerminalSymbol;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class StepState extends Group {
    private VBox stackContainer;
    private HBox inputAlphabetContainer;
    private MyNode currentNode;
    private Stack<TerminalSymbol> stack;
    private List<TerminalSymbol> inputAlphabet;
    private Circle circle;
    private Text nodeName;

    public StepState(Stack<TerminalSymbol> stack, List<TerminalSymbol> inputAlphabet, MyNode currentNode) {
        this.currentNode = currentNode;
        this.stack = stack;
        this.inputAlphabet = inputAlphabet;

        initStack();
        initInputAlphabet();
        initCircle();
        initTextField();


        this.getChildren().addAll(stackContainer, inputAlphabetContainer, circle, nodeName);
    }

    private void initCircle() {
        this.circle = new Circle(20);
        this.circle.setCenterX(20);
        this.circle.setCenterY(20);
        this.circle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 2;");
    }

    private void initTextField() {
        this.nodeName = new Text(currentNode.getName());
        this.nodeName.setX(20 - nodeName.getBoundsInLocal().getWidth() / 2);
        this.nodeName.setY(20 + nodeName.getBoundsInLocal().getHeight() / 4);
        this.nodeName.setStyle("-fx-border-color: black;");
    }

    private void initInputAlphabet() {
        this.inputAlphabetContainer = new HBox();
        this.inputAlphabetContainer.setLayoutX(0);
        this.inputAlphabetContainer.setLayoutY(40);

        for (int i = 0; i < 10; i++) {
            TextField textField = new TextField();
            textField.setAlignment(Pos.CENTER);
            textField.setPrefWidth(5);
            textField.setPrefHeight(5);
            textField.setStyle("-fx-border-color: black;");
            textField.setEditable(false);
            inputAlphabetContainer.getChildren().add(textField);
        }
    }

    public void updateStack(Stack<TerminalSymbol> stack) {
        for (int i = 0; i < 10; i++) {
            TextField textField = (TextField) this.stackContainer.getChildren().get(i);
            if (i < stack.size()) {
                textField.setText(stack.get(i).toString());
            } else {
                textField.setText("");
            }
        }
    }

    private void initStack() {
        this.stackContainer = new VBox();
        this.stackContainer.setLayoutX(170);
        this.stackContainer.setLayoutY(0);
        for (int i = 0; i < 10; i++) {
            TextField textField = new TextField();
            textField.setPrefWidth(10);
            textField.setPrefHeight(10);
            textField.setAlignment(Pos.CENTER);
            textField.setStyle("-fx-border-color: black;");
            textField.setEditable(false);
            stackContainer.getChildren().add(textField);
        }
    }


}
