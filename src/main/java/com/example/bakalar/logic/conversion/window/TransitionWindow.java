package com.example.bakalar.logic.conversion.window;

import com.example.bakalar.logic.conversion.ConversionLogic;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TransitionWindow extends ConversionWindow {
    private Button showStepsButton;


    public TransitionWindow() {
        super();
    }

    public void initializeElements(BorderPane root) {
        VBox ruleBox = new VBox();
        VBox stepsBox = new VBox();

        ScrollPane ruleBoxScrollPane = new ScrollPane();
        ruleBoxScrollPane.setFitToWidth(true);
        ruleBoxScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ruleBoxScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ruleBoxScrollPane.setPadding(new Insets(10));
        ruleBoxScrollPane.setStyle("-fx-background-color: #f4f4f4; ");
        ruleBoxScrollPane.setContent(ruleBox);

        showStepsButton = new Button("Ukáž kroky");
        showStepsButton.setFocusTraversable(false);
        showStepsButton.setAlignment(Pos.BOTTOM_CENTER);
        showStepsButton.setPadding(new Insets(10));
        showStepsButton.setPrefSize(140, 26);
        showStepsButton.setFont(new Font(18));
        showStepsButton.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.getChildren().add(ruleBoxScrollPane);
        contentBox.getChildren().add(showStepsButton);
        contentBox.getChildren().add(stepsBox);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setSpacing(10);

        BorderPane ruleBoxPane = new BorderPane();
        ruleBoxPane.setPadding(new Insets(10));
        ruleBoxPane.setCenter(contentBox);

        root.setCenter(ruleBoxPane);
    }

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
