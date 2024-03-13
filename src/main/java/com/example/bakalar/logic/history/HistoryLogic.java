package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.Board;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class HistoryLogic {
    private Stack<AppState> undoStack;
    private Stack<AppState> redoStack;
    private Board board;

    public HistoryLogic() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public AppState createHistory() {
        AppState appState = new AppState();
        appState.setNodeCounter(board.getNodeCounter());
        List<ArrowModel> arrowHistories = createArrowHistory(board.getArrows());
        List<NodeModel> nodeHistories = createNodeHistory(board.getNodes());
        appState.setNodes(nodeHistories);
        appState.setArrows(arrowHistories);

        return appState;
    }

    private List<ArrowModel> createArrowHistory(List<Arrow> arrows) {
        List<ArrowModel> arrowHistories = new ArrayList<>();
        for (Arrow arrow : arrows) {
            for (TransitionInputs transition : arrow.getTransitions()) {
                if (arrow instanceof LineArrow la) {
                    arrowHistories.add(new ArrowModel(la.getFrom().getNodeId(), la.getTo().getNodeId(), transition.copy(), la.getControlPointChangeX(), la.getControlPointChangeY()));
                } else if (arrow instanceof SelfLoopArrow sla) {
                    arrowHistories.add(new ArrowModel(sla.getFrom().getNodeId(), sla.getTo().getNodeId(), transition.copy()));
                }
            }
        }
        return arrowHistories;
    }

    private List<NodeModel> createNodeHistory(List<MyNode> nodes) {
        List<NodeModel> nodeHistories = new ArrayList<>();
        for (MyNode node : nodes) {
            nodeHistories.add(new NodeModel(node.getName(), node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY(), node.isStarting(), node.isEnding(), node.getNodeId()));
        }
        return nodeHistories;

    }


    public MyNode findNodeById(Long toId) {
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
        AppState myHistory = this.createHistory();
        undoStack.push(myHistory);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(this.createHistory());
            AppState boardHistory = undoStack.pop();
            board.createBoardFromAppState(boardHistory);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(this.createHistory());
            AppState nextState = redoStack.pop();
            board.createBoardFromAppState(nextState);
        }
    }
}
