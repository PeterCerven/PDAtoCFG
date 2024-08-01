package com.example.bakalar.logic.history;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AppState implements Serializable {
    private List<NodeModel> nodeModels;
    private List<ArrowModel> arrowModels;
    private int nodeRadius;

    public AppState() {
    }
}
