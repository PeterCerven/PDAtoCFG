package com.example.bakalar.logic;

import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.exceptions.MyCustomException;
import com.example.bakalar.logic.conversion.ConversionLogic;
import com.example.bakalar.logic.history.HistoryLogic;
import com.example.bakalar.logic.utility.ButtonState;
import com.example.bakalar.logic.utility.DescribePDA;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

import static com.example.bakalar.logic.Board.EPSILON;
import static com.example.bakalar.logic.utility.ErrorPopUp.showErrorDialog;


public class MainController {

    public static final int NODE_RADIUS = 34;
    private static final String ARROW_ICON_PATH = "/icons/Arrow.png";
    private static final String ERASER_ICON_PATH = "/icons/Eraser.png";
    private static final String NODE_ICON_PATH = "/icons/Node.png";
    private static final String ERASE_ALL_ICON_PATH = "/icons/EraseAll.png";
    private static final String UNDO_ICON_PATH = "/icons/Undo.png";
    private static final String REDO_ICON_PATH = "/icons/Redo.png";
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
    private TextField describeStates;
    @FXML
    private TextField describeAlphabet;
    @FXML
    private TextField describeStackAlphabet;
    @FXML
    private TextField describeEndStates;
    @FXML
    private VBox transFunctions;
    private ButtonState currentState = ButtonState.SELECT;
    private Board currentBoard;
    private ConversionLogic conversionLogic;
    @Setter
    private Stage stage;


    @FXML
    private void initialize() {
        setupBoard();
        setUpButtons();
    }

    private void setupBoard() {
        List<TextField> describePDAFields = List.of(describeStates, describeAlphabet, describeStackAlphabet, describeEndStates);
        DescribePDA describePDA = new DescribePDA(describePDAFields, transFunctions);
        HistoryLogic historyLogic = new HistoryLogic(undoBtn, reUndoBtn);
        currentBoard = new Board(mainPane, describePDA, historyLogic, currentState, stage);
        historyLogic.setBoard(currentBoard);
        this.conversionLogic = new ConversionLogic(currentBoard);


    }

    public void setMainScene(Scene mainScene) {
        mainScene.setOnKeyPressed(this::keyAction);
        mainScene.setOnMouseClicked(this::mouseAction);
    }

    // set images


    private void setUpButtons() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ARROW_ICON_PATH)));
        ImageView imageView = new ImageView(image);
        this.arrowBtn.setGraphic(imageView);
        arrowBtn.setOnMouseEntered(e -> arrowBtn.setCursor(Cursor.HAND));
        arrowBtn.setOnMouseExited(e -> arrowBtn.setCursor(Cursor.DEFAULT));

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ERASER_ICON_PATH)));
        imageView = new ImageView(image);
        this.eraseBtn.setGraphic(imageView);
        eraseBtn.setOnMouseEntered(e -> eraseBtn.setCursor(Cursor.HAND));
        eraseBtn.setOnMouseExited(e -> eraseBtn.setCursor(Cursor.DEFAULT));

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(NODE_ICON_PATH)));
        imageView = new ImageView(image);
        this.nodeBtn.setGraphic(imageView);
        nodeBtn.setOnMouseEntered(e -> nodeBtn.setCursor(Cursor.HAND));
        nodeBtn.setOnMouseExited(e -> nodeBtn.setCursor(Cursor.DEFAULT));

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ERASE_ALL_ICON_PATH)));
        imageView = new ImageView(image);
        this.resetBtn.setGraphic(imageView);
        resetBtn.setOnMouseEntered(e -> resetBtn.setCursor(Cursor.HAND));
        resetBtn.setOnMouseExited(e -> resetBtn.setCursor(Cursor.DEFAULT));


        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(UNDO_ICON_PATH)));
        imageView = new ImageView(image);
        this.undoBtn.setGraphic(imageView);
        undoBtn.setOnMouseEntered(e -> undoBtn.setCursor(Cursor.HAND));
        undoBtn.setOnMouseExited(e -> undoBtn.setCursor(Cursor.DEFAULT));

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(REDO_ICON_PATH)));
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
            currentBoard.saveCurrentStateToHistory();
            currentBoard.createMyNode(event.getX(), event.getY());
        }
    }

    // Buttons actions

    public void resetAll() {
        currentBoard.saveCurrentStateToHistory();
        currentBoard.clearBoard();
        currentState = ButtonState.SELECT;
        currentBoard.setSelectedNode(null);
        updateButtonStates();
    }

    public void buttonRedo() {
        currentBoard.redo();
    }

    public void buttonUndo() {
        currentBoard.undo();
    }

    public void convertPDA() {
        try {
            conversionLogic.convertPDA();
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }

    public void testBoard() {
        currentBoard.saveCurrentStateToHistory();
        currentBoard.clearBoard();
        MyNode firstNode = currentBoard.createMyNode(120, 150);
        currentBoard.setStarting(firstNode, true);
        MyNode secondNode = currentBoard.createMyNode(320, 150);
        currentBoard.setEnding(firstNode, true);

        currentBoard.createMyArrow(firstNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs("1", "Z", "XZ"));
        currentBoard.createMyArrow(firstNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs("1", "X", "XX"));
        currentBoard.createMyArrow(firstNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs(EPSILON, "X", EPSILON));
        currentBoard.createMyArrow(firstNode.getNodeId(), secondNode.getNodeId(), new TransitionInputs("0", "X", "X"));
        currentBoard.createMyArrow(secondNode.getNodeId(), secondNode.getNodeId(), new TransitionInputs("1", "X", EPSILON));
        currentBoard.createMyArrow(secondNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs("0", "Z", "Z"));
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

    // menu action
    public void closeApp() {
        stage.close();
    }

    public void saveToFile() {
        currentBoard.saveCurrentStateToFile();
    }

    public void loadFromFile() {
        currentBoard.loadStateFromFile();
    }

    // key actions
    public void keyAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            currentState = ButtonState.SELECT;
            updateButtonStates();
            currentBoard.setCurrentState(currentState);
        }
    }

    public void mouseAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            currentState = ButtonState.SELECT;
            updateButtonStates();
            currentBoard.setCurrentState(currentState);
        }
    }

}
