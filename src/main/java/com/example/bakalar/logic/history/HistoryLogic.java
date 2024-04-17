package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.Board;
import javafx.scene.control.Button;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class HistoryLogic {
    private static final int MAX_HISTORY_SIZE = 15;

    private Stack<AppState> undoStack;
    private Stack<AppState> redoStack;
    private Board board;
    private Button undoButton;
    private Button redoButton;

    public HistoryLogic(Button undoButton, Button redoButton) {
        this.undoButton = undoButton;
        this.redoButton = redoButton;
        undoButton.setDisable(true);
        redoButton.setDisable(true);
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
                    arrowHistories.add(new ArrowModel(la.getArrowId(), la.getFrom().getNodeId(), la.getTo().getNodeId(),
                            transition.copy(), la.getControlPointChangeX(), la.getControlPointChangeY()));
                } else if (arrow instanceof SelfLoopArrow sla) {
                    arrowHistories.add(new ArrowModel(sla.getArrowId(), sla.getFrom().getNodeId(), sla.getTo().getNodeId(), transition.copy()));
                }
            }
        }
        return arrowHistories;
    }

    private List<NodeModel> createNodeHistory(List<MyNode> nodes) {
        List<NodeModel> nodeHistories = new ArrayList<>();
        for (MyNode node : nodes) {
            nodeHistories.add(new NodeModel(node.getName(), node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY(),
                    node.isStarting(), node.isEnding(), node.getNodeId()));
        }
        return nodeHistories;

    }

    public void saveCurrentState() {
        AppState myHistory = this.createHistory();
        manageStack(undoStack);
        undoStack.push(myHistory);
        redoStack.clear();
        this.undoButton.setDisable(this.undoStack.isEmpty());
        this.redoButton.setDisable(this.redoStack.isEmpty());
    }

    public void undo() {
        historyOperation(undoStack, redoStack);
    }

    public void redo() {
        historyOperation(redoStack, undoStack);
    }

    private void historyOperation(Stack<AppState> redoStack, Stack<AppState> undoStack) {
        if (!redoStack.isEmpty()) {
            undoStack.push(this.createHistory());
            manageStack(undoStack);
            AppState nextState = redoStack.pop();
            board.createBoardFromAppState(nextState);
            this.undoButton.setDisable(this.undoStack.isEmpty());
            this.redoButton.setDisable(this.redoStack.isEmpty());
        }
    }

    private void manageStack(Stack<AppState> stack) {
        if (stack.size() >= MAX_HISTORY_SIZE) {
            stack.remove(0);
        }
    }
}
