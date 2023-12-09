package com.example.bakalar.canvas;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.button.ButtonState;
import com.example.bakalar.canvas.node.MyNode;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainController {

    public static final int NODE_RADIUS = 30;
    private static final Logger log = LogManager.getLogger(MainController.class.getName());
    @FXML
    private ScrollPane myScrollPane;
    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox stack;
    @FXML
    private HBox inputAlphabet;

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
    @FXML
    private Button startButton;
    @FXML
    private Button stepButton;
    @FXML
    private TextField inputFieldAlphabet;
    @FXML
    private TextField begSymbol;
    private ButtonState currentState = ButtonState.NONE;
    private MyNode selectedNode;
    private double startX, startY;
    private Board board;
    private BoardLogic boardLogic;


    @FXML
    private void initialize() {
        board = new Board(mainPane, stack, inputAlphabet);
        this.boardLogic = new BoardLogic(board);
        initStack();
    }

    // initialization

    private void initStack() {
        for (int i = 0; i < 10; i++) {
            TextField textField = new TextField();
            textField.setAlignment(Pos.CENTER);
            textField.setStyle("-fx-border-color: black;");
            textField.setEditable(false);
            stack.getChildren().add(textField);
        }
    }

    // Objects creation

    public void createNode(MouseEvent event) {
        if (currentState.equals(ButtonState.NODE) && event.getButton() == MouseButton.PRIMARY) {
            MyNode myNode = new MyNode(event.getX(), event.getY(), NODE_RADIUS);
            makeDraggable(myNode);
            enableArrowCreation(myNode);
            board.addObject(myNode);
        }
    }

    private void createArrow(MyNode node) {
        if (selectedNode != null) {
            Arrow arrow = board.createArrow(selectedNode, node);
            makeErasable(arrow);
            if (arrow instanceof LineArrow lineArrow) {
                makeCurveDraggable(lineArrow);
            }
            board.selectNode(selectedNode, false);
            selectedNode = null;
        } else {
            selectedNode = node;
            board.selectNode(node, true);
        }
    }

    // Event handlers

    private void makeCurveDraggable(LineArrow arrow) {
        arrow.getControlIndicator().setOnMousePressed(event -> {
            if (currentState.equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - arrow.getControlIndicator().getTranslateX();
                    startY = event.getSceneY() - arrow.getControlIndicator().getTranslateY();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    arrow.resetControlPoint();
                }
            }
        });

        arrow.getControlIndicator().setOnMouseDragged(e -> {
            if (currentState.equals(ButtonState.SELECT)) {
                arrow.moveControlPoint(e.getSceneX() - startX, e.getSceneY() - startY);
            }
        });
    }


    private void makeDraggable(MyNode node) {
        node.setOnMousePressed(event -> {
            if (currentState.equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    board.showDialog(node);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - node.getTranslateX();
                    startY = event.getSceneY() - node.getTranslateY();
                }
            }
        });

        node.setOnMouseDragged(e -> {
            if (currentState.equals(ButtonState.SELECT)) {
                node.move(e.getSceneX() - startX, e.getSceneY() - startY);
                node.updateArrows(false);
            }
        });

        node.setOnMouseReleased(event -> {
            if (currentState.equals(ButtonState.SELECT)) {
                node.updateArrows(true);
            }
        });
    }


    private void enableArrowCreation(MyNode node) {
        node.setOnMouseClicked(event -> {
            if (currentState.equals(ButtonState.ARROW) && event.getButton() == MouseButton.PRIMARY) {
                createArrow(node);
            } else if (currentState.equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                board.removeObject(node);
            }
        });
    }

    private void makeErasable(Node node) {
        node.setOnMouseClicked(event -> {
            if (currentState.equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                board.removeObject(node);
            }
        });
    }

    // Buttons actions

    public void resetAll() {
        board.clearBoard();
        currentState = ButtonState.NONE;
        selectedNode = null;
        updateButtonStates();
    }

    public void start() {
        log.info("Start");
        String begSymbol = this.begSymbol.getText();
        String inputAlphabet = this.inputFieldAlphabet.getText();
        boardLogic.start(begSymbol, inputAlphabet);
        // TODO start
    }

    public void step() {
        log.info("Step");
        boardLogic.step();
        // TODO step
    }

    public void reUndo() {
        log.info("ReUndo");
        // TODO reUndo
    }

    public void undo() {
        log.info("Undo");
        // TODO undo
    }

    // Buttons toggle

    private void updateButtonStates() {
        nodeBtn.setText(currentState == ButtonState.NODE ? "Vypni" : "Zvol kruh");
        arrowBtn.setText(currentState == ButtonState.ARROW ? "Vypni" : "Zvol sip");
        eraseBtn.setText(currentState == ButtonState.ERASE ? "Vypni" : "Zvol zmizik");
        selectBtn.setText(currentState == ButtonState.SELECT ? "Vypni" : "Zvol vyber");
    }

    public void drawNodeOn() {
        toggleButtonState(ButtonState.NODE);
    }

    public void drawArrowOn() {
        toggleButtonState(ButtonState.ARROW);
    }

    public void eraseFunctionOn() {
        toggleButtonState(ButtonState.ERASE);
    }

    public void selectOn() {
        toggleButtonState(ButtonState.SELECT);
    }

    private void toggleButtonState(ButtonState newState) {
        if (currentState == newState) {
            currentState = ButtonState.NONE;
        } else {
            currentState = newState;
        }
        updateButtonStates();
    }


}
