package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.TransitionInputs;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArrowHistory {
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private int fromId;
    private int toId;
    private List<TransitionInputs> transitions;
    private double controlPointChangeX;
    private double controlPointChangeY;
    private boolean isLineArrow;

    public ArrowHistory(double startX, double startY, double endX, double endY, int fromId, int toId,
                        List<TransitionInputs> transitions, double controlPointChangeX, double controlPointChangeY, boolean isLineArrow) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.fromId = fromId;
        this.toId = toId;
        this.transitions = transitions;
        this.controlPointChangeX = controlPointChangeX;
        this.controlPointChangeY = controlPointChangeY;
        this.isLineArrow = isLineArrow;
    }

    public ArrowHistory(int fromId, int toId, List<TransitionInputs> transitions, boolean isLineArrow) {
        this.fromId = fromId;
        this.toId = toId;
        this.transitions = transitions;
        this.isLineArrow = isLineArrow;
    }
}
