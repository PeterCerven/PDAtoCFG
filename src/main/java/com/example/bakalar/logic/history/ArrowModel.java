package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.TransitionInputs;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ArrowModel implements Serializable {
    private Long fromNodeId;
    private Long toNodeId;
    private TransitionInputs transition;
    private Double controlPointChangeX;
    private Double controlPointChangeY;
    private boolean isLineArrow;

    public ArrowModel() {
    }

    public ArrowModel(Long fromNodeId, Long toNodeId, TransitionInputs transition, Double controlPointChangeX, Double controlPointChangeY) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.transition = transition;
        this.controlPointChangeX = controlPointChangeX;
        this.controlPointChangeY = controlPointChangeY;
        this.isLineArrow = true;
    }

    public ArrowModel(Long fromNodeId, Long toNodeId, TransitionInputs transition) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.transition = transition;
        this.isLineArrow = false;
    }
}
