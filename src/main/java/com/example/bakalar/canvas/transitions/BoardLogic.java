package com.example.bakalar.canvas.transitions;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.character.TerminalSymbol;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoardLogic {
    private Stack<TerminalSymbol> startingStack;
    private List<TerminalSymbol> inputAlphabet;
    private List<StepState> stepStates;
    private Board board;
    private String begSymbol;
    private int currentState;
    private MyNode currentNode;
    private HBox stateContainer;


    public BoardLogic(Board board, HBox stateContainer) {
        this.board = board;
        this.stateContainer = stateContainer;
    }

    public void start(String begSymbol, String inputAlphabet) {
        populateInputAlphabet(inputAlphabet);
        this.stepStates = new ArrayList<>();
        this.begSymbol = begSymbol;
        startingStack = new Stack<>();
        startingStack.push(new TerminalSymbol(begSymbol.charAt(0)));
        currentState = 0;
        currentNode = board.getStartNode();
        StepState stepState = new StepState(startingStack, this.inputAlphabet, currentNode);
        this.stepStates.add(stepState);
        stateContainer.getChildren().add(stepState);
    }

    public void step() {
        TerminalSymbol input = inputAlphabet.get(currentState);
        TerminalSymbol stackTop = startingStack.peek();
        List<NodeTransition> nodeTransitions = currentNode.getTransitions();
        for (NodeTransition nodeTransition : nodeTransitions) {
            TerminalSymbol read = new TerminalSymbol(nodeTransition.getRead().charAt(0));
            TerminalSymbol pop = new TerminalSymbol(nodeTransition.getPop().charAt(0));
            if (input.equals(read) && stackTop.equals(pop)) {
                startingStack.pop();
                startingStack.push(new TerminalSymbol(nodeTransition.getPush().charAt(0)));
//                currentNode = board.getNode(nodeTransition.getTo());
                break;
            }

        }


//        board.updateStack(stack);
        currentState++;
    }

    private void step(StepState stepState) {

    }



    private void populateInputAlphabet(String inputAlphabet) {
        for (int i = 0; i < inputAlphabet.length(); i++) {
            this.inputAlphabet.add(new TerminalSymbol(inputAlphabet.charAt(i)));
        }
    }




}
