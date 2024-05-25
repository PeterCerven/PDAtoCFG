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

    public AppState createHistory(int nodeCounter, List<Arrow> arrows, List<MyNode> nodes) {
        AppState appState = new AppState();
        appState.setNodeCounter(nodeCounter);
        List<ArrowModel> arrowHistories = createArrowHistory(arrows);
        List<NodeModel> nodeHistories = createNodeHistory(nodes);
        appState.setNodes(nodeHistories);
        appState.setArrows(arrowHistories);
        return appState;
    }

    private List<ArrowModel> createArrowHistory(List<Arrow> arrows) {
        List<ArrowModel> arrowHistories = new ArrayList<>();
        for (Arrow arrow : arrows) {
            for (TransitionInputs transition : arrow.getTransitions()) {
                if (arrow instanceof LineArrow la) {
                    arrowHistories.add(new ArrowModel(la.getID(), la.getFrom().getID(), la.getTo().getID(),
                            transition.copy(), la.getControlPointChangeX(), la.getControlPointChangeY()));
                } else if (arrow instanceof SelfLoopArrow sla) {
                    arrowHistories.add(new ArrowModel(sla.getID(), sla.getFrom().getID(), sla.getTo().getID(), transition.copy()));
                }
            }
        }
        return arrowHistories;
    }

    private List<NodeModel> createNodeHistory(List<MyNode> nodes) {
        List<NodeModel> nodeHistories = new ArrayList<>();
        for (MyNode node : nodes) {
            nodeHistories.add(new NodeModel(node.getName(), node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY(),
                    node.isStarting(), node.isEnding(), node.getID()));
        }
        return nodeHistories;

    }

    public void saveCurrentState(int nodeCounter, List<Arrow> arrows, List<MyNode> nodes) {
        AppState myHistory = this.createHistory(nodeCounter, arrows, nodes);
        manageStack(undoStack);
        undoStack.push(myHistory);
        redoStack.clear();
        this.undoButton.setDisable(this.undoStack.isEmpty());
        this.redoButton.setDisable(this.redoStack.isEmpty());
    }

    public AppState undo(int nodeCounter, List<Arrow> arrows, List<MyNode> nodes) {
        return historyOperation(undoStack, redoStack, nodeCounter,  arrows, nodes);
    }

    public AppState redo(int nodeCounter, List<Arrow> arrows, List<MyNode> nodes) {
        return historyOperation(redoStack, undoStack, nodeCounter,  arrows, nodes);
    }

    private AppState historyOperation(Stack<AppState> redoStack, Stack<AppState> undoStack, int nodeCounter,
                                      List<Arrow> arrows, List<MyNode> nodes) {
        if (!redoStack.isEmpty()) {
            undoStack.push(this.createHistory(nodeCounter,  arrows, nodes));
            manageStack(undoStack);
            AppState nextState = redoStack.pop();
            this.undoButton.setDisable(this.undoStack.isEmpty());
            this.redoButton.setDisable(this.redoStack.isEmpty());
            return nextState;
        }
        return null;
    }

    private void manageStack(Stack<AppState> stack) {
        if (stack.size() >= MAX_HISTORY_SIZE) {
            stack.remove(0);
        }
    }


}
