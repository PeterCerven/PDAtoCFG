package com.example.bakalar.logic;

import com.example.bakalar.instructions.HelpUser;
import com.example.bakalar.logic.history.HistoryLogic;
import com.example.bakalar.logic.utility.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;


public class MainController {
    private static final KeyCombination CTRL_Z = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    private static final KeyCombination CTRL_SHIFT_Z = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    public Button testBtn;
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
    @FXML
    private Slider slider;
    @FXML
    private TextField sliderInput;
    private Board board;
    private Stage stage;
    private ButtonBehaviour btnBeh;
    private HelpUser helpUser;
    @FXML
    private MenuItem gifControls;

    @FXML
    private void initialize() {
        setupBoard();
    }

    private void setupBoard() {
        btnBeh = new ButtonBehaviour(nodeBtn, arrowBtn, resetBtn, eraseBtn, undoBtn, reUndoBtn, conversionBtn);
        List<TextField> describePDAFields = List.of(describeStates, describeAlphabet, describeStackAlphabet, describeEndStates);
        DescribePDA describePDA = new DescribePDA(describePDAFields, transFunctions);
        HistoryLogic historyLogic = new HistoryLogic(undoBtn, reUndoBtn);
        setSlider();
        board = new Board(mainPane, describePDA, historyLogic, stage, btnBeh, slider, sliderInput);
        helpUser = new HelpUser();

        mainPane.setOnMouseClicked(this::createNode);
    }


    private void setSlider() {
        TextFormatter<Integer> formatter = new TextFormatter<>(new SliderFilter());
        sliderInput.setTextFormatter(formatter);
        sliderInput.setOnKeyTyped(e -> {
            if (sliderInput.getText().isEmpty()) {
                return;
            }
            if (Integer.parseInt(sliderInput.getText()) > 60) {
                slider.setValue(60);
                sliderInput.setText("60");
            }
            if (sliderInput.getText().length() > 1 && Integer.parseInt(sliderInput.getText()) < 25) {
                slider.setValue(25);
                sliderInput.setText("25");
            }
            int value = Integer.parseInt(sliderInput.getText());
            slider.setValue(value);
        });

        slider.valueChangingProperty().addListener((obs, wasChanging, isNowChanging) -> {
            if (!isNowChanging) {
                Integer value = (int) Math.round(slider.getValue());
                sliderInput.setText(value + "");
            }
        });
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            event.consume();
            board.showExitConfirmation(stage);
        });
    }

    public void setMainScene(Scene mainScene) {
        mainScene.setOnKeyPressed(this::keyPress);
        mainScene.setOnMouseClicked(this::mouseAction);
        mainPane.setOnMouseClicked(this::createNode);
    }


    // Objects creation

    public void createNode(MouseEvent event) {
        board.createNode(event);
    }


    // Buttons actions

    public void changeSize() {
        board.updateBoardSize((int) Math.round(slider.getValue()));
    }

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
        board.clearBoard(true);
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


    // menu action
    public void closeApp() {
        board.showExitConfirmation(stage);
    }

    public void saveToFile() {
        board.saveCurrentStateToFile();
    }

    public void loadFromFile() {
        board.loadStateFromFile();
    }

    public void about() {
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

    public void mouseAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            btnBeh.resetToSelect();
        }
    }

    public void keyPress(KeyEvent event) {
        if (CTRL_Z.match(event)) {
            board.undo();
        } else if (CTRL_SHIFT_Z.match(event)) {
            board.redo();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            if (btnBeh.getCurrentState() == ButtonState.SELECT) {
                board.showExitConfirmation(stage);
            } else {
                btnBeh.resetToSelect();
            }
        }
    }

    // test

    public void testBoard() {
        board.testBoard();
    }


}
