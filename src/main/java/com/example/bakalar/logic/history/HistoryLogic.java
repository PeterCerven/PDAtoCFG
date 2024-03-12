package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.Board;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

@Getter
@Setter
public class HistoryLogic {
    private Stack<MyHistory> undoStack;
    private Stack<MyHistory> redoStack;
    private Board board;

    public HistoryLogic() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public MyHistory createHistory() {
        BoardHistory boardHistory = new BoardHistory(board.getStartNode() == null ? null : board.getStartNode().getNodeId(),
                board.getNodeCounter(), board.getIdNodeCounter());
        List<ArrowHistory> arrowHistories = createArrowHistory(board.getArrows());
        List<NodeHistory> nodeHistories = createNodeHistory(board.getNodes());

        return new MyHistory(boardHistory, nodeHistories, arrowHistories);
    }

    private List<ArrowHistory> createArrowHistory(List<Arrow> arrows) {
        List<ArrowHistory> arrowHistories = new ArrayList<>();
        for (Arrow arrow : arrows) {
            if (arrow instanceof LineArrow lineArrow) {
                arrowHistories.add(new ArrowHistory(lineArrow.getStartX(), lineArrow.getStartY(), lineArrow.getEndX(), lineArrow.getEndY(),
                        lineArrow.getFromId(), lineArrow.getToId(), lineArrow.getTransitions(), lineArrow.getControlX(), lineArrow.getControlY(), true));
            } else if (arrow instanceof SelfLoopArrow selfLoopArrow) {
                arrowHistories.add(new ArrowHistory(selfLoopArrow.getFromId(), selfLoopArrow.getToId(), selfLoopArrow.getTransitions(), false));
            }
        }
        return arrowHistories;
    }

    private List<NodeHistory> createNodeHistory(List<MyNode> nodes) {
        List<NodeHistory> nodeHistories = new ArrayList<>();
        for (MyNode node : nodes) {
            nodeHistories.add(new NodeHistory(node.getName(), node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY(), node.isStarting(), node.isEnding(), node.getNodeId()));
        }
        return nodeHistories;

    }


    public MyNode findNodeById(UUID toId) {
        for (MyNode node : board.getNodes()) {
            if (node.getNodeId().equals(toId)) {
                return node;
            }
        }
        return null;
    }

    public Node addObjectFromHistory(Node node, List<MyNode> nodes, List<Arrow> arrows) {
        if (node instanceof Arrow arrow) {
            arrows.add(arrow);
        } else if (node instanceof MyNode myNode) {
            nodes.add(myNode);
        }
        return node;
    }

    public void saveCurrentState() {
        MyHistory myHistory = this.createHistory();
        undoStack.push(myHistory);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(this.createHistory());
            MyHistory boardHistory = undoStack.pop();
            board.restoreBoardFromHistory(boardHistory);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(this.createHistory());
            MyHistory nextState = redoStack.pop();
            board.restoreBoardFromHistory(nextState);
        }
    }
}
