package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.Board;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bakalar.logic.Board.STARTING_Z;

public class ConversionLogic {
    private final Board board;
    private Stage transitionStage;
    private Label transitionLabel;
    private Label transitionIndexLabel;
    private List<Transition> transitions;
    private VBox ruleBox;
    private Map<Integer, List<CFGRule>> cfgRules;
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
        this.transitionLabel = new Label();
        this.transitionIndexLabel = new Label();
        this.ruleBox = new VBox();
        this.cfgRules = new HashMap<>();

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> updateTransition(1));

        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> updateTransition(-1));

        HBox buttonLayout = new HBox(10, prevButton, nextButton);
        buttonLayout.setAlignment(Pos.TOP_RIGHT);

        Button showStepsButton = new Button("Show Steps");
        showStepsButton.setOnAction(e -> showSteps());

        Button nextSteps = new Button("Next Steps");
        nextSteps.setOnAction(e -> step(1));

        Button prevSteps = new Button("Previous Steps");
        prevSteps.setOnAction(e -> step(-1));

        HBox stepsLayout = new HBox(10, showStepsButton, nextSteps, prevSteps);
        stepsLayout.setAlignment(Pos.BOTTOM_RIGHT);

        transitionLabel.setFont(new Font("Arial", 16));

        transitionIndexLabel.setFont(new Font("Arial", 16));
        transitionIndexLabel.setAlignment(Pos.TOP_LEFT);

        for (Transition transition : transitions) {
            this.cfgRules.put(transitions.indexOf(transition), convertTransitions(transition));
        }

        VBox layout = new VBox(10, transitionLabel, buttonLayout, transitionIndexLabel, ruleBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(layout);
        root.setBottom(stepsLayout);

        Scene scene = new Scene(root, 800, 600);
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
        switch (currentTransition.getTransitionType()) {
            case START -> transitionLabel.setText("Start transition");
            case END -> transitionLabel.setText("End transition");
            default -> transitionLabel.setText(currentTransition.toString());
        }
        transitionIndexLabel.setText("Transition " + (currentIndex + 1) + "/" + transitions.size());
        List<CFGRule> convertedTransitions = this.cfgRules.get(currentIndex);
        ruleBox.getChildren().clear();
        for (CFGRule convertedTransition : convertedTransitions) {
            TextField textField = new TextField(convertedTransition.toString());
            textField.setEditable(false);
            textField.setFont(new Font("Arial", 16));
            ruleBox.getChildren().add(textField);
        }
    }


    private List<CFGRule> convertTransitions(Transition transition) {
        List<CFGRule> cfgRules = new ArrayList<>();
        switch (transition.getTransitionType()) {
            case START -> cfgRules.addAll(startMove(transition));
            case END -> cfgRules.addAll(endMove(transition));
            case NORMAL -> cfgRules.addAll(pushMove(transition));
            case TERMINAL -> cfgRules.addAll(terminalMove(transition));
        }
        return cfgRules;
    }

    private void recursiveLoop(int totalLoops, int iterations, String[] statesPos, int currentLoop, Transition transition, List<CFGRule> cfgRules) {
        if (currentLoop == totalLoops) {
            CFGRule cfgRule = createRule(statesPos, transition);
            cfgRules.add(cfgRule);
            return;
        }

        for (int i = 0; i < iterations; i++) {
            statesPos[currentLoop] = board.getNodes().get(i).getName();
            recursiveLoop(totalLoops, iterations, statesPos, currentLoop + 1, transition, cfgRules);
        }
    }

    private CFGRule createRule(String[] statesPos, Transition transition) {
        CFGRule cfgRule = new CFGRule();
        int posCounter = 0;

        SpecialNonTerminal leftSide = new SpecialNonTerminal(transition.getCurrentState(),
                transition.getSymbolToPop(), statesPos[posCounter++]);

        List<SpecialNonTerminal> rightSide = new ArrayList<>();
        String terminal = "";
        if (!transition.getInputSymbolToRead().getName().equals("ε")) {
            terminal = transition.getInputSymbolToRead().getName();
        }
        MySymbol nextStateSymbol = new MySymbol(transition.getNextState().getName());
        for (int i = 0; i < transition.getSymbolsToPush().size(); i++) {
            if (i == transition.getSymbolsToPush().size() - 1) {
                rightSide.add(new SpecialNonTerminal(nextStateSymbol, transition.getSymbolsToPush().get(i), statesPos[0]));
                break;
            }
            rightSide.add(new SpecialNonTerminal(nextStateSymbol, transition.getSymbolsToPush().get(i), statesPos[posCounter]));
            nextStateSymbol = new MySymbol(statesPos[posCounter]);
            posCounter++;
        }

        cfgRule.setLeftSide(leftSide);
        cfgRule.setTerminal(terminal);
        cfgRule.setRightSide(rightSide);
        return cfgRule;
    }

    // moves


    private List<CFGRule> terminalMove(Transition transition) {
        CFGRule cfgRule = new CFGRule();
        cfgRule.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), transition.getNextState()));
        cfgRule.setTerminal(transition.getInputSymbolToRead().getName());
        cfgRule.setRightSide(new ArrayList<>());
        return List.of(cfgRule);
    }

    private List<CFGRule> endMove(Transition transition) {
        return new ArrayList<>();
    }

    private List<CFGRule> pushMove(Transition transition) {
        List<CFGRule> convertedTransitions = new ArrayList<>();
        recursiveLoop(transition.getSymbolsToPush().size(), board.getNodes().size(), new String[board.getNodes().size()], 0, transition, convertedTransitions);
        return convertedTransitions;
    }


    private List<CFGRule> startMove(Transition transition) {
        List<MySymbol> allStateSymbols = board.getNodes().stream().map(node -> new MySymbol(node.getName())).toList();
        List<CFGRule> cfgRules = new ArrayList<>();
        MySymbol startSymbol = new MySymbol("S");
        MySymbol startStackSymbol = new MySymbol(STARTING_Z);
        for (MySymbol stateSymbol : allStateSymbols) {
            CFGRule cfgRule = new CFGRule();
            cfgRule.setMySymbolLeftSide(startSymbol);
            List<SpecialNonTerminal> rightSide = new ArrayList<>();
            rightSide.add(new SpecialNonTerminal(transition.getCurrentState(), startStackSymbol, stateSymbol));
            cfgRule.setRightSide(rightSide);
            cfgRules.add(cfgRule);
        }
        return cfgRules;
    }

    // steps

    private void showSteps() {
        ruleBox.getChildren().clear();
    }

    private void step(int i) {
    }


}
