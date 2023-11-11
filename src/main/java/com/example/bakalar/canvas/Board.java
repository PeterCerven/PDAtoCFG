package com.example.bakalar.canvas;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private List<MyNode> MyNodes;

    public Board() {
        this.MyNodes = new ArrayList<>();
    }


    public List<MyNode> getNodes() {
        return MyNodes;
    }

    public void setNodes(List<MyNode> MyNodes) {
        this.MyNodes = MyNodes;
    }


    public void addNode(MyNode myNode) {
        this.MyNodes.add(myNode);
    }

    public void reset() {
        this.MyNodes = new ArrayList<>();
    }

    public void remove(MyNode myNode) {
        this.MyNodes.remove(myNode);
    }
}
