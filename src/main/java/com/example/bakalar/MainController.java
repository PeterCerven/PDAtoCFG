package com.example.bakalar;

import com.example.bakalar.cfg.ContextFreeGrammar;
import com.example.bakalar.cfg.Rule;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainController {

    @FXML
    private Button addRuleBtn;

    @FXML
    private GridPane cfgTable;

    @FXML
    private Button convertBtn;

    @FXML
    private GridPane gnfTable;

    @FXML
    private Button inputBtn;

    @FXML
    private TextField inputTape;

    @FXML
    private Canvas myCanvas;

    @FXML
    private GridPane pdaTable;

    @FXML
    private Button showSteps;

    @FXML
    private VBox stack;

    @FXML
    private TextField startSymbolInput;

    private Set<Rule> rules;
    private int takenRowsCFG = 1;
    private int takenRowsGNF = 0;
    private int takenRowsProd = 0;
    private Character startSymbol;

    public void addNewRow() {
        TextField left = new TextField();
        TextField right = new TextField();
        Label arrow = new Label("   ->");
        cfgTable.add(left, 0, takenRowsCFG);
        cfgTable.add(arrow, 1, takenRowsCFG);
        cfgTable.add(right, 2, takenRowsCFG);
        takenRowsCFG++;
    }

    private void collectData() {
        rules = new LinkedHashSet<>();
        startSymbol = startSymbolInput.getText().trim().charAt(0);
        List<Pair<Character, String>> pairs = new ArrayList<>();
        for (int i = 0; i < cfgTable.getChildren().size(); i += 3) {

            TextField left = (TextField) cfgTable.getChildren().get(i);
            TextField right = (TextField) cfgTable.getChildren().get(i+2);
            if (left.getText().isEmpty() || right.getText().isEmpty()) {
                continue;
            }
            pairs.add(new Pair<>(left.getText().trim().charAt(0), right.getText().trim()));

        }
        for (Pair<Character, String> pair : pairs) {
            for (String s : pair.getValue().split("\\|")) {
                rules.add(new Rule(pair.getKey(), s));
            }

        }
    }

    public void convertGrammar() {
        gnfTable.getChildren().clear();
        takenRowsGNF = 0;
        takenRowsProd = 0;
        collectData();
        ContextFreeGrammar cfg = new ContextFreeGrammar(rules, startSymbol);
        List<Rule> sortedJoinedRules = new ArrayList<>(cfg.getJoinedRules());
        for (Rule rule : sortedJoinedRules) {
            TextField left = new TextField(rule.getLeftSide().toString());
            TextField right = new TextField(rule.getCombinedRightSide());
            Label arrow = new Label("   ->");
            gnfTable.add(left, 0, takenRowsGNF);
            gnfTable.add(arrow, 1, takenRowsGNF);
            gnfTable.add(right, 2, takenRowsGNF);
            takenRowsGNF++;
        }

        for (Rule rule : rules) {
            String nonTerminal = rule.getNonTerminalsRight().isEmpty() ? "ε" : rule.getNonTerminalsRight();
            TextField prodRight = new TextField("δ{" + rule.getTerminalsRight() + ", " + rule.getLeftSide() + ", " + nonTerminal + "}");
            pdaTable.add(prodRight, 0, takenRowsProd);
            takenRowsProd++;
        }

    }

    public void showSteps() {

    }

    public void step() {

    }

    public void createGraph() {

    }




}
