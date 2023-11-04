package com.example.bakalar.canvas;

import com.example.bakalar.cfg.ContextFreeGrammar;
import com.example.bakalar.cfg.Rule;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private Canvas canvas;

    @FXML
    private GridPane pdaTable;

    @FXML
    private Button showSteps;

    @FXML
    private VBox stack;

    @FXML
    private TextField startSymbolInput;

    @FXML
    private Button nodeBtn;

    @FXML
    private Button arrowBtn;

    @FXML
    private Button resetBtn;

    private boolean nodeBtnOn;

    private boolean arrowBtnOn;

    private Set<Rule> rules;
    private int takenRowsCFG = 1;
    private int takenRowsGNF = 0;
    private int takenRowsProd = 0;
    private Character startSymbol;

    private Board board;

    private static final Logger logger = LogManager.getLogger(MainController.class.getName());

    public MainController() {
        this.board = new Board();
        logger.info("test");
    }

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
            TextField right = (TextField) cfgTable.getChildren().get(i + 2);
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

    public void drawCircleOn() {
        if (nodeBtnOn) {
            this.nodeBtnOn = false;
            nodeBtn.setText("Nakresli kruh");
        } else {
            this.nodeBtnOn = true;
            this.arrowBtnOn = false;
            nodeBtn.setText("Vypni kreslenie");
        }
    }

    public void drawArrowOn() {
        if (arrowBtnOn) {
            this.arrowBtnOn = false;
            arrowBtn.setText("Nakresli arrow");
        } else {
            this.arrowBtnOn = true;
            this.nodeBtnOn = false;
            arrowBtn.setText("Vypni kreslenie");
        }
    }

    public void showSteps() {

    }

    public void createCircle(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        double radius = 50;
        if (nodeBtnOn) {
            board.addNode(new Node(x, y, radius));
            drawBoard();
        }
    }

    public void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        List<Node> nodes = board.getNodes();
        for (Node node : nodes) {
            drawNode(node, gc);
        }
    }

    public void drawNode(Node node, GraphicsContext gc) {
        List<Arrow> arrows = node.getArrows();
        double x = node.getxCord();
        double y = node.getyCord();
        double radius = node.getRadius();
        gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
        arrows.forEach(this::drawArrow);
    }

    public void drawArrow(Arrow arrow) {
        // TODO
    }

    public void step() {

    }

    public void reset() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        board.reset();
        drawBoard();
    }

    public void createGraph() {
    }


}
