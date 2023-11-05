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

    public Node getSelectedNode(double x, double y) {
        Node node;
        for (int i = this.nodes.size() - 1; i >= 0; i--) {
            node = nodes.get(i);
            if (node.isIn(x, y)) {
                node.setSelected(true);
                return node;
            }
        }
        return null;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void reset() {
        this.nodes = new ArrayList<>();
    }
}
