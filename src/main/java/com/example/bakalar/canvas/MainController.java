package com.example.bakalar.canvas;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.button.ButtonState;
import com.example.bakalar.canvas.conversion.ConversionLogic;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.transitions.BoardLogic;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.example.bakalar.canvas.Board.STARTING_Z;


public class MainController {

    public static final int NODE_RADIUS = 30;
    private static final Logger log = LogManager.getLogger(MainController.class.getName());
    @FXML
    private AnchorPane mainPane;
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
    private Button startButton;
    @FXML
    private Button stepButton;
    @FXML
    private Button conversionBtn;
    @FXML
    private TextField inputFieldAlphabet;
    @FXML
    private TextField begSymbol;
    @FXML
    private HBox stateContainer;
    @FXML
    private TextField describeStates;
    @FXML
    private TextField describeAlphabet;
    @FXML
    private TextField describeStackAlphabet;
    @FXML
    private TextField describeEndStates;
    @FXML
    private VBox transFunctions;
    @FXML
    private VBox rulesContainer;


    private ButtonState currentState = ButtonState.SELECT;
    private MyNode selectedNode;
    private double startX, startY;
    private Board board;
    private BoardLogic boardLogic;
    private ConversionLogic conversionLogic;


    @FXML
    private void initialize() {
        this.begSymbol.setText(STARTING_Z);
        List<TextField> describeFields = List.of(describeStates, describeAlphabet, describeStackAlphabet, describeEndStates);
        board = new Board(mainPane, inputFieldAlphabet, describeFields, transFunctions, rulesContainer);
        this.boardLogic = new BoardLogic(board, this.stateContainer);
        this.conversionLogic = new ConversionLogic(board);
        inputFieldAlphabet.textProperty().addListener((observable, oldValue, newValue) -> {
            board.updateAllDescribePDA();
        });
        setButtonImages();

    }


    // set images


    private void setButtonImages() {
        Image image = new Image("file:src/main/resources/icons/Arrow.png");
        ImageView imageView = new ImageView(image);
        this.arrowBtn.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/Eraser.png");
        imageView = new ImageView(image);
        this.eraseBtn.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/Node.png");
        imageView = new ImageView(image);
        this.nodeBtn.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/Start.png");
        imageView = new ImageView(image);
        this.startButton.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/Step.png");
        imageView = new ImageView(image);
        this.stepButton.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/EraseAll.png");
        imageView = new ImageView(image);
        this.resetBtn.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/Undo.png");
        imageView = new ImageView(image);
        this.undoBtn.setGraphic(imageView);

        image = new Image("file:src/main/resources/icons/Redo.png");
        imageView = new ImageView(image);
        this.reUndoBtn.setGraphic(imageView);

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
            if (arrow != null) {
                makeErasable(arrow);
                if (arrow instanceof LineArrow lineArrow) {
                    makeCurveDraggable(lineArrow);
                }

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
        String inputAlphabet = this.inputFieldAlphabet.getText();
        boardLogic.start(STARTING_Z, inputAlphabet);
    }

    public void step() {
        boardLogic.step();
    }

    public void reUndo() {
        log.info("ReUndo");
        // TODO reUndo
    }

    public void undo() {
        log.info("Undo");
        // TODO undo
    }

    public void convertPDA() {
        log.info("Convert PDA");
        conversionLogic.convertPDA();
    }

    // Buttons toggle

    private void updateButtonStates() {
        nodeBtn.setStyle(currentState == ButtonState.NODE ? "-fx-background-color: #00ff00" : "");
        arrowBtn.setStyle(currentState == ButtonState.ARROW ? "-fx-background-color: #00ff00" : "");
        eraseBtn.setStyle(currentState == ButtonState.ERASE ? "-fx-background-color: #00ff00" : "");
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

    private void toggleButtonState(ButtonState newState) {
        if (currentState == newState) {
            currentState = ButtonState.SELECT;
        } else {
            currentState = newState;
        }
        updateButtonStates();
    }


}
