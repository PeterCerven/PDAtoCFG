package com.example.bakalar.canvas.conversion;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.transitions.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class ConversionLogic {
    private final Board board;
    private Stage transitionStage;
    private Label currentStateLabel;
    private List<Transition> transitions;
    private int currentIndex = 0;

    public ConversionLogic(Board board) {
        this.board = board;
    }

    public void convertPDA(Stage primaryStage) {
        List<Transition> transitions = board.getNodesTransitions();
        setupTransitionStage(transitions);
        showTransitionStage();
    }
    private void setupTransitionStage(List<Transition> transitions) {
        this.transitions = transitions;
        this.transitionStage = new Stage();
        this.currentStateLabel = new Label();

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> updateTransition(1));

        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> updateTransition(-1));

        HBox buttonLayout = new HBox(10, prevButton, nextButton);
        buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);

        VBox layout = new VBox(10, currentStateLabel, buttonLayout);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 600);
        transitionStage.setScene(scene);
        transitionStage.setTitle("Transition Details");
    }

    private void showTransitionStage() {
        updateTransition(0); // To initialize with the first transition
        transitionStage.show();
    }

    private void updateTransition(int step) {
        currentIndex += step;
        if (currentIndex < 0) currentIndex = 0;
        if (currentIndex >= transitions.size()) currentIndex = transitions.size() - 1;

        Transition currentTransition = transitions.get(currentIndex);
        currentStateLabel.setText(currentTransition.toString());
        currentStateLabel.setFont(new Font("Arial", 16));
    }




}
