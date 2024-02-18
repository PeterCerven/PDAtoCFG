package com.example.bakalar.logic.transitions;

import com.example.bakalar.logic.MainController;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.utility.MySymbol;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class StepState extends Pane {
    private static final Logger log = LogManager.getLogger(MainController.class.getName());
    private MyNode currentNode;
    private TextField stack;
    private Stack<MySymbol> symbolStack;
    private Circle circle;
    private Text nodeName;
    private StateColor state;

    public StepState(Stack<MySymbol> symbolStack, MyNode currentNode, StateColor state) {
        this.currentNode = currentNode;
        this.symbolStack = symbolStack;
        this.stack = convertToTextField(symbolStack);
        this.state = state;


        initCircle();
        initTextField();
        initStackField();

        this.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: " + state.getColor() + "; -fx-background-radius: 5;");
        this.getChildren().addAll(stack, circle, nodeName);
    }

    private TextField convertToTextField(List<MySymbol> symbolStack) {
        StringBuilder sb = new StringBuilder();
        for (MySymbol symbol : symbolStack) {
            sb.append(symbol.getName());
        }
        TextField stack = new TextField();
        stack.setText(sb.toString());
        stack.setAlignment(Pos.CENTER);
        stack.setEditable(false);
        stack.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: white; -fx-background-radius: 5;");
        stack.setLayoutX(10);
        stack.setLayoutY(50);
        return stack;
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

    private void initStackField() {
        this.stack.setAlignment(Pos.CENTER);
        this.stack.setEditable(false);
        this.stack.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: white; -fx-background-radius: 5;");
        this.stack.setLayoutX(10);
        this.stack.setLayoutY(50);
    }


}
