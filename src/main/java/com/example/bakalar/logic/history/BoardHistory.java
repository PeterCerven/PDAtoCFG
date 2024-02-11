package com.example.bakalar.logic.history;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.node.MyNode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class BoardHistory {
    private Integer startNodeId;
    private int nodeCounter;
    private int idNodeCounter;

    public BoardHistory(Integer startNodeId, int nodeCounter, int idNodeCounter) {
        this.startNodeId = startNodeId;
        this.nodeCounter = nodeCounter;
        this.idNodeCounter = idNodeCounter;
    }
}
