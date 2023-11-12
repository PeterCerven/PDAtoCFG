package com.example.bakalar.canvas;

import com.example.bakalar.utils.State;
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
    private MyNode startNode;
    private MyNode endNode;
    private AnchorPane mainPane;

    public Board(AnchorPane mainPane) {
        this.nodes = new ArrayList<>();
        this.arrows = new ArrayList<>();
        this.mainPane = mainPane;
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

    public void clearBoard() {
        mainPane.getChildren().clear();
    }






}
