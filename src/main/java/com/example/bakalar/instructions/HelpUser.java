package com.example.bakalar.instructions;

import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class HelpUser {
    private static final List<String> imagePaths = List.of(
            "src/main/resources/gifs/Nodes.gif"
            );

    public void tutorial() throws FileNotFoundException {
        Stage instructionStage = new Stage();
        instructionStage.setTitle("Ako používať aplikáciu");
        ScrollPane scrollPane = new ScrollPane();
        VBox imageBox = new VBox(10);
        scrollPane.setContent(imageBox);
        for (String path : imagePaths) {
            Image image = new Image(new FileInputStream(path));
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(400);
            imageBox.getChildren().add(imageView);
        }

        Scene scene = new Scene(scrollPane, 450, 600);
        instructionStage.setScene(scene);
        instructionStage.show();
    }

    public void showAbout(Stage stage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle("Informácie o aplikácii");
        dialog.setHeaderText("Podrobnosti o aplikácii");

        // Vlastné nastavenie obsahu dialógu
        VBox content = new VBox(new Label("Toto je ukážková aplikácia JavaFX."), new Label("Verzia: 1.0"), new Label("Autor: Váš Meno"));
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);

        // Pridanie štandardného zatváracieho tlačidla
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Nastavenie modality
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

    }
}
