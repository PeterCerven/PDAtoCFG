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

    public MyNode getSelectedNode(double x, double y) {
        MyNode myNode;
//        for (int i = this.MyNodes.size() - 1; i >= 0; i--) {
//            myNode = MyNodes.get(i);
//            if (myNode.isIn(x, y)) {
//                myNode.setSelected(true);
//                return myNode;
//            }
//        }
        return null;
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
