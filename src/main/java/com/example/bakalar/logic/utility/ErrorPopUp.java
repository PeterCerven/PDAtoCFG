package com.example.bakalar.logic.utility;

import javafx.scene.control.Alert;

public class ErrorPopUp {

    public static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
