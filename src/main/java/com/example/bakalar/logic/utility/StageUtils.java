package com.example.bakalar.logic.utility;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class StageUtils {
    private static final String ICON_IMAGE_PATH = "/icons/Node.png";

    public static void setStageIcon(Stage stage) {
        Image icon = new Image(Objects.requireNonNull(StageUtils.class.getResourceAsStream(ICON_IMAGE_PATH)));
        stage.getIcons().add(icon);
    }
}
