package com.example.bakalar.logic.utility;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.util.Objects;


public class ButtonBehaviour {
    private static final String ARROW_ICON_PATH = "/icons/Arrow.png";
    private static final String ERASER_ICON_PATH = "/icons/Eraser.png";
    private static final String NODE_ICON_PATH = "/icons/Node.png";
    private static final String ERASE_ALL_ICON_PATH = "/icons/EraseAll.png";
    private static final String UNDO_ICON_PATH = "/icons/Undo.png";
    private static final String REDO_ICON_PATH = "/icons/Redo.png";

    private final Button nodeBtn;
    private final Button arrowBtn;
    private final Button resetBtn;
    private final Button eraseBtn;
    private final Button undoBtn;
    private final Button reUndoBtn;
    private final Button conversionBtn;
    @Getter
    private ButtonState currentState;

    public ButtonBehaviour(Button nodeBtn, Button arrowBtn, Button resetBtn, Button eraseBtn, Button undoBtn,
                           Button reUndoBtn, Button conversionBtn) {
        this.nodeBtn = nodeBtn;
        this.arrowBtn = arrowBtn;
        this.resetBtn = resetBtn;
        this.eraseBtn = eraseBtn;
        this.undoBtn = undoBtn;
        this.reUndoBtn = reUndoBtn;
        this.conversionBtn = conversionBtn;
        this.currentState = ButtonState.SELECT;
        setUpButtons();
    }

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

    private void updateButtonStates() {
        nodeBtn.setStyle(currentState == ButtonState.NODE ? "-fx-background-color: #113a11; -fx-border-color: #0b270c" : "");
        arrowBtn.setStyle(currentState == ButtonState.ARROW ? "-fx-background-color: #113a11; -fx-border-color: #0b270c" : "");
        eraseBtn.setStyle(currentState == ButtonState.ERASE ? "-fx-background-color: #113a11; -fx-border-color: #0b270c" : "");
    }

    public void resetToSelect() {
        currentState = ButtonState.SELECT;
        updateButtonStates();
    }

    public void toggleButtonState(ButtonState newState) {
        if (currentState == newState) {
            currentState = ButtonState.SELECT;
        } else {
            currentState = newState;
        }
        updateButtonStates();
    }


}
