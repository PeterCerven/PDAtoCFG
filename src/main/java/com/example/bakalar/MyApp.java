package com.example.bakalar;

import com.example.bakalar.logic.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.bakalar.logic.utility.StageUtils.setStageIcon;

public class MyApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApp.class.getResource("my-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setMainScene(scene);

        String styles = Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm();
        scene.getStylesheets().add(styles);

        setStageIcon(stage);
        stage.setTitle("Konverzia PDA na CFG");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}