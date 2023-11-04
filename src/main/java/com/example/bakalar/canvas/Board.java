package com.example.bakalar.canvas;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Node> nodes;

    public Board() {
        this.nodes = new ArrayList<>();
    }


    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void draw() {
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void reset() {
        this.nodes = new ArrayList<>();
    }
}
