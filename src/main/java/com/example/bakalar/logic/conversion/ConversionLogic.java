package com.example.bakalar.logic.conversion;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.exceptions.MyCustomException;
import com.example.bakalar.files.FileLogic;
import com.example.bakalar.logic.conversion.window.ConversionStage;
import com.example.bakalar.logic.conversion.window.InformationWindow;
import com.example.bakalar.logic.conversion.window.StepsWindow;
import com.example.bakalar.logic.conversion.window.WindowType;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.bakalar.logic.Board.*;
import static com.example.bakalar.logic.utility.ErrorPopUp.showErrorDialog;

public class ConversionLogic {
    private List<MyNode> nodes;
    private final StepsWindow stepsWindow;
    private final InformationWindow informationWindow;
    private final ConversionStage conversionStage;
    private boolean showSteps = false;
    private List<RulesWindows> rulesWindows;
    private int currentIndex;


    public ConversionLogic() {
        this.conversionStage = new ConversionStage();
        this.stepsWindow = new StepsWindow();
        this.informationWindow = new InformationWindow();
    }

    public void convertPDA(List<Transition> transitions, List<MyNode> nodes, MyNode startNode) throws MyCustomException {
        this.nodes = nodes;
        rulesWindows = new ArrayList<>();
        currentIndex = 0;
        if (transitions.isEmpty()) {
            showErrorDialog("Nemožno previesť Nemáte žiadne prechodové funkcie.");
            return;
        }
        if (startNode == null) {
            showErrorDialog("Nemožno previesť Nemáte žiadny začiatočný stav.");
            return;
        }
        rulesWindows.add(new RulesWindows(WindowType.INFORMATION));
        transitions.add(0, new Transition(startNode.getName(), WindowType.START));
        setupTransitionStage(transitions);
        showTransitionStage();
    }

    private Set<SpecialNonTerminal> getAllNonTerminals() {
        Set<SpecialNonTerminal> nonTerminals = new HashSet<>();
        for (RulesWindows ruleWindow : rulesWindows) {
            for (CFGRule rule : ruleWindow.getRules()) {
                if (rule.getLeftSide() != null)
                    nonTerminals.add(rule.getLeftSide());
                nonTerminals.addAll(rule.getRightSide());
            }
        }
        return nonTerminals;
    }

    private List<CFGRule> getAllRules() {
        List<CFGRule> allRules = new ArrayList<>();
        for (RulesWindows ruleWindow : rulesWindows) {
            allRules.addAll(ruleWindow.getRules());
        }
        return allRules;
    }

    private Set<MySymbol> getAllTerminals() {
        Set<MySymbol> terminals = new HashSet<>();
        for (CFGRule rule : getAllRules()) {
            if (rule.getTerminal() != null && !rule.getTerminal().getName().equals(EPSILON)) {
                terminals.add(rule.getTerminal());
            }
        }
        return terminals;
    }


