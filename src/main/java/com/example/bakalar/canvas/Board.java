package com.example.bakalar.canvas;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.node.EndNode;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.StartNodeArrow;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board {
    private static final Logger log = LogManager.getLogger(Board.class.getName());
    private List<MyNode> nodes;
    private List<Arrow> arrows;
    private List<EndNode> endNodes;
    private MyNode startNode;
    private MyNode endNode;
    private AnchorPane mainPane;
    private StartNodeArrow startNodeArrow;
    private int nodeCounter;

    public Board(AnchorPane mainPane) {
        this.nodes = new ArrayList<>();
        this.arrows = new ArrayList<>();
        this.mainPane = mainPane;
        this.startNodeArrow = new StartNodeArrow(0, 0, 0);
        this.addObject(startNodeArrow);
        this.startNodeArrow.setVisible(false);
        this.endNodes = new ArrayList<>();
        this.nodeCounter = 0;
    }


    public void removeObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.remove(arrow);
        } else if (node instanceof MyNode myNode) {
            nodes.remove(myNode);
        }
        mainPane.getChildren().remove(node);
    }

    public Arrow createArrow(MyNode from, MyNode to) {
        from.unselectNode();
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            arrow.addSymbolContainer();
            return arrow;
        }
        if (from == to) {
            arrow = new SelfLoopArrow(from, to);
        } else {
            arrow = new LineArrow(from, to);
        }
        from.addArrow(arrow);
        to.addArrow(arrow);
        this.addObject(arrow);
        return arrow;
    }

    public void addObject(Node node) {
        log.info("Adding object: " + node);
        if (node instanceof Arrow arrow) {
            arrows.add(arrow);
        } else if (node instanceof MyNode myNode) {
            myNode.setName("Q" + nodeCounter++);
            nodes.add(myNode);

        }
        mainPane.getChildren().add(node);
    }

    private Arrow sameArrowExists(MyNode from, MyNode to) {
        for (Arrow arrow : arrows) {
            if (arrow.getFrom() == from && arrow.getTo() == to) {
                return arrow;
            }
        }
        return null;
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
