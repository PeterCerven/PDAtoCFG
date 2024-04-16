package com.example.bakalar.logic.utility;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;


public class LimitedTextField extends TextField {


    public LimitedTextField(String text, int maxLength) {
        super(text);
        this.setTextFormatter(new TextFormatter<>(setMaxLength(maxLength)));
    }

    public LimitedTextField(int maxLength) {
        this.setTextFormatter(new TextFormatter<>(setMaxLength(maxLength)));
    }

    private UnaryOperator<TextFormatter.Change> setMaxLength(int maxLength) {
        return change -> {
            if (change.getControlNewText().length() <= maxLength) {
                if (change.getControlNewText().length() > 1 && change.getControlNewText().contains("ε")) {
                    return null;
                }
                return change;
            } else {
                return null;
            }
        };
    }
}
