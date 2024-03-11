package com.example.bakalar.logic.conversion;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;

public class ConversionUI {


    public Label createHelpingComment(String text) {
        Label label = new Label(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.TEXT_ONLY);
        label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #000000; " +
                "-fx-background-color: #deeff5; -fx-border-color: #9cc2cf; " +
                "-fx-border-width: 0 0 2 0; -fx-padding: 5;");
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;

    }

    public void setScrollPaneStyle(ScrollPane scrollPane) {
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
