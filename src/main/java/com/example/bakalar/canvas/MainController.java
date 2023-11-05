package com.example.bakalar.canvas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class MainController {

    private static final Logger log = LogManager.getLogger(MainController.class.getName());

    @FXML
    private ScrollPane myScrollPane;

    @FXML
    private AnchorPane mySecondAnchor;

    @FXML
    private Button nodeBtn;
    @FXML
    private Button arrowBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button eraseBtn;
    @FXML
    private Button undoBtn;
    @FXML
    private Button reUndoBtn;
    @FXML
    private Button selectBtn;

    private boolean nodeBtnOn;
    private boolean arrowBtnOn;
    private boolean selectBtnOn;
    private boolean eraseBtnOn;
//    private Set<Rule> rules;
//    private int takenRowsCFG = 1;
//    private int takenRowsGNF = 0;
//    private int takenRowsProd = 0;
//    private Character startSymbol;
    private Board board;

    public MainController() {
        this.board = new Board();
        log.info("test");
    }

//    public void addNewRow() {
//        TextField left = new TextField();
//        TextField right = new TextField();
//        Label arrow = new Label("   ->");
//        cfgTable.add(left, 0, takenRowsCFG);
//        cfgTable.add(arrow, 1, takenRowsCFG);
//        cfgTable.add(right, 2, takenRowsCFG);
//        takenRowsCFG++;
//    }

//    private void collectData() {
//        rules = new LinkedHashSet<>();
//        startSymbol = startSymbolInput.getText().trim().charAt(0);
//        List<Pair<Character, String>> pairs = new ArrayList<>();
//        for (int i = 0; i < cfgTable.getChildren().size(); i += 3) {
//
//            TextField left = (TextField) cfgTable.getChildren().get(i);
//            TextField right = (TextField) cfgTable.getChildren().get(i + 2);
//            if (left.getText().isEmpty() || right.getText().isEmpty()) {
//                continue;
//            }
//            pairs.add(new Pair<>(left.getText().trim().charAt(0), right.getText().trim()));
//
//        }
//        for (Pair<Character, String> pair : pairs) {
//            for (String s : pair.getValue().split("\\|")) {
//                rules.add(new Rule(pair.getKey(), s));
//            }
//
//        }
//    }

//    public void convertGrammar() {
//        gnfTable.getChildren().clear();
//        takenRowsGNF = 0;
//        takenRowsProd = 0;
//        collectData();
//        ContextFreeGrammar cfg = new ContextFreeGrammar(rules, startSymbol);
//        List<Rule> sortedJoinedRules = new ArrayList<>(cfg.getJoinedRules());
//        for (Rule rule : sortedJoinedRules) {
//            TextField left = new TextField(rule.getLeftSide().toString());
//            TextField right = new TextField(rule.getCombinedRightSide());
//            Label arrow = new Label("   ->");
//            gnfTable.add(left, 0, takenRowsGNF);
//            gnfTable.add(arrow, 1, takenRowsGNF);
//            gnfTable.add(right, 2, takenRowsGNF);
//            takenRowsGNF++;
//        }
//
//        for (Rule rule : rules) {
//            String nonTerminal = rule.getNonTerminalsRight().isEmpty() ? "ε" : rule.getNonTerminalsRight();
//            TextField prodRight = new TextField("δ{" + rule.getTerminalsRight() + ", " + rule.getLeftSide() + ", " + nonTerminal + "}");
//            pdaTable.add(prodRight, 0, takenRowsProd);
//            takenRowsProd++;
//        }
//
//    }


    public void createNode(MouseEvent event) {
        if (nodeBtnOn) {
            log.info("creating node");
            double x = event.getX();
            log.info("X coordinate: " + x);
            double y = event.getY();
            log.info("Y coordinate: " + y);
            double radius = 50;
            MyNode myNode = new MyNode(x, y, radius);
            myNode.setFill(Color.BLACK);
            board.addNode(myNode);
            mySecondAnchor.getChildren().add(myNode);
        }
    }


    public void drawNode(MyNode myNode, GraphicsContext gc) {
        List<Arrow> arrows = myNode.getArrows();
        double x = myNode.getX();
        double y = myNode.getY();
        double radius = myNode.getRadius();
        gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
        for (Arrow arrow: arrows) {
            drawArrow(arrow, gc);
        }
    }

    public void moveNode(MouseEvent event) {
        if (selectBtnOn) {
            double x = event.getX();
            double y = event.getY();
            MyNode myNode = board.getSelectedNode(x, y);
            if (myNode == null) {
                return;
            }
        }


    }

    public void drawArrow(Arrow arrow, GraphicsContext gc) {
        Pair<Double, Double>  start = arrow.getStart();
        Pair<Double, Double>  finish = arrow.getFinish();
        gc.moveTo(start.getKey(), start.getValue());
        gc.lineTo(finish.getKey(), finish.getValue());
    }


    public void resetAll() {
        board.reset();
        turnOffAll();
    }


    public void drawNodeOn() {
        if (nodeBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            nodeBtnOn = true;
            nodeBtn.setText("Vypni");
        }
    }

    public void drawArrowOn() {
        turnOffAll();
        if (arrowBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            arrowBtnOn = true;
            arrowBtn.setText("Vypni");
        }
    }

    public void erase() {
        turnOffAll();
        if (eraseBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            eraseBtnOn = true;
            eraseBtn.setText("Vypni");
        }
    }

    public void selectOn() {
        turnOffAll();
        if (selectBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            selectBtnOn = true;
            selectBtn.setText("Vypni");
        }
    }

    public void undo() {
        turnOffAll();
        // TODO undo
    }

    private void turnOffAll() {
        this.eraseBtnOn = false;
        this.nodeBtnOn = false;
        this.arrowBtnOn = false;
        this.selectBtnOn = false;
        this.selectBtn.setText("Zvol vyber");
        this.arrowBtn.setText("Zvol sip");
        this.nodeBtn.setText("Zvol kruh");
        this.eraseBtn.setText("Zvol zmizik");
    }


    public void reUndo(ActionEvent actionEvent) {
        // TODO reUndo
    }
}
