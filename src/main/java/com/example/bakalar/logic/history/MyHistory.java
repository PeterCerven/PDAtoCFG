package com.example.bakalar.logic.history;

import lombok.Getter;
import lombok.Setter;

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
