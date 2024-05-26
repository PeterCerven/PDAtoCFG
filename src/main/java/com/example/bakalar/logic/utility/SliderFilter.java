package com.example.bakalar.logic.utility;

import javafx.scene.control.TextFormatter.Change;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SliderFilter implements UnaryOperator<Change> {
    private static final Pattern RANGE_PATTERN = Pattern.compile("^\\d*$");

    @Override
    public Change apply(Change change) {
        if (RANGE_PATTERN.matcher(change.getControlNewText()).matches()) {
            return change;
        }
        return null;
    }
}