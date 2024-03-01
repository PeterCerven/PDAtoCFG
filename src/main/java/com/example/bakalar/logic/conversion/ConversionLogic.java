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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bakalar.logic.Board.*;

public class ConversionLogic {
    private final Board board;
    private Stage transitionStage;
    private TextFlow transitionLabel;
    private HBox transitionLabelBox;
    private Label transitionIndexLabel;
    private List<Transition> transitions;
    private VBox ruleBox;
    private Map<Integer, List<CFGRule>> cfgRules;
    private int currentIndex = 0;
    private int currentStep = 0;

    public ConversionLogic(Board board) {
        this.board = board;
    }

    public void convertPDA() {
        List<Transition> transitions = board.getNodesTransitions();
        transitions.add(0, new Transition(board.getStartNode().getName(), TransitionType.START));
        setupTransitionStage(transitions);
        showTransitionStage();
    }

    private void setupTransitionStage(List<Transition> transitions) {
        this.transitions = transitions;
        this.transitionStage = new Stage();
        this.transitionLabel = new TextFlow();
        this.transitionIndexLabel = new Label();
        this.ruleBox = new VBox();
        this.cfgRules = new HashMap<>();
        this.transitionLabelBox = new HBox(10, transitionLabel);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> updateTransition(1));

        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> updateTransition(-1));

        HBox buttonLayout = new HBox(10, prevButton, nextButton);
        buttonLayout.setAlignment(Pos.TOP_RIGHT);

        Button showStepsButton = new Button("Show Steps");
        showStepsButton.setOnAction(e -> showSteps());

        Button nextSteps = new Button("Next Steps");
        nextSteps.setOnAction(e -> updateStep(1));

        Button prevSteps = new Button("Previous Steps");
        prevSteps.setOnAction(e -> updateStep(-1));

        HBox stepsLayout = new HBox(10, showStepsButton, prevSteps, nextSteps);
        stepsLayout.setAlignment(Pos.BOTTOM_RIGHT);

        VBox helpingComments = new VBox();
        helpingComments.setAlignment(Pos.CENTER_LEFT);
        helpingComments.getChildren().add(new Label("Red color means the symbol is being processed"));


        transitionIndexLabel.setFont(new Font("Arial", 22));
        transitionLabelBox.setAlignment(Pos.CENTER);


        for (Transition transition : transitions) {
            this.cfgRules.put(transitions.indexOf(transition), convertTransitions(transition));
        }

        VBox topMenu = new VBox(10, transitionLabelBox, buttonLayout, transitionIndexLabel);
        topMenu.setAlignment(Pos.TOP_CENTER);

        VBox layout = new VBox(ruleBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setLeft(helpingComments);
        root.setCenter(layout);
        root.setTop(topMenu);
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
        currentStep = 0;

        Transition currentTransition = transitions.get(currentIndex);
        transitionLabel.getChildren().clear();
        Text labelText = new Text();
        labelText.setFont(new Font("Courier New", 25));
        switch (currentTransition.getTransitionType()) {
            case START -> {
                labelText.setText("Start transition");
                transitionLabel.getChildren().add(labelText);
            }
            default -> {
                List<Text> transitionTexts = currentTransition.createTextFromStep();
                String transitionText = transitionTexts.stream().map(Text::getText).reduce("", String::concat);
                labelText.setText(transitionText);
                transitionLabel.getChildren().add(labelText);
            }
        }
        transitionIndexLabel.setText("Transition " + (currentIndex + 1) + "/" + transitions.size());
        List<CFGRule> cfgRules = this.cfgRules.get(currentIndex);
        ruleBox.getChildren().clear();
        for (CFGRule cfgRule : cfgRules) {
            TextFlow textFlow = new TextFlow();
            Text text = new Text(cfgRule.toString());
            text.setFont(new Font("Courier New", 22));
            textFlow.getChildren().add(text);
            ruleBox.getChildren().add(textFlow);
        }
    }


    private List<CFGRule> convertTransitions(Transition transition) {
        List<CFGRule> cfgRules = new ArrayList<>();
        switch (transition.getTransitionType()) {
            case START -> cfgRules.addAll(startMove(transition));
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
        int posCounter = 0;
        CFGRuleStep cfgRuleStep = new CFGRuleStep(transition);


        // left side
        SpecialNonTerminal leftSide = new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(),
                statesPos[posCounter]);

        // steps for left side
        SpecialNonTerminal leftSideStep1 = new SpecialNonTerminal(new MySymbol(transition.getCurrentState(), Color.RED),
                new MySymbol("_"), new MySymbol("_"));
        SpecialNonTerminal leftSideStep2 = new SpecialNonTerminal(new MySymbol(transition.getCurrentState().getName()),
                new MySymbol(transition.getSymbolToPop(), Color.RED), new MySymbol("_"));
        SpecialNonTerminal leftSideStep3 = new SpecialNonTerminal(new MySymbol(transition.getCurrentState().getName()),
                new MySymbol(transition.getSymbolToPop().getName()), new MySymbol(statesPos[posCounter], Color.RED));

        cfgRuleStep.addLeftSideStep(leftSideStep1, "current");
        cfgRuleStep.addLeftSideStep(leftSideStep2, "pop");

        posCounter++;

        // terminal
        List<SpecialNonTerminal> rightSide = new ArrayList<>();
        MySymbol terminal = new MySymbol("");
        if (!transition.getInputSymbolToRead().getName().equals(EPSILON)) {
            terminal = new MySymbol(transition.getInputSymbolToRead().getName());
        }

        // steps for terminal
        cfgRuleStep.addTerminal(new MySymbol(terminal, Color.RED));


        // right side
        MySymbol nextStateSymbol = new MySymbol(transition.getNextState().getName());

        // steps for right side stack symbols
        cfgRuleStep.addRightSideStackSymbols(transition.getSymbolsToPush(), Color.RED);
        cfgRuleStep.addFirstRightSideStep(new MySymbol(nextStateSymbol.getName(), Color.RED));
        List<MySymbol> tableOptions = new ArrayList<>();

        for (int i = 0; i < transition.getSymbolsToPush().size(); i++) {
            if (i == transition.getSymbolsToPush().size() - 1) {
                rightSide.add(new SpecialNonTerminal(nextStateSymbol, transition.getSymbolsToPush().get(i), statesPos[0]));
                break;
            }
            // steps for right side
            tableOptions.add(new MySymbol(statesPos[posCounter], Color.RED, i));

            rightSide.add(new SpecialNonTerminal(nextStateSymbol, transition.getSymbolsToPush().get(i), statesPos[posCounter]));
            nextStateSymbol = new MySymbol(statesPos[posCounter]);
            posCounter++;
        }

        // All possibilities
        cfgRuleStep.addAllPossibilities(leftSideStep3, tableOptions);
        cfgRuleStep.addPossibilitiesAnotherSide(tableOptions);

        // Add lastRight step


        cfgRuleStep.addLastRightStep(leftSideStep3);

        CFGRule cfgRule = new CFGRule(leftSide, terminal, rightSide, transition);
        cfgRule.setSteps(cfgRuleStep.getCfgRulesSteps());
        cfgRule.setStepsTransitions(cfgRuleStep.getStepsTransitions());

        return cfgRule;
    }

    // moves


    private List<CFGRule> terminalMove(Transition transition) {
        CFGRuleStep cfgRuleStep = new CFGRuleStep(transition);
        // TODO: add steps for terminal move
        SpecialNonTerminal leftSide = new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), transition.getNextState());
        MySymbol terminal = new MySymbol(transition.getInputSymbolToRead().getName());
        List<SpecialNonTerminal> rightSide = new ArrayList<>();
        CFGRule cfgRule = new CFGRule(leftSide, terminal, rightSide, transition);
        return List.of(cfgRule);
    }

    private List<CFGRule> pushMove(Transition transition) {
        List<CFGRule> convertedTransitions = new ArrayList<>();
        recursiveLoop(transition.getSymbolsToPush().size(), board.getNodes().size(), new String[transition.getSymbolsToPush().size()],
                0, transition, convertedTransitions);

        return convertedTransitions;
    }


    private List<CFGRule> startMove(Transition transition) {
        CFGRuleStep cfgRuleStep = new CFGRuleStep(transition);
        // TODO: add steps for start move
        List<MySymbol> allStateSymbols = board.getNodes().stream().map(node -> new MySymbol(node.getName())).toList();
        List<CFGRule> cfgRules = new ArrayList<>();
        MySymbol startSymbol = new MySymbol(STARTING_S);
        MySymbol startStackSymbol = new MySymbol(STARTING_Z);
        for (MySymbol stateSymbol : allStateSymbols) {
            List<SpecialNonTerminal> rightSide = new ArrayList<>();
            rightSide.add(new SpecialNonTerminal(transition.getCurrentState(), startStackSymbol, stateSymbol));
            CFGRule cfgRule = new CFGRule(startSymbol, null, rightSide, transition);
            cfgRules.add(cfgRule);
        }
        return cfgRules;
    }

    // steps
    private void showSteps() {
        updateStep(currentStep);
    }

    private void updateStep(int step) {
        currentStep += step;
        List<CFGRule> cfgRules = this.cfgRules.get(currentIndex);
        if (currentStep < 0) currentStep = 0;
        if (currentStep >= cfgRules.get(0).getSteps().size()) currentStep = cfgRules.get(0).getSteps().size() - 1;
        ruleBox.getChildren().clear();
        for (CFGRule cfgRule : cfgRules) {
            CFGRule cfgStep = cfgRule.getSteps().get(currentStep);
            Transition transition = cfgRule.getStepsTransitions().get(currentStep);
            TextFlow textFlow = new TextFlow();
            List<Text> texts = cfgStep.createTextFromStep();
            List<Text> transitionTexts = transition.createTextFromStep();


            transitionLabel.getChildren().clear();
            transitionLabel.getChildren().addAll(transitionTexts);
            textFlow.getChildren().addAll(texts);
            ruleBox.getChildren().add(textFlow);
        }

    }


}
