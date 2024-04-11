package com.example.bakalar.logic.conversion.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepsWindow {
    private final Button showStepsButton;
    private BorderPane ruleBoxPane;
    private VBox ruleBox;
    private VBox stepsBox;
    private VBox stepsLayout;
    private ScrollPane scrollPane;


    public StepsWindow() {
        super();
        ruleBox = new VBox();
        stepsBox = new VBox();

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

        ruleBoxPane = new BorderPane();
        ruleBoxPane.setPadding(new Insets(10));
        ruleBoxPane.setCenter(contentBox);
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

    public void updateStepsWindow() {
        showStepsButton.setText("Ukáž kroky");
        ruleBox.getChildren().clear();
        stepsBox.getChildren().clear();

        stepsLayout = new VBox(10);

        scrollPane = new ScrollPane();
        scrollPane.setContent(stepsLayout);
        setScrollPaneStyle(scrollPane);
    }
}