    private void setupTransitionStage(List<Transition> transitions) throws MyCustomException {
        for (Transition transition : transitions) {
            this.rulesWindows.add(convertTransitions(transition));
        }

        Scene scene = conversionStage.getScene();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                updateWindow(1);
            } else if (e.getCode() == KeyCode.LEFT) {
                updateWindow(-1);
            }
        });

        Button downloadButton = informationWindow.getDownloadBtn();
        Button prevButton = conversionStage.getPreviousButton();
        Button nextButton = conversionStage.getNextButton();
        Button showStepsButton = stepsWindow.getShowStepsButton();

        FileLogic fileLogic = new FileLogic();
        downloadButton.setOnAction(e -> fileLogic.saveToTextFile(getAllRules(), conversionStage.getStage()));
        prevButton.setOnAction(e -> updateWindow(-1));
        nextButton.setOnAction(e -> updateWindow(1));
        showStepsButton.setOnAction(e -> steps());
        updateWindow(0);
        conversionStage.getStage().show();
    }


    private RulesWindows convertTransitions(Transition transition) throws MyCustomException {
        switch (transition.getWindowType()) {
            case START -> {
                return new RulesWindows(startMove(transition), transition, WindowType.START);
            }
            case NORMAL -> {
                return new RulesWindows(pushMove(transition), transition, WindowType.NORMAL);
            }
            case TERMINAL -> {
                return new RulesWindows(terminalMove(transition), transition, WindowType.TERMINAL);
            }
            default -> {
                throw new MyCustomException("Nepodporovaný typ prechodovej funkcie.");
            }
        }
    }

    private void recursiveLoop(int totalLoops, int iterations, String[] statesPos, int currentLoop,
                               Transition transition, List<CFGRule> cfgRules) {
        if (currentLoop == totalLoops) {
            CFGRule cfgRule = createRule(statesPos, transition);
            cfgRules.add(cfgRule);
            return;
        }

        for (int i = 0; i < iterations; i++) {
            statesPos[currentLoop] = nodes.get(i).getName();
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
        SpecialNonTerminal leftSide = new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(),
                transition.getNextState());
        MySymbol terminal = new MySymbol(transition.getInputSymbolToRead().getName());
        List<SpecialNonTerminal> rightSide = new ArrayList<>();
        ruleStepLogic.createTerminalSteps();
        CFGRule cfgRule = new CFGRule(leftSide, terminal, rightSide, transition);
        cfgRule.setSteps(ruleStepLogic.getStepRules());
        return List.of(cfgRule);
    }

    private List<CFGRule> pushMove(Transition transition) {
        List<CFGRule> convertedTransitions = new ArrayList<>();
        recursiveLoop(transition.getSymbolsToPush().size(), nodes.size(), new String[transition.getSymbolsToPush().size()],
                0, transition, convertedTransitions);

        return convertedTransitions;
    }


    private List<CFGRule> startMove(Transition transition) {
        List<MySymbol> allStateSymbols = nodes.stream().map(node -> new MySymbol(node.getName())).toList();
        List<CFGRule> cfgRules = new ArrayList<>();

        MySymbol startSymbol = new MySymbol(STARTING_S);
        MySymbol startStackSymbol = new MySymbol(STARTING_Z);
        for (MySymbol stateSymbol : allStateSymbols) {
            RuleStepLogic ruleStepLogic = new RuleStepLogic(transition);
            List<SpecialNonTerminal> rightSide = new ArrayList<>();
            rightSide.add(new SpecialNonTerminal(transition.getCurrentState(), startStackSymbol, stateSymbol));
            ruleStepLogic.createStartingSteps(STARTING_S, STARTING_Z, transition.getCurrentState().getName(), stateSymbol.getName());
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
        stepsWindow.hideSteps();
        updateWindow(0);
    }

    private void showSteps() {
        stepsWindow.showSteps();
        List<CFGRule> cfgRules = this.rulesWindows.get(currentIndex).getRules();
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

                if (stepRule.getTransition().getWindowType() != WindowType.START) {
                    Transition transition = stepRule.getTransition();
                    List<Text> transitionTexts = transition.createTextFromStep();
                    transitionTextFlow = new TextFlow();
                    transitionTextFlow.setTextAlignment(TextAlignment.CENTER);
                    transitionTextFlow.getChildren().addAll(transitionTexts);
                    transitionTextFlow.setPadding(new Insets(10));
                    transitionTextFlow.setStyle("-fx-background-color: #deeff5;");
                }
            }
            Label helpingLabelComment = stepsWindow.createHelpingComment(helpingComment);
            stepLayout.getChildren().add(0, helpingLabelComment);
            stepLayout.getChildren().add(0, transitionTextFlow);
            stepsWindow.getStepsLayout().getChildren().add(stepLayout);
        }
        stepsWindow.getStepsBox().getChildren().add(stepsWindow.getScrollPane());
    }

    private void showTransitionStage() {
        updateWindow(0);
        conversionStage.getStage().show();
        conversionStage.getRootPane().requestFocus();
    }

    private void resetStates() {
        conversionStage.getTransitionLabel().getChildren().clear();
        stepsWindow.getStepsBox().getChildren().clear();
        this.showSteps = false;
    }

    public void updateWindow(int step) {
        stepsWindow.getStepsBox().getChildren().clear();
        stepsWindow.getShowStepsButton().setText("Ukáž kroky");
        currentIndex += step;
        if (currentIndex < 0) currentIndex = 0;
        if (currentIndex >= rulesWindows.size()) currentIndex = rulesWindows.size() - 1;

        if (!stepsWindow.getContentBox().getChildren().contains(stepsWindow.getRuleBoxScrollPane())) {
            stepsWindow.getContentBox().getChildren().add(0, stepsWindow.getRuleBoxScrollPane());
        }

        RulesWindows currentWindow = rulesWindows.get(currentIndex);
        resetStates();
        Text labelText = new Text();
        labelText.setFont(new Font(22));
        labelText.setStyle("-fx-font-weight: bold;");
        conversionStage.getTransitionIndexLabel().setText((currentIndex + 1) + "/" + rulesWindows.size());
        switch (currentWindow.getWindowType()) {
            case START -> {
                labelText.setText("Začiatočná prechodová funkcia");
                conversionStage.getTransitionLabel().getChildren().add(labelText);
                conversionStage.getHelpingLabelComment().setText("Pre každú bez-kontextovú gramatiku sa pridá začiatočný symbol S," +
                        " ktorý simuluje pridávanie začiatočného stavu zásobníka symbolu Z.");
                conversionStage.getRootPane().setCenter(stepsWindow.getRuleBoxPane());
            }
            case NORMAL -> {
                List<Text> transitionTexts = currentWindow.getTransition().createTextFromStep();
                String transitionText = transitionTexts.stream().map(Text::getText).reduce("", String::concat);
                labelText.setText(transitionText);
                conversionStage.getTransitionLabel().getChildren().add(labelText);
                conversionStage.getHelpingLabelComment().setText("Prechodová funkcia vyžiera zo zásobníka a pridáva nový" +
                        " symbol na zásobník. Počet nových pravidiel závisí od počtu stavov a od počtu nových zásobníkových symbolov.");
                conversionStage.getRootPane().setCenter(stepsWindow.getRuleBoxPane());
            }
            case TERMINAL -> {
                List<Text> transitionTexts = currentWindow.getTransition().createTextFromStep();
                String transitionText = transitionTexts.stream().map(Text::getText).reduce("", String::concat);
                labelText.setText(transitionText);
                conversionStage.getTransitionLabel().getChildren().add(labelText);
                conversionStage.getHelpingLabelComment().setText("Prechodová funkcia prečíta symbol. Zo zásobníka jeden vyžerie.");
                conversionStage.getRootPane().setCenter(stepsWindow.getRuleBoxPane());
            }
            case INFORMATION -> {
                labelText.setText("Bezkontextová gramatika");
                conversionStage.getTransitionLabel().getChildren().add(labelText);
                conversionStage.getHelpingLabelComment().setText("Pravidlá bezkontextovej gramatiky.");
                conversionStage.getRootPane().setCenter(informationWindow.getInformationPane(this.getAllNonTerminals(),
                        this.getAllTerminals(), this.getAllRules(), STARTING_S));
                return;
            }
            default -> {
                return;
            }
        }
        List<CFGRule> cfgRules = this.rulesWindows.get(currentIndex).getRules();
        stepsWindow.getRuleBox().getChildren().clear();
        for (CFGRule cfgRule : cfgRules) {
            TextFlow textFlow = new TextFlow();
            Text text = new Text(cfgRule.toString());
            text.setFont(new Font(22));
            textFlow.getChildren().add(text);
            textFlow.setTextAlignment(TextAlignment.CENTER);
            stepsWindow.getRuleBox().getChildren().add(textFlow);
        }
    }


}
