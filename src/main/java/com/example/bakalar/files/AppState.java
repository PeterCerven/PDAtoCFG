package com.example.bakalar.files;

import com.example.bakalar.canvas.arrow.ArrowModel;
import com.example.bakalar.canvas.node.MyNodeModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AppState implements Serializable {
    private List<MyNodeModel> nodes;
    private List<ArrowModel> arrows;

    public AppState(List<MyNodeModel> nodes, List<ArrowModel> arrows) {
        this.nodes = nodes;
        this.arrows = arrows;
    }

    public AppState() {
    }
}
