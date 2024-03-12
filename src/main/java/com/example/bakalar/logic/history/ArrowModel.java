package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.TransitionInputs;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ArrowModel implements Serializable {
    private UUID fromNodeId;
    private UUID toNodeId;
    private TransitionInputs transition;
    private Double controlPointChangeX;
    private Double controlPointChangeY;
    private boolean isLineArrow;

    public ArrowModel() {
    }

    public ArrowModel(UUID fromNodeId, UUID toNodeId, TransitionInputs transition, Double controlPointChangeX, Double controlPointChangeY) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.transition = transition;
        this.controlPointChangeX = controlPointChangeX;
        this.controlPointChangeY = controlPointChangeY;
        this.isLineArrow = true;
    }

    public ArrowModel(UUID fromNodeId, UUID toNodeId, TransitionInputs transition) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.transition = transition;
        this.isLineArrow = false;
    }
}
