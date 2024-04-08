package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.Board;
import com.example.bakalar.logic.conversion.window.ConversionStage;
import com.example.bakalar.logic.conversion.window.ConversionWindow;
import com.example.bakalar.logic.conversion.window.InformationWindow;
import com.example.bakalar.logic.conversion.window.TransitionWindow;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.example.bakalar.logic.Board.*;

public class ConversionLogic {
    private static final Logger log = LogManager.getLogger(ConversionLogic.class.getName());
    private final Board board;
    private final ConversionUI conversionUI;
    private List<Transition> transitions;
    private boolean showSteps = false;
    private Map<Integer, List<CFGRule>> cfgRules;
    private int currentIndex;
    private List<ConversionWindow> conversionWindows;
    private TransitionWindow transitionWindow;
    private InformationWindow informationWindow;
    private ConversionStage conversionStage;


    public ConversionLogic(Board board) {
        this.conversionWindows = new ArrayList<>();
        this.board = board;
        this.conversionUI = new ConversionUI();
        this.transitionWindow = new TransitionWindow();
        this.informationWindow = new InformationWindow();
    }

    public void convertPDA() {
        currentIndex = 0;
        List<Transition> transitions = board.getNodesTransitions();
        if (transitions.isEmpty()) {
            board.showErrorDialog("Nemožno previesť Nemáte žiadne prechodové funkcie.");
            return;
        }
        if (board.getStartNode() == null) {
            board.showErrorDialog("Nemožno previesť Nemáte žiadny začiatočný stav.");
            return;
        }
        transitions.add(0, new Transition(board.getStartNode().getName(), TransitionType.START));
        setupTransitionStage(transitions);
        showTransitionStage();
        board.showCFG(getAllNonTerminals(), getAllTerminals(), getAllRules(), STARTING_S);
    }

    private Set<SpecialNonTerminal> getAllNonTerminals() {
        Set<SpecialNonTerminal> nonTerminals = new HashSet<>();
        for (List<CFGRule> lists : cfgRules.values()) {
            for (CFGRule rule : lists) {
                if (rule.getLeftSide() != null)
                    nonTerminals.add(rule.getLeftSide());
                nonTerminals.addAll(rule.getRightSide());
            }
        }
        return nonTerminals;
    }

    private List<CFGRule> getAllRules() {
        List<CFGRule> allRules = new ArrayList<>();
        for (List<CFGRule> lists : cfgRules.values()) {
            allRules.addAll(lists);
        }
        return allRules;
    }

    private Set<MySymbol> getAllTerminals() {
        Set<MySymbol> terminals = new HashSet<>();
        for (CFGRule rule : getAllRules()) {
            if (rule.getTerminal() != null) {
                terminals.add(rule.getTerminal());
            }
        }
        return terminals;
    }


