package com.example.bakalar.canvas.conversion;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.transitions.Transition;
import com.example.bakalar.character.MySymbol;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bakalar.canvas.Board.STARTING_Z;

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
            convertedTransitionBox.getChildren().add(new TextField(convertedTransition.toString()));
        }
    }

    private List<ConvertedTransitions> convertTransitions(Transition transition) {
        List<ConvertedTransitions> convertedTransitions = new ArrayList<>();
        switch (transition.getTransitionType()) {
            case START -> convertedTransitions.addAll(startMove(transition));
            case END -> convertedTransitions.addAll(endMove(transition));
            case NORMAL -> convertedTransitions.addAll(pushMove(transition));
        }
        return convertedTransitions;
    }

    private List<ConvertedTransitions> endMove(Transition transition) {
        return new ArrayList<>();
    }

    private List<ConvertedTransitions> pushMove(Transition transition) {
        List<MySymbol> allStateSymbols = board.getNodes().stream().map(node -> new MySymbol(node.getName())).toList();
        List<ConvertedTransitions> convertedTransitions = new ArrayList<>();
        // convert transition to cfg rules
        for (MySymbol stateSymbol : allStateSymbols) {
            for (MySymbol nextStateSymbol : allStateSymbols) {
                ConvertedTransitions convertedTransition = new ConvertedTransitions();

                // Construct the left-hand side of the CFG rule
                MySymbol leftSide = new MySymbol("[" + transition.getCurrentState() + ", "
                        + transition.getSymbolToPop() + ", "
                        + stateSymbol + "]");
                convertedTransition.setLeftSide(leftSide);

                // Construct the right-hand side of the CFG rule
                List<MySymbol> rightSide = new ArrayList<>();
                if (!transition.getInputSymbolToRead().getName().equals("ε")) {
                    rightSide.add(transition.getInputSymbolToRead());
                }

                MySymbol startSymbol = transition.getNextState();
                MySymbol endSymbol = nextStateSymbol;
                for (MySymbol symbolToPush : transition.getSymbolsToPush()) {
                    rightSide.add(new MySymbol("[" + startSymbol + ", " + symbolToPush + ", " + endSymbol + "]"));
                    startSymbol = endSymbol;
                    endSymbol = stateSymbol;
                }

                if (transition.getSymbolsToPush().isEmpty()) {
                    rightSide.add(new MySymbol("ε"));
                }

                convertedTransition.setRightSide(rightSide);
                convertedTransitions.add(convertedTransition);
            }
        }
        return convertedTransitions;
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
