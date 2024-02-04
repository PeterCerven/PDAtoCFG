package com.example.bakalar.logic;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.node.MyNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardHistory {
    private List<MyNode> nodes;
    private List<Arrow> arrows;

    public BoardHistory(List<MyNode> nodes, List<Arrow> arrows) {
        // deep copy
        this.nodes = new ArrayList<>();
        this.arrows = new ArrayList<>();
        for (MyNode node : nodes) {
            this.nodes.add(node.clone());
        }
        for (Arrow arrow : arrows) {
            this.arrows.add(arrow.clone());
        }



    }
}
