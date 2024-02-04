package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.Board;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.character.MySymbol;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

import static com.example.bakalar.logic.Board.STARTING_Z;

public class ConversionLogic {
    private final Board board;
    private Stage transitionStage;
    private Label currentStateLabel;
    private Label transitionNumberLabel;
    private List<Transition> transitions;
    private VBox convertedTransitionBox;
    private Map<Integer, List<ConvertedTransitions>> convertedTransitions;
    private int currentIndex = 0;

    public ConversionLogic(Board board) {
        this.board = board;
    }

    public void convertPDA() {
        List<Transition> transitions = board.getNodesTransitions();
        transitions.add(0, new Transition(board.getStartNode().getName(), TransitionType.START));
        transitions.add(new Transition("qf", TransitionType.END));
        setupTransitionStage(transitions);
        showTransitionStage();
    }

    private void setupTransitionStage(List<Transition> transitions) {
        this.transitions = transitions;
        this.transitionStage = new Stage();
        this.currentStateLabel = new Label();
        this.transitionNumberLabel = new Label();
        this.convertedTransitionBox = new VBox();
        this.convertedTransitions = new HashMap<>();

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> updateTransition(1));

        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> updateTransition(-1));

        HBox buttonLayout = new HBox(10, prevButton, nextButton);
        buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);

        currentStateLabel.setFont(new Font("Arial", 16));

        transitionNumberLabel.setFont(new Font("Arial", 16));
        transitionNumberLabel.setAlignment(Pos.TOP_LEFT);

        for (Transition transition : transitions) {
            this.convertedTransitions.put(transitions.indexOf(transition), convertTransitions(transition));
        }

        VBox layout = new VBox(10, currentStateLabel, buttonLayout, convertedTransitionBox, transitionNumberLabel);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 600);
        transitionStage.setScene(scene);
        transitionStage.setTitle("Transition Details");
    }

    private void showTransitionStage() {
        updateTransition(0); // To initialize with the first transition
        transitionStage.show();
    }

    private void updateTransition(int step) {
        currentIndex += step;
        if (currentIndex < 0) currentIndex = 0;
        if (currentIndex >= transitions.size()) currentIndex = transitions.size() - 1;

        Transition currentTransition = transitions.get(currentIndex);
        currentStateLabel.setText(currentTransition.toString());
        transitionNumberLabel.setText("Transition " + (currentIndex + 1) + "/" + transitions.size());
        List<ConvertedTransitions> convertedTransitions = this.convertedTransitions.get(currentIndex);
        convertedTransitionBox.getChildren().clear();
        for (ConvertedTransitions convertedTransition : convertedTransitions) {
            TextField textField = new TextField(convertedTransition.toString());
            textField.setEditable(false);
            textField.setFont(new Font("Arial", 16));
            convertedTransitionBox.getChildren().add(textField);
        }
    }

    private List<ConvertedTransitions> convertTransitions(Transition transition) {
        List<ConvertedTransitions> convertedTransitions = new ArrayList<>();
        switch (transition.getTransitionType()) {
            case START -> convertedTransitions.addAll(startMove(transition));
            case END -> convertedTransitions.addAll(endMove(transition));
            case NORMAL -> convertedTransitions.addAll(pushMove(transition));
            case TERMINAL -> convertedTransitions.addAll(terminalMove(transition));
        }
        return convertedTransitions;
    }

    private List<ConvertedTransitions> terminalMove(Transition transition) {
        ConvertedTransitions convertedTransition = new ConvertedTransitions();
        convertedTransition.setLeftSide(new MySymbol("[" + transition.getCurrentState() + ", " + transition.getSymbolToPop() + ", " + transition.getNextState() + "]"));
        convertedTransition.setRightSide(List.of(transition.getInputSymbolToRead()));
        return List.of(convertedTransition);
    }

    private List<ConvertedTransitions> endMove(Transition transition) {
        return new ArrayList<>();
    }

    private List<ConvertedTransitions> pushMove(Transition transition) {
        List<ConvertedTransitions> convertedTransitions = new ArrayList<>();
        recursiveLoop(transition.getSymbolsToPush().size(), board.getNodes().size(), new String[board.getNodes().size()], 0, transition, convertedTransitions);
        return convertedTransitions;
    }

    private void recursiveLoop(int totalLoops, int iterations, String[] statesPos, int currentLoop, Transition transition, List<ConvertedTransitions> convertedTransitions) {
        if (currentLoop == totalLoops) {
            ConvertedTransitions convertedTransition = createRule(statesPos, transition);
            convertedTransitions.add(convertedTransition);
            return;
        }

        for (int i = 0; i < iterations; i++) {
            statesPos[currentLoop] = board.getNodes().get(i).getName();
            recursiveLoop(totalLoops, iterations, statesPos, currentLoop + 1, transition, convertedTransitions);
        }
    }

    private ConvertedTransitions createRule(String[] statesPos, Transition transition) {
        ConvertedTransitions convertedTransition = new ConvertedTransitions();
        int posCounter = 0;

        MySymbol leftSide = new MySymbol("[" + transition.getCurrentState() + ", "
                + transition.getSymbolToPop() + ", "
                + statesPos[posCounter++] + "]");

        List<MySymbol> rightSide = new ArrayList<>();
        if (!transition.getInputSymbolToRead().getName().equals("ε")) {
            rightSide.add(transition.getInputSymbolToRead());
        }
        MySymbol nextStateSymbol = new MySymbol(transition.getNextState().getName());
        for (int i = 0; i < transition.getSymbolsToPush().size(); i++) {
            if (i == transition.getSymbolsToPush().size() - 1) {
                rightSide.add(new MySymbol("[" + nextStateSymbol + ", " + transition.getSymbolsToPush().get(i) + ", " + statesPos[0] + "]"));
                break;
            }
            rightSide.add(new MySymbol("[" + nextStateSymbol + ", " + transition.getSymbolsToPush().get(i) + ", " + statesPos[posCounter] + "]"));
            nextStateSymbol = new MySymbol(statesPos[posCounter]);
            posCounter++;
        }

        convertedTransition.setLeftSide(leftSide);
        convertedTransition.setRightSide(rightSide);
        return convertedTransition;
    }


    private List<ConvertedTransitions> startMove(Transition transition) {
        List<MySymbol> allStateSymbols = board.getNodes().stream().map(node -> new MySymbol(node.getName())).toList();
        List<ConvertedTransitions> convertedTransitions = new ArrayList<>();
        MySymbol startSymbol = new MySymbol("S");
        MySymbol startStackSymbol = new MySymbol(STARTING_Z);
        for (MySymbol stateSymbol : allStateSymbols) {
            ConvertedTransitions convertedTransition = new ConvertedTransitions();

            // Construct the left-hand side of the CFG rule

            convertedTransition.setLeftSide(startSymbol);

            // Construct the right-hand side of the CFG rule
            List<MySymbol> rightSide = new ArrayList<>();
            rightSide.add(new MySymbol("[" + transition.getCurrentState() + ", " + startStackSymbol + ", " + stateSymbol + "]"));
            convertedTransition.setRightSide(rightSide);
            convertedTransitions.add(convertedTransition);
        }
        return convertedTransitions;
    }


}
