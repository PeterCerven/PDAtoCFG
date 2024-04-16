package com.example.bakalar.logic.utility;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ErrorPopUp {

    public static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        StageUtils.setStageIcon(alertStage);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();

    }
}
