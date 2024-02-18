package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.node.MyNode;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MyHistory {
    private BoardHistory boardHistory;
    private List<NodeHistory> nodeHistory;
    private List<ArrowHistory> arrowHistory;

    public MyHistory(BoardHistory boardHistory, List<NodeHistory> nodeHistory, List<ArrowHistory> arrowHistory) {
        this.boardHistory = boardHistory;
        this.nodeHistory = nodeHistory;
        this.arrowHistory = arrowHistory;
    }


}
