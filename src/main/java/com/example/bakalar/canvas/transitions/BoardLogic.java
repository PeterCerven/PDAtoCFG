package com.example.bakalar.canvas.transitions;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.character.TerminalSymbol;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.example.bakalar.canvas.arrow.Arrow.LAMDA;

public class BoardLogic {
    private Stack<TerminalSymbol> startingStack;
    private List<TerminalSymbol> inputAlphabet;
    private List<StepState> stepStates;
    private List<StepState> tmpStepStates;
    private Board board;
    private String begSymbol;
    private int currentAlphabetInputIndex;
    private HBox stateContainer;


    public BoardLogic(Board board, HBox stateContainer) {
        this.board = board;
        this.stateContainer = stateContainer;
        this.inputAlphabet = new ArrayList<>();
        this.stepStates = new ArrayList<>();
        this.tmpStepStates = new ArrayList<>();
        this.startingStack = new Stack<>();
    }

    public void start(String begSymbol, String inputAlphabet) {
        this.inputAlphabet = new ArrayList<>();
        this.stepStates = new ArrayList<>();
        this.tmpStepStates = new ArrayList<>();
        this.startingStack = new Stack<>();
        populateInputAlphabet(inputAlphabet);
        this.begSymbol = begSymbol;
        startingStack.push(new TerminalSymbol(begSymbol.charAt(0)));
        TextField stack = new TextField();
        stack.setText(begSymbol);
        currentAlphabetInputIndex = 0;
        MyNode currentNode = board.getStartNode();
        StepState stepState = new StepState(stack, currentNode, StateColor.NEUTRAL);
        this.stepStates.add(stepState);
        stateContainer.getChildren().clear();
        stateContainer.getChildren().add(stepState);
    }

    public void step() {
        TerminalSymbol inputLetterToRead = inputAlphabet.get(currentAlphabetInputIndex);
        for (StepState stepState : stepStates) {
            if (stepState.getState() == StateColor.ACCEPTED || stepState.getState() == StateColor.REJECTED) {
                continue;
            }
            char stackTop = stepState.getStack().getText().charAt(0);
            boolean foundWay = false;
            for (Arrow arrow : stepState.getCurrentNode().getArrowsFrom()) {
                char read = arrow.getRead().charAt(0);
                char pop = arrow.getPop().charAt(0);
                char push = arrow.getPush().charAt(0);
                TextField stack = new TextField();
                if (read == inputLetterToRead.getName() && (pop == stackTop || pop == LAMDA.charAt(0))) {
                    String newStackAfterPop = pop == LAMDA.charAt(0) ? stepState.getStack().getText() : stepState.getStack().getText().substring(1);
                    String newStackAfterPush = push == LAMDA.charAt(0) ? newStackAfterPop : push + newStackAfterPop;
                    stack.setText(newStackAfterPush);
                    MyNode currentNode = arrow.getTo();
                    StepState newStepState = stack.getText().isEmpty()
                            ? new StepState(stack, currentNode, StateColor.ACCEPTED)
                            : new StepState(stack, currentNode, StateColor.NEUTRAL);
                    tmpStepStates.add(newStepState);
                    stateContainer.getChildren().add(newStepState);
                    foundWay = true;
                }
            }
            if (!foundWay) {
                TextField stack = new TextField();
                stack.setText(stepState.getStack().getText());
                StepState newStepState = new StepState(stack, stepState.getCurrentNode(), StateColor.REJECTED);
                tmpStepStates.add(newStepState);
                stateContainer.getChildren().add(newStepState);
            }
        }
        stateContainer.getChildren().removeAll(stepStates);
        stepStates = tmpStepStates;
        tmpStepStates = new ArrayList<>();
        currentAlphabetInputIndex++;
    }


    private void populateInputAlphabet(String inputAlphabet) {
        for (int i = 0; i < inputAlphabet.length(); i++) {
            this.inputAlphabet.add(new TerminalSymbol(inputAlphabet.charAt(i)));
        }
    }


}
