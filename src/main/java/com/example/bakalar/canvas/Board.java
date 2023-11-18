package com.example.bakalar.canvas;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board {
    private List<MyNode> nodes;
    private List<Arrow> arrows;
    private List<EndNode> endNodes;
    private MyNode startNode;
    private MyNode endNode;
    private AnchorPane mainPane;
    private StartNodeArrow startNodeArrow;

    public Board(AnchorPane mainPane) {
        this.nodes = new ArrayList<>();
        this.arrows = new ArrayList<>();
        this.mainPane = mainPane;
        this.startNodeArrow = new StartNodeArrow(0,0,0);
        this.addObject(startNodeArrow);
        this.startNodeArrow.setVisible(false);
        this.endNodes = new ArrayList<>();
    }


    public void removeObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.remove(arrow);
        } else if (node instanceof MyNode myNode) {
            nodes.remove(myNode);
        }
        mainPane.getChildren().remove(node);
    }

    public void addObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.add(arrow);
        } else if (node instanceof MyNode myNode) {
            myNode.setName("Q" + nodes.size());
            nodes.add(myNode);

        }
        mainPane.getChildren().add(node);
    }

    public void unSelectAllNodes() {
        for (MyNode node : nodes) {
            node.unselectNode();
        }
    }

    public void clearBoard() {
        mainPane.getChildren().clear();
    }


    public StartNodeArrow removeStartingFromOtherNodes() {
        for (MyNode node : nodes) {
            node.unSetStarting();
            node.getStartingCheckBox().setSelected(false);
        }
        this.startNodeArrow.setVisible(true);
        return this.startNodeArrow;
    }
}
