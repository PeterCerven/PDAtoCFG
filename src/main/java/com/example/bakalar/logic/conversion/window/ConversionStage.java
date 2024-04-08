package com.example.bakalar.logic.conversion.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ConversionStage {
    private Stage stage;
    private Scene scene;
    private BorderPane rootPane;
    private Button previousButton;
    private Button nextButton;
    private static final String NEXT_ARROW_ICON_PATH = "file:src/main/resources/icons/nextArrow.png";
    private static final String PREVIOUS_ARROW_ICON_PATH = "file:src/main/resources/icons/previousArrow.png";

    public ConversionStage() {
        this.stage = new Stage();
        this.rootPane = new BorderPane();
        this.scene = new Scene(rootPane, 800, 800);
        this.previousButton = new Button();
        this.nextButton = new Button();
        initStage();
    }

    private void initStage() {
        Pane helpingLayout = new Pane();
        TextFlow transitionLabel = new TextFlow();

        Button nextButton = buttonsSetUp(NEXT_ARROW_ICON_PATH);

        Button prevButton = buttonsSetUp(PREVIOUS_ARROW_ICON_PATH);

        HBox buttonLayout = new HBox(10, prevButton, nextButton);
        buttonLayout.setAlignment(Pos.BASELINE_CENTER);

        Label transitionIndexLabel = new Label();
        transitionIndexLabel.setFont(new Font("Arial", 22));

        HBox arrowsLayoutBox = new HBox();
        arrowsLayoutBox.getChildren().add(transitionIndexLabel);
        arrowsLayoutBox.getChildren().add(buttonLayout);
        arrowsLayoutBox.setAlignment(Pos.CENTER_RIGHT);
        arrowsLayoutBox.setPadding(new Insets(10));
        arrowsLayoutBox.setSpacing(10);

        HBox transitionLabelBox = new HBox(helpingLayout, transitionLabel, arrowsLayoutBox);

        Label helpingLabelComment = new Label();
        helpingLabelComment.setTextAlignment(TextAlignment.CENTER);
        helpingLabelComment.setContentDisplay(ContentDisplay.TEXT_ONLY);
        helpingLabelComment.setWrapText(true);
        helpingLabelComment.setFont(new Font(18));
        helpingLabelComment.setStyle("-fx-font-weight: bold;");

        VBox helpingComments = new VBox();
        helpingComments.setAlignment(Pos.CENTER);
        helpingComments.setPadding(new Insets(10));
        helpingComments.setStyle("-fx-background-color: lightblue;");
        helpingComments.getChildren().add(helpingLabelComment);


        VBox topMenu = new VBox(10, transitionLabelBox, helpingComments);
        topMenu.setAlignment(Pos.TOP_CENTER);

        rootPane.setPadding(new Insets(10));
        rootPane.setTop(topMenu);

        String styles = Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm();
        String conversionStyle = Objects.requireNonNull(getClass().getResource("/css/conversion.css")).toExternalForm();
        scene.getStylesheets().add(styles);
        scene.getStylesheets().add(conversionStyle);
        stage.setScene(scene);
        stage.setTitle("Detaily prechodových funkcií");
    }

    private Button buttonsSetUp(String nextArrowIconPath) {
        Button nextButton = new Button();
        nextButton.setFocusTraversable(false);
        Image nextArrowImage = new Image(nextArrowIconPath);
        ImageView nextArrowView = new ImageView(nextArrowImage);
        nextArrowView.setFitHeight(20);
        nextArrowView.setFitWidth(20);
        nextButton.setGraphic(nextArrowView);
        return nextButton;
    }
}
