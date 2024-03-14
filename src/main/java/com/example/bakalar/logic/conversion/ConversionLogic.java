package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.Board;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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
    private static final String NEXT_ARROW_ICON_PATH = "file:src/main/resources/icons/nextArrow.png";
    private static final String PREVIOUS_ARROW_ICON_PATH = "file:src/main/resources/icons/previousArrow.png";
    private final Board board;
    private Stage transitionStage;
    private TextFlow transitionLabel;
    private Label transitionIndexLabel;
    private List<Transition> transitions;
    private VBox ruleBox;
    private VBox stepsBox;
    private boolean showSteps = false;
    private Map<Integer, List<CFGRule>> cfgRules;
    private int currentIndex;
    private Label helpingLabelComment;
    private BorderPane root;
    private final ConversionUI conversionUI;
    private Button showStepsButton;


    public ConversionLogic(Board board) {
        this.board = board;
        this.conversionUI = new ConversionUI();
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
        this.transitionStage = new Stage();
        this.transitionLabel = new TextFlow();
        transitionLabel.setTextAlignment(TextAlignment.CENTER);
        this.transitionIndexLabel = new Label();
        this.ruleBox = new VBox();
        ScrollPane ruleBoxScrollPane = new ScrollPane();
        ruleBoxScrollPane.setFitToWidth(true);
        ruleBoxScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ruleBoxScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ruleBoxScrollPane.setPadding(new Insets(10));
        ruleBoxScrollPane.setStyle("-fx-background-color: #f4f4f4; ");
        ruleBoxScrollPane.setContent(ruleBox);
        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.TOP_CENTER);
        this.stepsBox = new VBox();
        this.helpingLabelComment = new Label();
        this.cfgRules = new HashMap<>();
        Pane helpingLayout = new Pane();
        HBox arrowsLayoutBox = new HBox();
        HBox transitionLabelBox = new HBox(helpingLayout, transitionLabel, arrowsLayoutBox);
        helpingLayout.prefWidthProperty().bind(arrowsLayoutBox.widthProperty());
        arrowsLayoutBox.prefWidthProperty().bind(Bindings.divide(transitionLabelBox.widthProperty(), 4));
        transitionLabelBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(transitionLabel, Priority.ALWAYS);



        showStepsButton = new Button("Ukáž kroky");
        showStepsButton.setFocusTraversable(false);
        showStepsButton.setAlignment(Pos.BOTTOM_CENTER);
        showStepsButton.setPadding(new Insets(10));
        showStepsButton.setPrefSize(140, 26);
        showStepsButton.setFont(new Font(18));
        showStepsButton.setAlignment(Pos.CENTER);
        showStepsButton.setOnAction(e -> steps());


        Button nextButton = new Button();
        nextButton.setFocusTraversable(false);
        Image nextArrowImage = new Image(NEXT_ARROW_ICON_PATH);
        ImageView nextArrowView = new ImageView(nextArrowImage);
        nextArrowView.setFitHeight(20);
        nextArrowView.setFitWidth(20);
        nextButton.setGraphic(nextArrowView);
        nextButton.setOnAction(e -> updateTransition(1));

        Button prevButton = new Button();
        prevButton.setFocusTraversable(false);
        Image previousArrowImage = new Image(PREVIOUS_ARROW_ICON_PATH);
        ImageView previousArrowView = new ImageView(previousArrowImage);
        previousArrowView.setFitHeight(20);
        previousArrowView.setFitWidth(20);
        prevButton.setGraphic(previousArrowView);
        prevButton.setOnAction(e -> updateTransition(-1));

        HBox buttonLayout = new HBox(10, prevButton, nextButton);
        buttonLayout.setAlignment(Pos.BASELINE_CENTER);

        VBox helpingComments = new VBox();
        helpingComments.setAlignment(Pos.CENTER);
        helpingComments.setPadding(new Insets(10));
        helpingComments.setStyle("-fx-background-color: lightblue;");
        this.helpingLabelComment = new Label();
        helpingLabelComment.setTextAlignment(TextAlignment.CENTER);
        helpingLabelComment.setContentDisplay(ContentDisplay.TEXT_ONLY);
        helpingLabelComment.setWrapText(true);
        helpingLabelComment.setFont(new Font(18));
        helpingLabelComment.setStyle("-fx-font-weight: bold;");
        helpingComments.getChildren().add(helpingLabelComment);


        transitionIndexLabel.setFont(new Font("Arial", 22));
        arrowsLayoutBox.getChildren().add(transitionIndexLabel);
        arrowsLayoutBox.getChildren().add(buttonLayout);
        arrowsLayoutBox.setAlignment(Pos.CENTER_RIGHT);
        arrowsLayoutBox.setPadding(new Insets(10));
        arrowsLayoutBox.setSpacing(10);


        for (Transition transition : transitions) {
            this.cfgRules.put(transitions.indexOf(transition), convertTransitions(transition));
        }

        VBox topMenu = new VBox(10, transitionLabelBox, helpingComments);
        topMenu.setAlignment(Pos.TOP_CENTER);

        BorderPane ruleBoxPane = new BorderPane();
        ruleBoxPane.setPadding(new Insets(10));
        contentBox.getChildren().add(ruleBoxScrollPane);
        contentBox.getChildren().add(showStepsButton);
        contentBox.getChildren().add(stepsBox);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setSpacing(10);
        ruleBoxPane.setCenter(contentBox);


        root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(ruleBoxPane);
        root.setTop(topMenu);

        Scene scene = new Scene(root, 800, 800);
        String styles = Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm();
        String conversionStyle = Objects.requireNonNull(getClass().getResource("/css/conversion.css")).toExternalForm();
        scene.getStylesheets().add(styles);
        scene.getStylesheets().add(conversionStyle);
        transitionStage.setScene(scene);
        transitionStage.setTitle("Detaily prechodových funkcií");


        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                updateTransition(1);
            } else if (e.getCode() == KeyCode.LEFT) {
                updateTransition(-1);
            }
        });
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
    private void steps() {
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
        conversionUI.setScrollPaneStyle(scrollPane);

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
            Label helpingLabelComment = conversionUI.createHelpingComment(helpingComment);
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

    private void updateTransition(int step) {
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
