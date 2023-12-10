package com.example.bakalar.canvas.transitions;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.character.TerminalSymbol;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    public StepState(Stack<TerminalSymbol> stack, List<TerminalSymbol> inputAlphabet, MyNode currentNode) {
        this.stackContainer = new VBox();
        this.inputAlphabetContainer = new HBox();
        this.currentNode = currentNode;
        this.stack = stack;
        this.inputAlphabet = inputAlphabet;
        this.getChildren().addAll(stackContainer, inputAlphabetContainer, currentNode);
    }



}