    private void setupTransitionStage(List<Transition> transitions) {
        this.transitions = transitions;
        for (Transition transition : transitions) {
            this.cfgRules.put(transitions.indexOf(transition), convertTransitions(transition));
        }

        Scene scene = conversionStage.getScene();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                updateTransition(1);
            } else if (e.getCode() == KeyCode.LEFT) {
                updateTransition(-1);
            }
        });

        Button prevButton = conversionStage.getPreviousButton();
        Button nextButton = conversionStage.getNextButton();
        Button showStepsButton = transitionWindow.getShowStepsButton();

        prevButton.setOnAction(e -> updateTransition(-1));
        nextButton.setOnAction(e -> updateTransition(1));
        showStepsButton.setOnAction(e -> steps());
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
        RuleStepLogic ruleStepLogic = new RuleStepLogic(transition);
        ruleStepLogic.prepareSteps();


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

        ruleStepLogic.addLeftSideStep(leftSideStep1, "current");
        ruleStepLogic.addLeftSideStep(leftSideStep2, "pop");

        posCounter++;

        // terminal
        List<SpecialNonTerminal> rightSide = new ArrayList<>();
        MySymbol terminal = new MySymbol("");
        if (!transition.getInputSymbolToRead().getName().equals(EPSILON)) {
            terminal = new MySymbol(transition.getInputSymbolToRead().getName());
        }

        // steps for terminal
        ruleStepLogic.addTerminal(new MySymbol(terminal, Color.RED));


        // right side
        MySymbol nextStateSymbol = new MySymbol(transition.getNextState().getName());

        // steps for right side stack symbols
        ruleStepLogic.addRightSideStackSymbols(transition.getSymbolsToPush(), Color.RED);
        ruleStepLogic.addFirstRightSideStep(new MySymbol(nextStateSymbol.getName(), Color.RED));
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
        ruleStepLogic.addAllPossibilities(leftSideStep3, tableOptions);
        if (transition.getSymbolsToPush().size() > 1) {
            ruleStepLogic.addPossibilitiesAnotherSide(tableOptions);
        }

        // Add lastRight step


        ruleStepLogic.addLastRightStep(leftSideStep3);

        CFGRule cfgRule = new CFGRule(leftSide, terminal, rightSide, transition);
        cfgRule.setSteps(ruleStepLogic.getStepRules());

        return cfgRule;
    }

    // moves


    private List<CFGRule> terminalMove(Transition transition) {
        RuleStepLogic ruleStepLogic = new RuleStepLogic(transition);
        SpecialNonTerminal leftSide = new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), transition.getNextState());
        MySymbol terminal = new MySymbol(transition.getInputSymbolToRead().getName());
        List<SpecialNonTerminal> rightSide = new ArrayList<>();
        ruleStepLogic.createTerminalSteps();
        CFGRule cfgRule = new CFGRule(leftSide, terminal, rightSide, transition);
        cfgRule.setSteps(ruleStepLogic.getStepRules());
        return List.of(cfgRule);
    }

    private List<CFGRule> pushMove(Transition transition) {
        List<CFGRule> convertedTransitions = new ArrayList<>();
        recursiveLoop(transition.getSymbolsToPush().size(), board.getNodes().size(), new String[transition.getSymbolsToPush().size()],
                0, transition, convertedTransitions);

        return convertedTransitions;
    }


    private List<CFGRule> startMove(Transition transition) {
        List<MySymbol> allStateSymbols = board.getNodes().stream().map(node -> new MySymbol(node.getName())).toList();
        List<CFGRule> cfgRules = new ArrayList<>();
        MySymbol startSymbol = new MySymbol(STARTING_S);
        MySymbol startStackSymbol = new MySymbol(STARTING_Z);
        for (MySymbol stateSymbol : allStateSymbols) {
            RuleStepLogic ruleStepLogic = new RuleStepLogic(transition);
            List<SpecialNonTerminal> rightSide = new ArrayList<>();
            rightSide.add(new SpecialNonTerminal(transition.getCurrentState(), startStackSymbol, stateSymbol));
            ruleStepLogic.createStartingSteps(STARTING_S, STARTING_Z, board.getStartNode().getName(), stateSymbol.getName());
            CFGRule cfgRule = new CFGRule(startSymbol, null, rightSide, transition);
            cfgRule.setSteps(ruleStepLogic.getStepRules());
            cfgRules.add(cfgRule);
        }
        return cfgRules;
    }

    // steps
    public void steps() {
        this.showSteps = !showSteps;
        if (showSteps) {
            showSteps();
        } else {
            hideSteps();
        }
    }

    private void hideSteps() {
        stepsBox.getChildren().clear();
        updateTransition(currentIndex);
    }

    private void showSteps() {
        showStepsButton.setText("Ukáž pravidlá");
        ruleBox.getChildren().clear();
        stepsBox.getChildren().clear();
        VBox stepsLayout = new VBox(10);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(stepsLayout);
        transitionWindow.setScrollPaneStyle(scrollPane);

        List<CFGRule> cfgRules = this.cfgRules.get(currentIndex);
        for (int i = 0; i < cfgRules.get(0).getSteps().size(); i++) {

            VBox stepLayout = new VBox();
            stepLayout.setPadding(new Insets(10));
            stepLayout.setAlignment(Pos.CENTER);
            String helpingComment = "";
            TextFlow transitionTextFlow = new TextFlow();
            for (CFGRule cfgRule : cfgRules) {
                StepRule stepRule = cfgRule.getSteps().get(i);
                helpingComment = stepRule.getHelpingComment();

                TextFlow textFlow = new TextFlow();
                List<Text> texts = stepRule.createTextFromStep();
                textFlow.getChildren().addAll(texts);
                textFlow.setTextAlignment(TextAlignment.CENTER);
                textFlow.setPadding(new Insets(10));

                stepLayout.getChildren().addAll(textFlow);

                if (stepRule.getTransition().getTransitionType() != TransitionType.START) {
                    Transition transition = stepRule.getTransition();
                    List<Text> transitionTexts = transition.createTextFromStep();
                    transitionTextFlow = new TextFlow();
                    transitionTextFlow.setTextAlignment(TextAlignment.CENTER);
                    transitionTextFlow.getChildren().addAll(transitionTexts);
                    transitionTextFlow.setPadding(new Insets(10));
                    transitionTextFlow.setStyle("-fx-background-color: #deeff5;");
                }
            }
            Label helpingLabelComment = transitionWindow.createHelpingComment(helpingComment);
            stepLayout.getChildren().add(0, helpingLabelComment);
            stepLayout.getChildren().add(0, transitionTextFlow);
            stepsLayout.getChildren().add(stepLayout);
        }
        stepsBox.getChildren().add(scrollPane);
    }

    private void showTransitionStage() {
        updateTransition(0);
        transitionStage.show();
        root.requestFocus();
    }

    private void resetStates() {
        transitionLabel.getChildren().clear();
        stepsBox.getChildren().clear();
        this.showSteps = false;
    }

    public void updateTransition(int step) {
        showStepsButton.setText("Ukáž kroky");
        currentIndex += step;
        if (currentIndex < 0) currentIndex = 0;
        if (currentIndex >= transitions.size()) currentIndex = transitions.size() - 1;

        Transition currentTransition = transitions.get(currentIndex);
        resetStates();
        Text labelText = new Text();
        labelText.setFont(new Font(22));
        labelText.setStyle("-fx-font-weight: bold;");
        switch (currentTransition.getTransitionType()) {
            case START -> {
                labelText.setText("Začiatočná prechodová funkcia");
                transitionLabel.getChildren().add(labelText);
                helpingLabelComment.setText("Pre každú bez-kontextovú gramatiku sa pridá začiatočný symbol S," +
                        " ktorý simuluje pridávanie začiatočného stavu zásobníka symbolu Z.");
            }
            case NORMAL -> {
                List<Text> transitionTexts = currentTransition.createTextFromStep();
                String transitionText = transitionTexts.stream().map(Text::getText).reduce("", String::concat);
                labelText.setText(transitionText);
                transitionLabel.getChildren().add(labelText);
                helpingLabelComment.setText("Prechodová funkcia vyžiera zo zásobníka a pridáva nový symbol na zásobník." +
                        " Počet nových pravidiel závisí od počtu stavov a od počtu nových zásobníkových symbolov.");
            }
            case TERMINAL -> {
                List<Text> transitionTexts = currentTransition.createTextFromStep();
                String transitionText = transitionTexts.stream().map(Text::getText).reduce("", String::concat);
                labelText.setText(transitionText);
                transitionLabel.getChildren().add(labelText);
                helpingLabelComment.setText("Prechodová funkcia prečíta symbol. Zo zásobníka jeden vyžerie.");
            }
            default -> {
                List<Text> transitionTexts = currentTransition.createTextFromStep();
                String transitionText = transitionTexts.stream().map(Text::getText).reduce("", String::concat);
                labelText.setText(transitionText);
                transitionLabel.getChildren().add(labelText);
            }
        }
        transitionIndexLabel.setText((currentIndex + 1) + "/" + transitions.size());
        List<CFGRule> cfgRules = this.cfgRules.get(currentIndex);
        ruleBox.getChildren().clear();
        for (CFGRule cfgRule : cfgRules) {
            TextFlow textFlow = new TextFlow();
            Text text = new Text(cfgRule.toString());
            text.setFont(new Font(22));
            textFlow.getChildren().add(text);
            textFlow.setTextAlignment(TextAlignment.CENTER);
            ruleBox.getChildren().add(textFlow);
        }
    }


}
