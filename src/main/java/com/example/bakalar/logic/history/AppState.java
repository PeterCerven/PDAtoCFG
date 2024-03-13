package com.example.bakalar.logic.history;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AppState implements Serializable {
    private List<NodeModel> nodes;
    private List<ArrowModel> arrows;
    private int nodeCounter;
    private Long idCounter;

    public AppState(List<NodeModel> nodes, List<ArrowModel> arrows, int nodeCounter, Long idCounter) {
        this.nodes = nodes;
        this.arrows = arrows;
        this.nodeCounter = nodeCounter;
        this.idCounter = idCounter;
    }

    public AppState() {
    }
}
