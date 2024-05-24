package com.example.bakalar.logic;

import com.example.bakalar.instructions.HelpUser;
import com.example.bakalar.logic.history.HistoryLogic;
import com.example.bakalar.logic.utility.ButtonBehaviour;
import com.example.bakalar.logic.utility.ButtonState;
import com.example.bakalar.logic.utility.DescribePDA;
import com.example.bakalar.logic.utility.ErrorPopUp;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.util.List;


public class MainController {

    public static final int NODE_RADIUS = 34;
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
    private Board board;
    @Setter
    private Stage stage;
    private ButtonBehaviour btnBeh;
    private HelpUser helpUser;

    @FXML
    private void initialize() {
        setupBoard();
    }

    private void setupBoard() {
        btnBeh = new ButtonBehaviour(nodeBtn, arrowBtn, resetBtn, eraseBtn, undoBtn, reUndoBtn, conversionBtn);
        List<TextField> describePDAFields = List.of(describeStates, describeAlphabet, describeStackAlphabet, describeEndStates);
        DescribePDA describePDA = new DescribePDA(describePDAFields, transFunctions);
        HistoryLogic historyLogic = new HistoryLogic(undoBtn, reUndoBtn);
        board = new Board(mainPane, describePDA, historyLogic, stage, btnBeh);
        helpUser = new HelpUser();
    }

    public void setMainScene(Scene mainScene) {
        mainScene.setOnKeyPressed(this::keyAction);
        mainScene.setOnMouseClicked(this::mouseAction);
        mainScene.setOnKeyPressed(this::arrowCreation);
    }

    // Objects creation

    public void createNode(MouseEvent event) {
        if (btnBeh.getCurrentState().equals(ButtonState.NODE) && event.getButton() == MouseButton.PRIMARY) {
            board.saveCurrentStateToHistory();
            board.createMyNode(event.getX(), event.getY());
        }
    }


    // Buttons actions

    public void drawNodeOn() {
        btnBeh.toggleButtonState(ButtonState.NODE);
    }

    public void drawArrowOn() {
        btnBeh.toggleButtonState(ButtonState.ARROW);
    }

    public void eraseFunctionOn() {
        btnBeh.toggleButtonState(ButtonState.ERASE);
    }

    public void resetAll() {
        board.saveCurrentStateToHistory();
        board.clearBoard();
    }

    public void buttonRedo() {
        board.redo();
    }

    public void buttonUndo() {
        board.undo();
    }

    public void convertPDA() {
        board.convertPDA();
    }

    // test

    public void testBoard() {
        board.testBoard();
    }

    // menu action
    public void closeApp() {
        stage.close();
    }

    public void saveToFile() {
        board.saveCurrentStateToFile();
    }

    public void loadFromFile() {
        board.loadStateFromFile();
    }

    public void about(){
        helpUser.showAbout(stage);
    }

    public void controls() {
        try {
            helpUser.tutorial();
        } catch (FileNotFoundException e) {
            ErrorPopUp.showErrorDialog("Nepodarilo sa načítať návod");
        }
    }

    // key actions
    public void keyAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            btnBeh.resetToSelect();
        }
    }

    public void mouseAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            btnBeh.resetToSelect();
        }
    }

    public void arrowCreation(KeyEvent event) {
        // shift
        if (event.getCode() == KeyCode.SHIFT){
            btnBeh.toggleButtonState(ButtonState.ARROW);
        }
    }



}
