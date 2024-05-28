package com.example.bakalar.instructions;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.bakalar.logic.utility.ErrorPopUp.showErrorDialog;
import static com.example.bakalar.logic.utility.StageUtils.setStageIcon;

public class HelpUser {
    private static final String NEXT_ARROW_PATH = "src/main/resources/icons/right-arrow.png";
    private static final String PREVIOUS_ARROW_PATH = "src/main/resources/icons/left-arrow.png";
    private static final List<String> imagePaths = List.of(
            "src/main/resources/gifs/NodesEdit.gif",
            "src/main/resources/gifs/ArrowsEdit.gif",
            "src/main/resources/gifs/EraserEdit.gif"
    );

    private static final List<String> comments = List.of(
            "Vytváranie a presúvanie stavov. Zmena názvu stavu a jeho typu na koncový alebo začiatočný.",
            "Vytváranie prechodov medzi stavmi. Zmena symbolov prechodu. Zmena trajektórie prechodu.",
            "Mazanie prvkov z plochy. Mazanie stavov, prechodov aj prechodových symbolov."
    );

    private static final List<String> labels = List.of(
            "Ovládanie stavov automatu",
            "Ovládanie prechodov medzi stavmi",
            "Mazanie prvkov z plochy"
    );

    private int currentIndex = 0;
    private ImageView imageView;
    private Label commentLabel;
    private Label counterLabel;
    private Label imageLabel;
    private final List<Image> preloadedImages = new ArrayList<>();

    public void preLoadImages(MenuItem showTutorialButton) {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (String path : imagePaths) {
                    preloadedImages.add(new Image(new FileInputStream(path)));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                showTutorialButton.setDisable(false);
            }

            @Override
            protected void failed() {
                showErrorDialog("Nepodarilo sa načítať návod");
            }
        };

        Thread preloadThread = new Thread(preloadTask);
        preloadThread.setDaemon(true);
        preloadThread.start();
    }


    public void tutorial() throws FileNotFoundException {
        Stage instructionStage = new Stage();
        instructionStage.setTitle("Ako používať aplikáciu");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-alignment: center; -fx-padding: 10;");
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1152);
        commentLabel = new Label();
        commentLabel.setWrapText(true);
        counterLabel = new Label();
        imageLabel = new Label();
        imageLabel.setStyle("-fx-font-weight: bold;");
        imageLabel.setFont(new Font(32));
        updateContent();

        HBox navigationBox = getNavigationBox();



        VBox contentBox = new VBox(10, imageLabel, imageView, commentLabel);
        contentBox.setStyle("-fx-alignment: center; -fx-padding: 10;");

        root.setCenter(contentBox);
        root.setBottom(navigationBox);

        Scene scene = new Scene(root, 1200, 800);
        String conversionStyle = Objects.requireNonNull(getClass().getResource("/css/conversion.css")).toExternalForm();
        String mainStyle = Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm();
        scene.getStylesheets().addAll(conversionStyle, mainStyle);
        instructionStage.setScene(scene);
        instructionStage.setResizable(false);
        setStageIcon(instructionStage);
        instructionStage.show();
    }

    private HBox getNavigationBox() throws FileNotFoundException {
        Button previousButton = new Button();
        ImageView previousArrowView  = new ImageView(new Image(new FileInputStream(PREVIOUS_ARROW_PATH)));
        previousArrowView.setFitWidth(30);
        previousArrowView.setFitHeight(30);
        previousButton.setGraphic(previousArrowView);
        previousButton.setOnAction(e -> showPrevious());


        Button nextButton = new Button();
        ImageView nextArrowView = new ImageView(new Image(new FileInputStream(NEXT_ARROW_PATH)));
        nextArrowView.setFitWidth(30);
        nextArrowView.setFitHeight(30);
        nextButton.setGraphic(nextArrowView);
        nextButton.setOnAction(e -> showNext());

        HBox navigationBox = new HBox(10, previousButton, nextButton, counterLabel);
        navigationBox.setStyle("-fx-alignment: center;");
        return navigationBox;
    }

    private void updateContent() throws FileNotFoundException {
        if (currentIndex >= 0 && currentIndex < imagePaths.size()) {
            Image image = preloadedImages.get(currentIndex);
            imageView.setImage(image);
            commentLabel.setText(comments.get(currentIndex));
            counterLabel.setText((currentIndex + 1) + " / " + imagePaths.size());
            imageLabel.setText(labels.get(currentIndex));
        }
    }

    private void showPrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            try {
                updateContent();
            } catch (FileNotFoundException e) {
                showErrorDialog("Nepodarilo sa načítať návod");
            }
        }
    }

    private void showNext() {
        if (currentIndex < imagePaths.size() - 1) {
            currentIndex++;
            try {
                updateContent();
            } catch (FileNotFoundException e) {
                showErrorDialog("Nepodarilo sa načítať návod");
            }
        }
    }

    public void showAbout(Stage stage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle("Informácie o aplikácii");
        dialog.setHeaderText(null);
        Text text = new Text("Toto je didaktická abeceda vytvorená ako praktická časť Bakalárskej práce. Téma práce:" +
                " Konštrukcia bezkontextovej gramatiky ekvivalentnej so zásobníkovým automatom akceptujúcim prázdnym zásobníkom\".");
        text.setWrappingWidth(300);
        text.setStyle("-fx-font-size: 14px;");
        VBox content = new VBox(text, new Label("Verzia: 1.0 2024"), new Label("Autor: Peter Červeň"));


        content.setSpacing(5);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

    }
}
