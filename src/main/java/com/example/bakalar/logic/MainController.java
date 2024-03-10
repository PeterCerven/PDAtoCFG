package com.example.bakalar.logic;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.conversion.ConversionLogic;
import com.example.bakalar.logic.history.HistoryLogic;
import com.example.bakalar.logic.transitions.runPDALogic;
import com.example.bakalar.logic.utility.ButtonState;
import com.example.bakalar.logic.utility.DescribeCFG;
import com.example.bakalar.logic.utility.DescribePDA;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.example.bakalar.logic.Board.EPSILON;
import static com.example.bakalar.logic.Board.STARTING_Z;


public class MainController {

    public static final int NODE_RADIUS = 34;
    private static final Logger log = LogManager.getLogger(MainController.class.getName());
    private static final String ARROW_ICON_PATH = "file:src/main/resources/icons/Arrow.png";
    private static final String ERASER_ICON_PATH = "file:src/main/resources/icons/Eraser.png";
    private static final String NODE_ICON_PATH = "file:src/main/resources/icons/Node.png";
    private static final String START_ICON_PATH = "file:src/main/resources/icons/Start.png";
    private static final String STEP_ICON_PATH = "file:src/main/resources/icons/Step.png";
    private static final String ERASE_ALL_ICON_PATH = "file:src/main/resources/icons/EraseAll.png";
    private static final String UNDO_ICON_PATH = "file:src/main/resources/icons/Undo.png";
    private static final String REDO_ICON_PATH = "file:src/main/resources/icons/Redo.png";
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
    private Button conversionBtn;
    @FXML
    private Button testBtn;
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
    private BorderPane mainContainer;
    private ButtonState currentState = ButtonState.SELECT;
    private Board currentBoard;
    private runPDALogic runPDALogic;
    private ConversionLogic conversionLogic;


    @FXML
    private void initialize() {
        setupBoard();
        setUpButtons();
    }

    private void setupBoard() {



        List<TextField> describePDAFields = List.of(describeStates, describeAlphabet, describeStackAlphabet, describeEndStates);
        DescribePDA describePDA = new DescribePDA(describePDAFields, transFunctions);



        HistoryLogic historyLogic = new HistoryLogic();

        currentBoard = new Board(mainPane, describePDA, historyLogic, currentState);
        historyLogic.setBoard(currentBoard);
//        this.runPDALogic = new runPDALogic(currentBoard, this.stateContainer);
        this.conversionLogic = new ConversionLogic(currentBoard);
//        inputFieldAlphabet.textProperty().addListener((observable, oldValue, newValue) -> {
//            currentBoard.updateAllDescribePDA();
//        });
    }


    // set images


    private void setUpButtons() {
        Image image = new Image(ARROW_ICON_PATH);
        ImageView imageView = new ImageView(image);
        this.arrowBtn.setGraphic(imageView);
        arrowBtn.setOnMouseEntered(e -> arrowBtn.setCursor(Cursor.HAND));
        arrowBtn.setOnMouseExited(e -> arrowBtn.setCursor(Cursor.DEFAULT));

        image = new Image(ERASER_ICON_PATH);
        imageView = new ImageView(image);
        this.eraseBtn.setGraphic(imageView);
        eraseBtn.setOnMouseEntered(e -> eraseBtn.setCursor(Cursor.HAND));
        eraseBtn.setOnMouseExited(e -> eraseBtn.setCursor(Cursor.DEFAULT));

        image = new Image(NODE_ICON_PATH);
        imageView = new ImageView(image);
        this.nodeBtn.setGraphic(imageView);
        nodeBtn.setOnMouseEntered(e -> nodeBtn.setCursor(Cursor.HAND));
        nodeBtn.setOnMouseExited(e -> nodeBtn.setCursor(Cursor.DEFAULT));

        image = new Image(ERASE_ALL_ICON_PATH);
        imageView = new ImageView(image);
        this.resetBtn.setGraphic(imageView);
        resetBtn.setOnMouseEntered(e -> resetBtn.setCursor(Cursor.HAND));
        resetBtn.setOnMouseExited(e -> resetBtn.setCursor(Cursor.DEFAULT));


        image = new Image(UNDO_ICON_PATH);
        imageView = new ImageView(image);
        this.undoBtn.setGraphic(imageView);
        undoBtn.setOnMouseEntered(e -> undoBtn.setCursor(Cursor.HAND));
        undoBtn.setOnMouseExited(e -> undoBtn.setCursor(Cursor.DEFAULT));

        image = new Image(REDO_ICON_PATH);
        imageView = new ImageView(image);
        this.reUndoBtn.setGraphic(imageView);
        reUndoBtn.setOnMouseEntered(e -> reUndoBtn.setCursor(Cursor.HAND));
        reUndoBtn.setOnMouseExited(e -> reUndoBtn.setCursor(Cursor.DEFAULT));

        conversionBtn.setOnMouseEntered(e -> conversionBtn.setCursor(Cursor.HAND));
        conversionBtn.setOnMouseExited(e -> conversionBtn.setCursor(Cursor.DEFAULT));
    }


    // Objects creation

    public void createNode(MouseEvent event) {
        if (currentState.equals(ButtonState.NODE) && event.getButton() == MouseButton.PRIMARY) {
            currentBoard.saveCurrentState();
            currentBoard.createMyNode(event.getX(), event.getY());
        }
    }

    // Buttons actions

    public void resetAll() {
        currentBoard.saveCurrentState();
        currentBoard.clearBoard();
        currentState = ButtonState.SELECT;
        currentBoard.setSelectedNode(null);
        updateButtonStates();
    }

    public void step() {
        runPDALogic.step();
    }

    public void buttonRedo() {
        currentBoard.redo();
    }

    public void buttonUndo() {
        currentBoard.undo();
    }

    public void convertPDA() {
        conversionLogic.convertPDA();
    }

    public void testBoard() {
        currentBoard.saveCurrentState();
        currentBoard.testBoard();
        MyNode firstNode = currentBoard.createMyNode(120, 150);
        currentBoard.setStarting(firstNode, true);
        MyNode secondNode = currentBoard.createMyNode(320, 150);
        currentBoard.setEnding(firstNode, true);
        currentBoard.createMyArrow(firstNode, firstNode, "1", "Z", "XZ");
        currentBoard.createMyArrow(firstNode, firstNode, "1", "X", "XX");
        currentBoard.createMyArrow(firstNode, firstNode, EPSILON, "X", EPSILON);
        currentBoard.createMyArrow(firstNode, secondNode, "0", "X", "X");
        currentBoard.createMyArrow(secondNode, secondNode, "1", "X", EPSILON);
        currentBoard.createMyArrow(secondNode, firstNode, "0", "Z", "Z");
    }

    // Buttons toggle

    private void updateButtonStates() {
        nodeBtn.setStyle(currentState == ButtonState.NODE ? "-fx-background-color: #113a11; -fx-border-color: #0b270c" : "");
        arrowBtn.setStyle(currentState == ButtonState.ARROW ? "-fx-background-color: #113a11; -fx-border-color: #0b270c" : "");
        eraseBtn.setStyle(currentState == ButtonState.ERASE ? "-fx-background-color: #113a11; -fx-border-color: #0b270c" : "");
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
        currentBoard.setCurrentState(currentState);
    }

}
