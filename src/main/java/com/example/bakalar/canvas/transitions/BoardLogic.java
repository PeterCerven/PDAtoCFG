package com.example.bakalar.canvas.transitions;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.character.MySymbol;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import static com.example.bakalar.canvas.arrow.Arrow.EPSILON;

public class BoardLogic {
    private List<MySymbol> inputAlphabet;
    private List<StepState> stepStates;
    private List<StepState> tmpStepStates;
    private final Board board;
    private int currentAlphabetInputIndex;
    private final HBox stateContainer;


    public BoardLogic(Board board, HBox stateContainer) {
        this.board = board;
        this.stateContainer = stateContainer;
        this.inputAlphabet = new ArrayList<>();
        this.stepStates = new ArrayList<>();
        this.tmpStepStates = new ArrayList<>();
    }

    public void start(String begSymbol, String inputAlphabet) {
        stateContainer.getChildren().clear();
        this.inputAlphabet = new ArrayList<>();
        this.stepStates = new ArrayList<>();
        this.tmpStepStates = new ArrayList<>();
        populateInputAlphabet(inputAlphabet);
        Stack<MySymbol> symbolStack = new Stack<>();
        symbolStack.push(new MySymbol(begSymbol));
        currentAlphabetInputIndex = 0;
        MyNode currentNode = board.getStartNode();
        StepState stepState = new StepState(symbolStack, currentNode, StateColor.NEUTRAL);
        this.stepStates.add(stepState);
        stateContainer.getChildren().add(stepState);
    }

    public void step() {
        MySymbol inputLetterToRead = inputAlphabet.get(currentAlphabetInputIndex);
        for (StepState stepState : stepStates) {
            if (stepState.getState() == StateColor.ACCEPTED || stepState.getState() == StateColor.REJECTED) {
                continue;
            }
            MySymbol stackTop = stepState.getSymbolStack().peek();
            boolean foundWay = false;
            for (Arrow arrow : stepState.getCurrentNode().getArrows()) {
                Stack<MySymbol> symbolStack = new Stack<>();
                symbolStack.addAll(stepState.getSymbolStack());
                MySymbol read = new MySymbol(arrow.getRead());
                MySymbol pop = new MySymbol(arrow.getPop());
                List<MySymbol> pushSymbols = convertStringToListOfSymbols(arrow.getPush());
                if (read.equals(inputLetterToRead) && (pop.equals(stackTop) || Objects.equals(pop.getName(), EPSILON))) {
                    if (!Objects.equals(pop.getName(), EPSILON)) {
                        symbolStack.pop();
                    }
                    if (!Objects.equals(pushSymbols.get(0).getName(), EPSILON)) {
                        for (int i = pushSymbols.size() - 1; i >= 0; i--) {
                            symbolStack.push(pushSymbols.get(i));
                        }
                    }
                    MyNode currentNode = arrow.getTo();
                    StepState newStepState = symbolStack.isEmpty()
                            ? new StepState(symbolStack, currentNode, StateColor.ACCEPTED)
                            : new StepState(symbolStack, currentNode, StateColor.NEUTRAL);
                    tmpStepStates.add(newStepState);
                    stateContainer.getChildren().add(newStepState);
                    foundWay = true;
                }
            }
            if (!foundWay) {
                Stack<MySymbol> symbolStack = new Stack<>();
                symbolStack.addAll(stepState.getSymbolStack());
                StepState newStepState = new StepState(symbolStack, stepState.getCurrentNode(), StateColor.REJECTED);
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
            this.inputAlphabet.add(new MySymbol((String) inputAlphabet.subSequence(i, i + 1)));
        }
    }

    private List<MySymbol> convertStringToListOfSymbols(String pushString) {
        List<MySymbol> symbols = new ArrayList<>();
        for (int i = 0; i < pushString.length(); i++) {
            symbols.add(new MySymbol((String) pushString.subSequence(i, i + 1)));
        }
        return symbols;
    }


}
